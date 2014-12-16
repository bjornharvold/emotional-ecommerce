package com.lela.commons.jobs;

import com.lela.commons.LelaException;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.ingest.JobErrorEvent;
import com.lela.commons.event.ingest.JobFailedEvent;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.document.JobExecution;
import com.lela.domain.document.JobKey;
import com.lela.domain.enums.JobResult;
import com.lela.domain.enums.JobStatus;
import org.bson.types.ObjectId;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 3/2/12
 * Time: 11:50 AM
 */
public abstract class AbstractExecutionContext<T extends AbstractJobDetail> implements ExecutionContext {
    private static final Logger log = LoggerFactory.getLogger(AbstractExecutionContext.class);

    private static final String LINE_SEPARATOR = "\n";

    private List<Object> processed = new ArrayList<Object>();
    private final T jobDetail;
    private final IngestJobService ingestJobService;

    private JobExecution execution;

    private boolean firedErrorEvent = false;
    private boolean firedFailureEvent = false;


    public AbstractExecutionContext(T jobDetail, IngestJobService ingestJobService) {
        this.jobDetail = jobDetail;
        this.ingestJobService = ingestJobService;

        this.execution = new JobExecution(new JobKey(jobDetail.getName()));
        this.execution.setId(new ObjectId());
    }

    //
    // LIFECYCLE
    //

    protected void preIngest() {}
    protected abstract void executeInternal(JobExecutionContext quartzContext);
    protected void postIngest() {}

    //
    // EXECUTION
    //

    public void execute(JobExecutionContext quartzContext) {

        log.debug(message("Starting job: " + getJobDetail().getName()));
        start();

        try {
            // Call pre ingest hook
            preIngest();

            // Call ingest starting listeners
            callIngestStarting();

            executeInternal(quartzContext);
        } catch (Exception e) {
            setFatalResult();
            log.error(message("Fatal Error occurred during job", e));
            throw new LelaException("Fatal Error occurred during job", e);
        } finally {

            // Set the success result if nothing was set by the implementing class
            if (getResult() == null) {
                setResult(JobResult.SUCCESS);
            }

            // Call ingest completed listener
            try {
                // If the job was already a Fatal, don't do this
                if (!JobResult.FATAL.equals(getResult())) {
                    callIngestCompleted();
                }
            } finally {

                // Try the post ingest hook
                try {
                    postIngest();
                } finally {
                    // Mark the job as ended
                    end();

                    // Set the quartz result
                    quartzContext.setResult(getResult());
                }
            }
        }
    }

    protected void addProcessed(Object object) {
        processed.add(object);
    }

    protected void addProcessed(Collection<Object> objects) {
        processed.addAll(objects);
    }

    //
    // Job execution attributes
    //

    public void putAttrib(String key, Object value) {
        execution.getAttribs().put(key, value);
        saveJobExecution();
    }

    public Object getAttrib(String key) {
        return execution.getAttribs().get(key);
    }

    public Integer addToAttrib(String key, Integer addend) {
        Integer result;
        synchronized (execution.getAttribs()) {
            Integer current = (Integer)execution.getAttribs().get(key);
            if (current == null) {
                current = 0;
            }

            result = current + addend;
            execution.getAttribs().put(key, result);
        }

        saveJobExecution();
        return result;
    }

    private void start() {
        setStatus(JobStatus.RUNNING);
        setStartDate(new Date());
    }

    private void end() {
        setStatus(JobStatus.DONE);
        setEndDate(new Date());
    }

    private void callIngestStarting() {
        if (getJobDetail().getInterceptors() != null) {
            for (IngestJobInterceptor listener : getJobDetail().getInterceptors()) {
                boolean okayToContinue = listener.ingestStarting(this);
                if (!okayToContinue) {
                    throw new RuntimeException("Ingest Listener failed on ingestStarting: " + listener.getClass());
                }
            }
        }
    }

    private void callIngestCompleted() {
        try {
            if (getJobDetail().getInterceptors() != null) {
                for (IngestJobInterceptor listener : getJobDetail().getInterceptors()) {
                    boolean okayToContinue = listener.ingestCompleted(this);
                    if (!okayToContinue) {
                        throw new RuntimeException("Ingest Listener failed on ingestCompleted: " + listener.getClass());
                    }
                }
            }
        } catch (RuntimeException e) {
            setFatalResult();
            throw e;
        } catch (Exception e) {
            setFatalResult();
            throw new RuntimeException("Caught exception in listener.ingestCompleted", e);
        }
    }

    public String message(Object message) {
        return message(message, null);
    }

    public String message(Object message, Exception e) {
        ingestJobService.saveJobMessage(execution.getId(), String.valueOf(message), e);
        return "Job: [" + getJobDetail().getName() + "] - " + message;
    }

    public T getJobDetail() {
        return jobDetail;
    }

    public List<Object> getProcessed() {
        return processed;
    }
    
    public Date getStartDate() {
        return execution.getStartDate();
    }

    public Date getEndDate() {
        return execution.getEndDate();
    }

    public JobStatus getStatus() {
        return execution.getStatus();
    }

    @Override
    public void setErrorResult() {
        if (!JobResult.FATAL.equals(getResult())) {

            setResult(JobResult.ERROR);
        }
    }

    @Override
    public void setFatalResult() {
        setResult(JobResult.FATAL);
    }

    public JobResult getResult() {
        return execution.getResult();
    }

    private void setStatus(JobStatus status) {
        this.execution.setStatus(status);
        saveJobExecution();
    }

    private void setResult(JobResult result) {
        if(result.equals(JobResult.ERROR) && !firedErrorEvent){
            EventHelper.post(new JobErrorEvent(this.jobDetail.getName(), new Date()));
            firedErrorEvent = true;
        }
        else if (result.equals(JobResult.FATAL) && !firedFailureEvent){
            EventHelper.post(new JobFailedEvent(this.jobDetail.getName(), new Date()));
            firedFailureEvent = true;
        }
        this.execution.setResult(result);
        saveJobExecution();
    }

    private void setStartDate(Date startDate) {
        this.execution.setStartDate(startDate);
        saveJobExecution();
    }

    private void setEndDate(Date endDate) {
        this.execution.setEndDate(endDate);
        saveJobExecution();
    }
    private void saveJobExecution() {
        ingestJobService.saveJobExecution(execution);
    }
}
