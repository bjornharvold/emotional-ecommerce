/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.service.VelocityService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.FactoryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/19/12
 * Time: 2:11 PM
 * Responsibility: Every interaction with Velocity should happen within this service
 */
@Service("velocityService")
public class VelocityServiceImpl implements VelocityService {
    private final static Logger log = LoggerFactory.getLogger(VelocityServiceImpl.class);

    // default encoding - create new method if you need to specify encoding manually
    private static final String UTF_8 = "UTF-8";

    private final VelocityEngine velocityEngine;

    @Autowired
    public VelocityServiceImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * Merge the specified Velocity template with the given model into a String.
     * <p>When using this method to prepare a text for a mail to be sent with Spring's
     * mail support, consider wrapping VelocityException in MailPreparationException.
     * @param templateLocation the location of template, relative to Velocity's
     * resource loader path
     * @param model the Map that contains model names as keys and model objects
     * as values
     * @return the result as String
     * @throws VelocityException if the template wasn't found or rendering failed
     * @see org.springframework.mail.MailPreparationException
     */
    @Override
    public String mergeTemplateIntoString(String templateLocation, Map model) {

        // merge the template with the data - formatting configurations provided by Velocity's tools.xml
        StringWriter writer = new StringWriter();
        mergeTemplate(templateLocation, UTF_8, model, writer);

        return writer.toString();
    }

    private static Context createToolboxContext() {
        ToolManager toolManager = new ToolManager();
        FactoryConfiguration fc = new FactoryConfiguration();
        toolManager.configure(fc);
        return toolManager.createContext();
    }

    /**
     * Merge the specified Velocity template with the given model and write
     * the result to the given Writer.
     * @param templateLocation the location of template, relative to Velocity's
     * resource loader path
     * @param encoding the encoding of the template file
     * @param model the Map that contains model names as keys and model objects
     * as values
     * @param writer the Writer to write the result to
     * @throws VelocityException if the template wasn't found or rendering failed
     */
    private void mergeTemplate(String templateLocation, String encoding, Map model, Writer writer) throws VelocityException {

        try {
            VelocityContext velocityContext = new VelocityContext(model, createToolboxContext());
            velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
        }
        catch (VelocityException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            log.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
            throw new VelocityException(ex.toString());
        }
    }
}
