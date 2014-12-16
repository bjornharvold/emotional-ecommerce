/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.mail;

import com.lela.commons.service.VelocityService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * User: bjorn
 * Date: Nov 9, 2007
 * Time: 7:20:49 PM
 */
public class MyMimeMessagePreparator implements MimeMessagePreparator {
    private final static Logger log = LoggerFactory.getLogger(MyMimeMessagePreparator.class);
    private static final String UTF_8 = "UTF-8";
    private VelocityService velocityService;
    private String template;
    private Map<String, Object> params;
    private String replyTo;
    private String from;
    private String inlineAssetRoot;
    private String attachmentAssetRoot;
    private Map<String, String> imageAssets;
    private Map<String, String> attachments;
    private Map<String, String> standardHeaders;
    private Map<String, String> headers;

    /**
     * Standard Constructor
     */
    public MyMimeMessagePreparator() {

    }

    /**
     * Copy constructor used for created a thread instance from a template
     *
     * @param original
     */
    public MyMimeMessagePreparator(MyMimeMessagePreparator original) {
        Assert.notNull(original, "The 'original' message argument cannot be null");

        this.velocityService = original.velocityService;
        this.template = original.template;
        this.replyTo = original.replyTo;
        this.from = original.from;
        this.inlineAssetRoot = original.inlineAssetRoot;
        this.attachmentAssetRoot = original.attachmentAssetRoot;

        this.params = copy(original.params);
        this.imageAssets = copy(original.imageAssets);
        this.attachments = copy(original.attachments);
        this.standardHeaders = copy(original.standardHeaders);
        this.headers = copy(original.headers);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setInlineAssetRoot(String inlineAssetRoot) {
        this.inlineAssetRoot = inlineAssetRoot;
    }

    public void setAttachmentAssetRoot(String attachmentAssetRoot) {
        this.attachmentAssetRoot = attachmentAssetRoot;
    }

    public void setImageAssets(Map<String, String> imageAssets) {
        this.imageAssets = imageAssets;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public void setStandardHeaders(Map<String, String> standardHeaders) {
        this.standardHeaders = standardHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public VelocityService getVelocityService() {
        return velocityService;
    }

    public void setVelocityService(VelocityService velocityService) {
        this.velocityService = velocityService;
    }

    public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8);

        // merge the template with the data
        String message = velocityService.mergeTemplateIntoString(template, params);

        if (log.isTraceEnabled()) {
            log.trace("Email content: \n" + message);
        }

        helper.setText(message, true);
        helper.setTo((String) params.get("to"));
        helper.setSubject((String) params.get("subject"));
        helper.setFrom(from);
        helper.setReplyTo(replyTo);

        // insert inline images
        if (imageAssets != null && imageAssets.size() > 0) {
            for (String asset : imageAssets.keySet()) {
                if (log.isTraceEnabled()) {
                    log.trace("Adding inline image asset to email: "
                            + asset + " " + imageAssets.get(asset));
                }

                Resource res = new ClassPathResource(inlineAssetRoot + "/" + imageAssets.get(asset));

                if (res.exists()) {
                    helper.addInline(asset, res);
                } else {
                    throw new Exception("Missing classpath resource: " + res.getURI());
                }
            }
        }

        // insert attachments
        if (attachments != null && attachments.size() > 0) {
            for (String attachment : attachments.keySet()) {
                if (log.isTraceEnabled()) {
                    log.trace("Adding attachment to email: " + attachment
                            + " " + attachments.get(attachment));
                }
                helper.addAttachment(attachment, new ClassPathResource(
                        attachmentAssetRoot + "/"
                                + attachments.get(attachment)));
            }
        }

        // Insert standard headers
        if (standardHeaders != null && standardHeaders.size() > 0) {
            for (String header : standardHeaders.keySet()) {
                if (log.isTraceEnabled()) {
                    log.trace("Adding header to email: " + header
                            + " " + standardHeaders.get(header));
                }
                mimeMessage.addHeader(header, standardHeaders.get(header));
            }
        }

        // Insert request specific headers
        if (headers != null && headers.size() > 0) {
            for (String header : headers.keySet()) {
                if (log.isTraceEnabled()) {
                    log.trace("Adding header to email: " + header
                            + " " + headers.get(header));
                }
                mimeMessage.addHeader(header, headers.get(header));
            }
        }

        /*
        if (log.isTraceEnabled()) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // print out the message to the console
                mimeMessage.writeTo(bos);
                log.debug(bos.toString());
                bos.flush();
                bos.close();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        */
    }

    /**
     * Warning, this produces a shallow copy
     *
     * @param original Map to copy
     * @return copied map
     */
    private <K, V> Map<K, V> copy(Map<K, V> original) {
        Map<K, V> copy = null;

        if (original != null) {
            copy = new HashMap<K, V>();
            for (Map.Entry<K, V> entry : original.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }
        }

        return copy;
    }
}
