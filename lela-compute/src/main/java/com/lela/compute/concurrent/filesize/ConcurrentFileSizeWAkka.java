/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.compute.concurrent.filesize;

import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.ActorRegistry;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

class RequestAFile {
}

class FileSize {
    public final long size;

    public FileSize(final long fileSize) {
        size = fileSize;
    }
}

class FileToProcess {
    public final String fileName;

    public FileToProcess(final String name) {
        fileName = name;
    }
}

class FileProcessor extends UntypedActor {
    private final ActorRef sizeCollector;

    public FileProcessor(final ActorRef theSizeCollector) {
        sizeCollector = theSizeCollector;
    }

    @Override
    public void preStart() {
        registerToGetFile();
    }

    public void registerToGetFile() {
        sizeCollector.tell(new RequestAFile(), getContext());
    }

    public void onReceive(final Object message) {
        FileToProcess fileToProcess = (FileToProcess) message;
        final File file = new File(fileToProcess.fileName);
        long size = 0L;
        if (file.isFile()) {
            size = file.length();
        } else {
            File[] children = file.listFiles();
            if (children != null)
                for (File child : children)
                    if (child.isFile())
                        size += child.length();
                    else
                        sizeCollector.tell(new FileToProcess(child.getPath()));
        }

        sizeCollector.tell(new FileSize(size));
        registerToGetFile();
    }
}

class SizeCollector extends UntypedActor {
    private final List<String> toProcessFileNames = new ArrayList<String>();
    private final List<ActorRef> idleFileProcessors =
            new ArrayList<ActorRef>();
    private long pendingNumberOfFilesToVisit = 0L;
    private long totalSize = 0L;
    private long start = System.nanoTime();

    public void sendAFileToProcess() {
        if (!toProcessFileNames.isEmpty() && !idleFileProcessors.isEmpty())
            idleFileProcessors.remove(0).tell(
                    new FileToProcess(toProcessFileNames.remove(0)));
    }

    public void onReceive(final Object message) {
        if (message instanceof RequestAFile) {
            idleFileProcessors.add(getContext().getSender().get());
            sendAFileToProcess();
        }

        if (message instanceof FileToProcess) {
            toProcessFileNames.add(((FileToProcess) (message)).fileName);
            pendingNumberOfFilesToVisit += 1;
            sendAFileToProcess();
        }

        if (message instanceof FileSize) {
            totalSize += ((FileSize) (message)).size;
            pendingNumberOfFilesToVisit -= 1;

            if (pendingNumberOfFilesToVisit == 0) {
                long end = System.nanoTime();
                System.out.println("Total size is " + totalSize);
                System.out.println("Time taken is " + (end - start) / 1.0e9);
                Actors.registry().shutdownAll();
            }
        }
    }
}

public class ConcurrentFileSizeWAkka {
    public static void main(final String[] args) {
        final ActorRef sizeCollector =
                Actors.actorOf(SizeCollector.class).start();

        sizeCollector.tell(new FileToProcess(args[0]));

        for (int i = 0; i < 100; i++)
            Actors.actorOf(new UntypedActorFactory() {
                public UntypedActor create() {
                    return new FileProcessor(sizeCollector);
                }
            }).start();
    }
}
