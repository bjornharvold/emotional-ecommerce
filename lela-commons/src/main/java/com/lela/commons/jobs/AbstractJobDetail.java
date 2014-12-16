/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs;


import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 10:49 AM
 */
public abstract class AbstractJobDetail extends JobDetail implements BeanNameAware, InitializingBean, IngestJobDetail {
    private String beanName;
    private String groupName;
    List<IngestJobInterceptor> interceptors;
    private JobExecutor jobExecutor;
    private List<JobParameter> parameters;

    private boolean allowConcurrentRuns = true;

    @Override
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public List<IngestJobInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<IngestJobInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public JobExecutor getJobExecutor() {
        return jobExecutor;
    }

    public void setJobExecutor(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    @Override
    public Class getJobClass() {
        return QuartzJobAdapter.class;
    }

    @Override
    public boolean isDurable() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getName() == null) {
            setName(this.beanName);
        }
        if (getGroup() == null) {
            setGroup(Scheduler.DEFAULT_GROUP);
        }
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setParameters(List<JobParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public List<JobParameter> getParameters() {
        return parameters;
    }

    public boolean isAllowConcurrentRuns() {
        return allowConcurrentRuns;
    }

    public void setAllowConcurrentRuns(boolean allowConcurrentRuns) {
        this.allowConcurrentRuns = allowConcurrentRuns;
    }
}
