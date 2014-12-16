/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import com.google.common.eventbus.EventBus;

/**
 * User: Chris Tallent
 * Date: 10/31/12
 * Time: 3:56 PM
 */
public class EventBusRegistrationBeanPostProcessor implements BeanPostProcessor, Ordered {
	
	private static final Logger log = LoggerFactory.getLogger(EventBusRegistrationBeanPostProcessor.class);
    private final EventBus eventBus;
    
    private Object preInitializedBean; 

    public EventBusRegistrationBeanPostProcessor(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    	log.debug("Registering bean : " + bean.getClass().getName() + " with EventBus before initialization");
    	this.preInitializedBean = bean;
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Note that a bean will not actually be registered unless it has a method
        // annotated by @subscribe.  This usage is described at http://code.google.com/p/guava-libraries/wiki/EventBusExplained
    	
    	//Tempted to move the registration in the beforeInit method, but it may change behavior that is not tested.
    	//Possibilly if the Event class is configured using a PropertyPlaceHolderConfigurer PostProcessor etc.
    	//So do this check in the afterInitialization callback instead.
    	if (bean instanceof Proxy) {
    		//The @Subscribe annotation doesn't make it across for proxied beans, so register the original
    		log.debug("Registering preInitialization bean : " + preInitializedBean.getClass().getName() + " with EventBus after initialization");
    		eventBus.register(preInitializedBean);
    	} else {
    		log.debug("Registering bean : " + bean.getClass().getName() + " with EventBus after initialization");
    		eventBus.register(bean);
    	}
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
