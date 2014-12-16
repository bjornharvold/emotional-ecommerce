package com.lela.data.queue;

import com.polarrose.amazon.spring.sqs.MessageHandler;
import com.polarrose.amazon.spring.sqs.QueueListener;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 10/28/12
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisabledQueueListener implements QueueListener {
    @Override
    public void setService(SqsService sqsService) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SqsService getService() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setQueue(SqsQueue sqsQueue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SqsQueue getQueue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MessageHandler getMessageHandler() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getThreadPoolSize() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setThreadPoolSize(int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPollInterval(int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPollInterval() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
