package com.lela.commons.jobs;

import com.lela.commons.spring.ApplicationContextLoader;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: Chris Tallent
 * Date: 8/17/12
 * Time: 4:50 PM
 */
public class IngestJobSpringProcess {

    private static final String CLASSPATH_CONTEXT = "META-INF/spring/applicationContext.xml";

    public static void main(String[] args) {
        IngestJobSpringProcess process = new IngestJobSpringProcess();
        ApplicationContextLoader loader = new ApplicationContextLoader();
        loader.load(process, CLASSPATH_CONTEXT);
    }
}
