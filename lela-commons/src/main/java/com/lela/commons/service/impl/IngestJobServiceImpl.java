/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.LelaException;
import com.lela.commons.jobs.AvailableJobs;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.IngestJobDetail;
import com.lela.commons.jobs.JobExecutor;
import com.lela.commons.jobs.JobSummary;
import com.lela.commons.jobs.TriggerDetail;
import com.lela.domain.document.JobKey;
import com.lela.commons.jobs.JobListing;
import com.lela.commons.jobs.JobNameComparator;
import com.lela.commons.repository.JobExecutionRepository;
import com.lela.commons.repository.JobMessageRepository;
import com.lela.commons.service.IngestJobService;
import com.lela.commons.service.TriggerService;
import com.lela.domain.document.CronTrigger;
import com.lela.domain.document.JobExecution;
import com.lela.domain.document.JobMessage;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/2/11
 * Time: 8:45 AM
 */
public class IngestJobServiceImpl implements IngestJobService {

    private final static Logger log = LoggerFactory.getLogger(IngestJobServiceImpl.class);
    private final SchedulerFactoryBean scheduler;
    private final Scheduler quartzScheduler;
    private final JobExecutionRepository jobExecutionRepository;
    private final JobMessageRepository jobMessageRepository;
    private final TriggerService triggerService;
    
    @Autowired
    public IngestJobServiceImpl(JobExecutionRepository jobExecutionRepository, 
    		JobMessageRepository jobMessageRepository, 
    		SchedulerFactoryBean scheduler,
    		TriggerService triggerService,
    		@Qualifier("ingestJobScheduler") Scheduler quartzScheduler
    		) {
    	this.scheduler = scheduler;
    	this.jobExecutionRepository = jobExecutionRepository;
    	this.jobMessageRepository = jobMessageRepository;
    	this.triggerService = triggerService;
    	this.quartzScheduler = quartzScheduler;
    	
    }
    @Resource(name = "ingestExecutors")
    private Map<String, JobExecutor<IngestJobDetail, ExecutionContext>> jobExecutors;

    @Value("${ingest.logs.retain.days:5}")
    private Integer retainDays;

    private JobNameComparator nameComparator = new JobNameComparator();

    @Override
    public AvailableJobs getAvailableJobs() {

        try {
            AvailableJobs results = new AvailableJobs();
            Map<String, List<JobListing>> jobs = new HashMap<String, List<JobListing>>();
            List<JobListing> flatJobListingList = new ArrayList<JobListing>();
            results.setJobs(jobs);

            for (String type : jobExecutors.keySet()) {
                for (IngestJobDetail job : jobExecutors.get(type).getJobs()) {
                    JobKey key = new JobKey(job.getName());
                    CronTrigger lastAssignedTriggerToJob = new CronTrigger(CronTrigger.UN_ASSIGNED_TRIGGER_URLNAME, CronTrigger.UN_ASSIGNED_TRIGGER_DESC); //Will assume that there is one trigger per job
                    // Create the job listing DTO; Get any triggers
                    Trigger[] triggers = scheduler.getScheduler().getTriggersOfJob(key.getJobName(), key.getGroupName());
                    List<Trigger> list = new ArrayList<Trigger>();
                    List<TriggerDetail> triggerDetailList = new ArrayList<TriggerDetail>();
                    if (triggers != null && triggers.length > 0) {
                        for (Trigger trigger : triggers) {
                            if (trigger.getNextFireTime() != null) {
                                list.add(trigger); //trigger.getDescription()
                                TriggerDetail td = new TriggerDetail();
                                td.setKey(trigger.getKey().toString());
                                int state = scheduler.getScheduler().getTriggerState(trigger.getName(), trigger.getGroup());
                                td.setStatus(Integer.toString(state));
                                td.setNextRunTime(trigger.getNextFireTime());
                                lastAssignedTriggerToJob = triggerService.findTriggerByUrlName(trigger.getName());
                                if (lastAssignedTriggerToJob == null){
                                	//Trigger exists but not user defined
                                	lastAssignedTriggerToJob = new CronTrigger(CronTrigger.CONFIG_ASSIGNED_TRIGGER_URLNAME, CronTrigger.CONFIG_ASSIGNED_TRIGGER_DESC);
                                }
                                triggerDetailList.add(td);
                            }
                        }
                    }

                    JobListing listing = new JobListing(key, type, job.getParameters(), list, getSummary(key));
                    listing.setTriggerDetailList(triggerDetailList);
                    listing.setCurrentTrigger(lastAssignedTriggerToJob);

                    String group = job.getGroupName() != null ? job.getGroupName() : jobExecutors.get(type).getDisplayName();
                    List<JobListing> jobList = jobs.get(group);
                    if (jobList == null) {
                        jobList = new ArrayList<JobListing>();
                        jobs.put(group, jobList);
                    }

                    jobList.add(listing);
                    flatJobListingList.add(listing);
                }
            }

            // Sort the keys within each group
            for (String group : jobs.keySet()) {
                // Sort the keys
                Collections.sort(jobs.get(group));
            }

            // Set the list of sorted groups
            List<String> groups = new ArrayList<String>(jobs.keySet());
            Collections.sort(groups);
            results.setGroups(groups);
            return results;
        } catch (SchedulerException e) {
            throw new LelaException("Could not get job names from Scheduler", e);
        }
    }

    @Override
    public JobListing getJobListing(JobKey jobKey) {
        JobListing result = null;

        OUTER: for (String type : jobExecutors.keySet()) {
            for (IngestJobDetail job : jobExecutors.get(type).getJobs()) {
                if (jobKey.getJobName().equals(job.getName())) {
                    result = new JobListing(jobKey, type, job.getParameters());
                    break OUTER;
                }
            }
        }

        return result;
    }

    @Override
    public boolean executeJob(JobKey jobKey, Map<String, String> parameters) {

        try {
            for (Object each : scheduler.getScheduler().getCurrentlyExecutingJobs()) {
                JobExecutionContext context = (JobExecutionContext)each;

                if (context.getJobDetail().getName().equals(jobKey.getJobName()) &&
                    context.getJobDetail().getGroup().equals(jobKey.getGroupName())) {

                    log.info("Already running job: " + jobKey);
                    return false;
                }
            }
        } catch (SchedulerException e) {
            throw new LelaException("Failure to query job status", e);
        }

        try {
            log.info("Executing job: " + jobKey);
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ApplicationConstants.JOB_PARAMETERS, parameters);
            scheduler.getScheduler().triggerJob(jobKey.getJobName(), jobKey.getGroupName(), dataMap);
            return true;
        } catch (SchedulerException e) {
            throw new LelaException("Failure to execute job: " + jobKey, e);
        }
    }

    @Override
    public IngestJobDetail getJob(JobKey jobKey) {

        IngestJobDetail result = null;

        for (JobExecutor<IngestJobDetail, ExecutionContext> executor : jobExecutors.values()) {
            result = executor.getJob(jobKey);
            if (result != null) {
                break;
            }
        }

        return result;
    }
    
    @Override
    public void pauseJob(JobKey jobKey)  throws Exception {
    	Scheduler quartzSheduler =  (Scheduler)scheduler.getScheduler();
    	quartzSheduler.pauseJob(jobKey.getJobName(), jobKey.getGroupName());
    }
    
    @Override
    public void resumeJob(JobKey jobKey)  throws Exception {
    	Scheduler quartzSheduler =  (Scheduler)scheduler.getScheduler();
    	quartzSheduler.resumeJob(jobKey.getJobName(), jobKey.getGroupName());
    }

    @Override
    public JobSummary getSummary(JobKey jobKey) {
        JobSummary result = null;

        // Query the job execution
        JobExecution execution = jobExecutionRepository.findLatestForJobKey(jobKey);
        if (execution != null) {
            result = new JobSummary();
            result.setExecution(execution);
            result.setMessages(jobMessageRepository.findAllJobMessages(execution.getId()));
        }

        return result;
    }

    @Override
    public void markAllJobExecutionsAsEnded() {
        jobExecutionRepository.markAllAsEnded();
    }

    @Override
    public JobExecution saveJobExecution(JobExecution jobExecution) {
        return jobExecutionRepository.save(jobExecution);
    }

    @Override
    public JobExecution findJobExecution(ObjectId jobExecutionId) {
        return jobExecutionRepository.findOne(jobExecutionId);
    }

    @Override
    public JobMessage saveJobMessage(ObjectId jobExecutionId, String message) {
        return saveJobMessage(jobExecutionId, message, null);
    }

    @Override
    public JobMessage saveJobMessage(ObjectId jobExecutionId, String message, Exception e) {
        if (jobExecutionId == null) {
            throw new IllegalArgumentException("Job execution id cannot be null");
        }

        JobMessage data = new JobMessage();
        data.setJobExecutionId(jobExecutionId);
        data.setMessage(message);

        if (e != null) {
            StringWriter writer = new StringWriter();
            PrintWriter printer = new PrintWriter(writer);
            e.printStackTrace(printer);

            data.setException(writer.getBuffer().toString());
        }

        return jobMessageRepository.save(data);
    }

    @Override
    public List<JobMessage> getMessages(ObjectId jobExecutionId) {
        return jobMessageRepository.findAllJobMessages(jobExecutionId);
    }

    @Override
    public void removeOldJobLogs() {
        DateTime retainDate = new DateTime();
        retainDate = retainDate.minusDays(retainDays);

        jobExecutionRepository.removeOlderThan(new Date(retainDate.getMillis()));
        jobMessageRepository.removeOlderThan(new Date(retainDate.getMillis()));
    }
    
    /**
     * Assigns the trigger corresponding to the passed in triggerUrl to the job represented by JobKey
     * At this time only one trigger per job is supported.
     */
    public void assignTriggerToJob(String jobName, String triggerId) throws Exception  {
    	String DEFAULT_GROUP = "DEFAULT";
    	JobDetail jobDetail = quartzScheduler.getJobDetail(jobName, DEFAULT_GROUP);
    	Assert.notNull(jobDetail, String.format("JobDetail for name %s cannot be found", jobName));
    	
    	//Check if we want to remove or add triggers
    	if (StringUtils.equals(CronTrigger.UN_ASSIGNED_TRIGGER_URLNAME, triggerId)){
    		//remove all triggers of this job
    		Trigger[] triggerArray = quartzScheduler.getTriggersOfJob(jobName, DEFAULT_GROUP);
    		for (Trigger trigger : triggerArray) {
    			quartzScheduler.unscheduleJob(trigger.getName(), DEFAULT_GROUP);
			}
    	} else if (StringUtils.equals(CronTrigger.CONFIG_ASSIGNED_TRIGGER_URLNAME, triggerId)){
    			// noop
    	} else {
    		//add the specified trigger
	    	CronTrigger trigger = triggerService.findTriggerByUrlName(triggerId);
	    	Assert.notNull(trigger, String.format("Trigger with URL %s not found in configuration", triggerId));
	    	
	    	Trigger[] existingTriggerArray =  quartzScheduler.getTriggersOfJob(jobName, DEFAULT_GROUP);
	    	if (existingTriggerArray != null) {
	    		for (Trigger trigger2 : existingTriggerArray) {
	    			quartzScheduler.unscheduleJob(trigger2.getName(), DEFAULT_GROUP);
				}
	    	}
	    	
	    	//Build a cron trigger with the new expression
	    	CronTriggerFactoryBean ctfb = new CronTriggerFactoryBean();
	    	ctfb.setBeanName(trigger.getRlnm());
	    	ctfb.setCronExpression(trigger.getCrnxprsn());
	    	//ctfb.setCronExpression("0 0/1 * * * ?");
	    	ctfb.setJobDetail(jobDetail); 
	    	ctfb.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);  
	    	ctfb.afterPropertiesSet();
	    	org.quartz.CronTrigger newTrigger = ctfb.getObject();
	    	quartzScheduler.scheduleJob(newTrigger);
    	}
    	
    }

    private Set<JobKey> getCurrentlyRunningJobs() {
        
        Set<JobKey> runningJobs = new HashSet<JobKey>();
        try {
            for (Object each: scheduler.getScheduler().getCurrentlyExecutingJobs()) {
                JobExecutionContext context = (JobExecutionContext)each;
                runningJobs.add(new JobKey(context.getJobDetail().getName()));
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("Could not retrieve running jobs", e);
        }

        return runningJobs;
    }
}
