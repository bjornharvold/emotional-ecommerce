/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.talend;

import com.lela.commons.LelaException;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.AbstractExecutionContext;
import com.lela.commons.service.IngestJobService;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class TalendExecutionContext extends AbstractExecutionContext<TalendJobDetail> implements ExecutionContext {

    private static final Logger log = LoggerFactory.getLogger(TalendExecutionContext.class);
    
    private static final String TARGET_METHOD               = "runJobInTOS";
    private static final String CONTEXT_TOKEN               = "--context=DEFAULT";
    private static final String CONTEXT_PARAM_TOKEN         = "--context_param";
    
    private static final String CONTEXT_PARAM_LOGPATH       = "LelaETL_LogPath";
    private static final String CONTEXT_PARAM_LOGFILE       = "LelaETL_LogFile";
    public static final String LOG_FILE_EXTENSION = ".log";
    public static final int TAIL_DELAY_MSEC = 1000;
    public static final int TAIL_FINAL_DELAY = 3000;

    private final Map<String, String> defaultContext;
    private final Map<String, String> parameters;
    
    public TalendExecutionContext(TalendJobDetail jobDetail, Map<String, String> defaultContext, IngestJobService ingestJobService, Map<String, String> parameters) {
        super(jobDetail, ingestJobService);
        this.defaultContext = defaultContext;
        this.parameters = parameters;
    }

    protected void executeInternal(JobExecutionContext quartzContext) {

        Tailer tailer = null;
        File temp = null;
        try {

            // Get the Talend job "main" class and execute it
            Class talendClass = this.getClass().getClassLoader().loadClass(getJobDetail().getTalendClass());

            // Create instance of the class
            Object instance = talendClass.newInstance();

            // Find the "runJobInTOS" method
            Method method = talendClass.getMethod(TARGET_METHOD, String[].class);

            // Create a temp file for talend job logs
            temp = File.createTempFile(talendClass.getName(), LOG_FILE_EXTENSION);
            temp.deleteOnExit();

            // Create a log file tailer
            TailerListener listener = new LogTailerListener(getJobDetail());
            tailer = Tailer.create(temp, listener, TAIL_DELAY_MSEC, true);

            // Build the arguments list
            List<String> argsList = new ArrayList<String>();

            // add a context argument which is the only way to get Talend to read the args
            argsList.add(CONTEXT_TOKEN);
            log.info(message("Argument: " + CONTEXT_TOKEN));

            // Add the temp log file
            String logPath = temp.getParent().replace('\\', '/');
            String logFile = temp.getName();
            argsList.add(CONTEXT_PARAM_TOKEN);
            argsList.add(CONTEXT_PARAM_LOGPATH + "=" + logPath);
            log.info(message("Argument: " + CONTEXT_PARAM_TOKEN + " " + CONTEXT_PARAM_LOGPATH + "=" + logPath));

            argsList.add(CONTEXT_PARAM_TOKEN);
            argsList.add(CONTEXT_PARAM_LOGFILE + "=" + logFile);
            log.info(message("Argument: " + CONTEXT_PARAM_TOKEN + " " + CONTEXT_PARAM_LOGFILE + "=" + logFile));

            // Add anything configured in the XML config
            Map<String, String> mergedContext = mergeContext(defaultContext, getJobDetail().getContext(), parameters);
            if (mergedContext != null) {
                for (String key : mergedContext.keySet()) {
                    if (StringUtils.hasText(mergedContext.get(key))) {
                        argsList.add(CONTEXT_PARAM_TOKEN);
                        argsList.add(key + "=" + mergedContext.get(key));

                        String displayValue = mergedContext.get(key);
                        if (key.toLowerCase().contains("pass")) {
                            displayValue = displayValue.replaceAll(".", "*");
                        }
                        log.info(message("Argument: " + CONTEXT_PARAM_TOKEN + " " + key + "=" + displayValue));
                    }                    
                }
            }
            
            String[] args = argsList.toArray(new String[0]);

            log.info(message("Invoking Talend job: " + getJobDetail().getTalendClass()));
            Object exitCode = method.invoke(instance, (Object)args);
            if (exitCode instanceof Integer && ((Integer)exitCode) != 0) {
                setErrorResult();
                message("ERROR while executing");
            }

            message("Talend job exit code: " + exitCode);

            log.info(message("Finished job: " + getJobDetail().getName()));
        } catch (RuntimeException e) {
            setFatalResult();
            log.error(message("Error occurred during job", e));
            throw e;
        } catch (Exception e) {
            setFatalResult();
            log.error(message("Error occurred during job", e));
            throw new LelaException("Error occurred during job", e);
        } finally {
            // Stop the tailer
            if (tailer != null) {
                // Wait for the tailer IF the file has length
                if (temp != null && temp.length() > 0) {
                    try {
                        message("Waiting for Talend log tail to complete");
                        Thread.sleep(TAIL_FINAL_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                tailer.stop();
            }
        }
    }

    private Map<String, String> mergeContext(Map<String, String> defaultContext, Map<String, String> jobContext, Map<String, String> parameters) {
        Map<String, String> merged = new HashMap<String, String>();

        // Start with the default Talend context
        if (defaultContext != null) {
            for (String key : defaultContext.keySet()) {
                merged.put(key, defaultContext.get(key));
            }
        }

        // Overlay any ingest configured context values specific to the job
        if (jobContext != null) {
            for (String key : jobContext.keySet()) {
                merged.put(key, jobContext.get(key));
            }
        }

        // Overlay any user parameters
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                merged.put(key, parameters.get(key));
            }
        }

        return merged;
    }

    private class LogTailerListener extends TailerListenerAdapter {

        private JobDetail detail;

        public LogTailerListener(JobDetail detail) {
            this.detail = detail;
        }
        
        @Override
        public void handle(String line) {
            message("[TALEND LOG] " + line);
        }
    }

}
