package com.lela.commons.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.channel.ChannelDecisionManagerImpl;
import org.springframework.security.web.access.channel.ChannelProcessor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/17/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChannelPostProcessor implements BeanPostProcessor {
    private final static Logger log = LoggerFactory.getLogger(ChannelPostProcessor.class);

    private List<ChannelProcessor> channelProcessors;

    public static final String AFFILIATE_SERVICE = "affiliateService";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ChannelDecisionManagerImpl)
        {
            log.info("ChannelPostProcessor - postProcessBeforeInitialization");
            ((ChannelDecisionManagerImpl)bean).setChannelProcessors(channelProcessors);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void setChannelProcessors(List<ChannelProcessor> channelProcessors) {
        this.channelProcessors = channelProcessors;
    }
}
