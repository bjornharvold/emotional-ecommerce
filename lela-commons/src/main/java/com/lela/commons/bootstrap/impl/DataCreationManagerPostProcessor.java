package com.lela.commons.bootstrap.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DataCreationManagerPostProcessor implements BeanPostProcessor {

	private final static Logger LOG = LoggerFactory.getLogger(DataCreationManagerPostProcessor.class);
	private DataCreationManagerEnabler dataCreationManagerEnabler;
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof OnStartupBootStrapperService) {
			((OnStartupBootStrapperService)bean).setEnabled(dataCreationManagerEnabler.isEnabled());
			LOG.debug("Set OnStartupBootStrapperService enable status to " + dataCreationManagerEnabler.isEnabled());
		}
		return bean;
	}

	public DataCreationManagerEnabler getDataCreationManagerEnabler() {
		return dataCreationManagerEnabler;
	}

	public void setDataCreationManagerEnabler(DataCreationManagerEnabler dataCreationManagerEnabler) {
		this.dataCreationManagerEnabler = dataCreationManagerEnabler;
	}

}
