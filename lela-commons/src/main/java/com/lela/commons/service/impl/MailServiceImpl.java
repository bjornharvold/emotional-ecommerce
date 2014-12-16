/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */



package com.lela.commons.service.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.mail.MailServiceException;
import com.lela.commons.mail.MyMimeMessagePreparator;
import com.lela.commons.service.MailService;
import com.lela.domain.document.Alert;
import com.lela.domain.document.Item;
import com.lela.domain.enums.MailParameter;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * User: Bjorn Harvold Date: Apr 23, 2007 Time: 5:04:33 PM
 */
@Service("mailService")
public class MailServiceImpl implements MailService {

    /**
     * Field description
     */
    private final static String API_CONFIRMATION_EMAIL = "apiRegistrationConfirmation";
    private final static String CONFIRMATION_EMAIL = "registrationConfirmation";
    private final static String SHARE_WITH_FRIENDS_EMAIL = "shareWithFriends";
    private final static String RELATIONSHIP_REQUEST_EMAIL = "relationshipRequest";
    private final static String RELATIONSHIP_CONFIRMATION_EMAIL = "relationshipConfirmation";
    private final static String SHARE_WITH_FRIENDS_REGISTRATION_CONFIRMATION_EMAIL = "shareWithFriendsRegistrationConfirmation";
    private static final String ALL_INVITATIONS_USED_CONFIRMATION_EMAIL = "allInvitationsUsedConfirmation";
    private static final String EVENT_CONFIRMATION_EMAIL = "eventConfirmation";
    private static final String COUPON_CONFIRMATION_EMAIL = "couponConfirmation";
    private static final String COUPON_REQUEST_EMAIL = "couponRequest";
    private static final String COUPON_TRANSACTION_EMAIL = "couponTransaction";
    private static final String PRICE_ALERT_EMAIL = "priceAlert";

    /** Field description */
    private static final String EIGHTY = "80";

    /** Field description */
    private static final String EMAIL_USER_PASSWORD_RESET_SUBJECT = "email.user.password.reset.subject";

    /** Field description */
    private static final String EMAIL_USER_REGISTRATION_CONFIRMATION_SUBJECT =
        "email.user.registration.confirmation.subject";

    /** Field description */
    private static final String EMAIL_SHARE_WITH_FRIENDS_SUBJECT =
        "email.share.with.friends.subject";

    /** Field description */
    private static final String EMAIL_USER_RELATIONSHIP_REQUEST_SUBJECT =
        "email.user.relationship.request.subject";

    /** Field description */
    private static final String EMAIL_USER_RELATIONSHIP_CONFIRMATION_SUBJECT =
        "email.user.relationship.confirmation.subject";

    /** Field description */
    private static final String EMAIL_USER_COUPON_CONFIRMATION_SUBJECT =
        "email.user.coupon.confirmation.subject";

    /** Field description */
    private static final String EMAIL_USER_COUPON_REQUEST_SUBJECT =
        "email.user.coupon.request.subject";

    /** Field description */
    private static final String EMAIL_USER_COUPON_TRANSACTION_SUBJECT =
        "email.user.coupon.transaction.subject";

    private static final String ALL_INVITATIONS_USED_SUBJECT = "email.user.all.invitations.used.confirmation.subject";

    private static final String EVENT_CONFIRMATION_SUBJECT = "email.event.confirmation.subject";

    private static final String PRICE_ALERT_SUBJECT = "email.price.alert.subject";

    /** Field description */
    private final static String RESET_PASSWORD_EMAIL = "resetPassword";

    /**
     * Field description
     */
    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    //~--- fields -------------------------------------------------------------

    /** Field description */
    @Value("${email.enabled:true}")
    public Boolean enabled;

    /** Field description */
    @Value("${email.from}")
    public String from;

    /** Field description */
    @Value("${application.context}")
    private String context;

    /** Field description */
    @Value("${server.port}")
    private Integer port;

    /** Field description */
    @Value("${server.protocol}")
    private String protocol;

    /** Field description */
    @Value("${server.name}")
    private String server;

    /** Field description */
    @Value("${email.local.coupon}")
    public String localCouponEmail;

    /**
     * Field description
     */
    private final JavaMailSender mailSender;

    /**
     * Field description
     */
    private final MessageSource messageSource;

    /**
     * Field description
     */
    private final MyMimeMessagePreparator templateMimeMessagePreparator;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param mailSender            mailSender
     * @param templateMimeMessagePreparator templateMimeMessagePreparator
     * @param messageSource         messageSource
     */
    @Autowired
    public MailServiceImpl(JavaMailSender mailSender,
                           MyMimeMessagePreparator templateMimeMessagePreparator,
                           MessageSource messageSource) {
        this.mailSender                     = mailSender;
        this.templateMimeMessagePreparator  = templateMimeMessagePreparator;
        this.messageSource                  = messageSource;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     * @return Return value
     */
    public Boolean isAvailable() {
        return enabled;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @param email       email
     * @param newPassword newPassword
     * @param locale           l
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendPasswordResetEmail(String email, String newPassword, Locale locale) throws MailServiceException {
        Map<MailParameter, Object> map = new HashMap<MailParameter, Object>();

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        map.put(MailParameter.TO, email);
        map.put(MailParameter.PASSWORD, newPassword);
        map.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_PASSWORD_RESET_SUBJECT, null, locale));

        sendMIMEEmail(RESET_PASSWORD_EMAIL + "_" + locale.getLanguage() + ".xml", map, null, null, null);
    }

    @Override
    public void sendShareWithFriends(String email, Map<MailParameter, Object> map, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        map.put(MailParameter.TO, email);
        map.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_SHARE_WITH_FRIENDS_SUBJECT, null, locale));

        sendMIMEEmail(SHARE_WITH_FRIENDS_EMAIL + "_" + locale.getLanguage() + ".xml", map, null, null, null);
    }

    @Override
    public void sendShareWithFriendsRegistrationConfirmation(String email, Map<MailParameter, Object> map, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        map.put(MailParameter.TO, email);
        map.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_REGISTRATION_CONFIRMATION_SUBJECT, null, locale));

        sendMIMEEmail(SHARE_WITH_FRIENDS_REGISTRATION_CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", map, null, null, null);
    }

    /**
     * Method description
     *
     * @param email    email
     * @param parameters Optional mail parameters
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendRegistrationConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_REGISTRATION_CONFIRMATION_SUBJECT, null, locale));

        sendMIMEEmail(CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /**
     * Method description
     *
     * @param email    email
     * @param parameters Optional mail parameters
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendApiRegistrationConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_REGISTRATION_CONFIRMATION_SUBJECT, null, locale));

        sendMIMEEmail(API_CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /**
     * Method description
     *
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendRelationshipRequest(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_RELATIONSHIP_REQUEST_SUBJECT, null, locale));

        sendMIMEEmail(RELATIONSHIP_REQUEST_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /**
     * Method description
     *
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendRelationshipConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_RELATIONSHIP_CONFIRMATION_SUBJECT, null, locale));

        sendMIMEEmail(RELATIONSHIP_CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    @Override
    public void sendAllInvitationsUsedConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(ALL_INVITATIONS_USED_SUBJECT, null, locale));

        sendMIMEEmail(ALL_INVITATIONS_USED_CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    @Override
    public void sendEventSignUpConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        String[] eventName = new String[1];
        eventName[0] = (String) parameters.get(MailParameter.EVENT_NAME);

        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EVENT_CONFIRMATION_SUBJECT, eventName, locale));

        sendMIMEEmail(EVENT_CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /**
     * Method description
     *
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendCouponConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        parameters.put(MailParameter.SUBJECT, parameters.get(MailParameter.OFFER_VALUE_TERM));

        sendMIMEEmail(COUPON_CONFIRMATION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /**
     * Method description
     *
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendCouponRequest(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, localCouponEmail);
        String[] branchName = new String[1];
        branchName[0] = (String) parameters.get(MailParameter.BRANCH_NAME);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_COUPON_REQUEST_SUBJECT, branchName, locale));

        sendMIMEEmail(COUPON_REQUEST_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /**
     * Method description
     *
     * @throws MailServiceException MailServiceException
     */
    @Override
    public void sendCouponTransaction(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, localCouponEmail);
        String[] branchName = new String[1];
        branchName[0] = (String) parameters.get(MailParameter.BRANCH_NAME);
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(EMAIL_USER_COUPON_TRANSACTION_SUBJECT, branchName, locale));

        sendMIMEEmail(COUPON_TRANSACTION_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    @Override
    public void sendPriceAlert(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        parameters.put(MailParameter.TO, email);
        Object[] subjectArgs = new Object[3];
        subjectArgs[0] = ((Item) parameters.get(MailParameter.ITEM)).getNm();
        subjectArgs[1] = ((Item) parameters.get(MailParameter.ITEM)).getSubAttributes().get("LowestPrice");
        subjectArgs[2] = ((Alert) parameters.get(MailParameter.PRICE_ALERT)).getPrc();
        parameters.put(MailParameter.SUBJECT, messageSource.getMessage(PRICE_ALERT_SUBJECT, subjectArgs, locale));

        sendMIMEEmail(PRICE_ALERT_EMAIL + "_" + locale.getLanguage() + ".xml", parameters, null, null, null);
    }

    /*
     * setup the mail message in preparation for sending
     *
     * @param template template
     * @param params   params
     * @return mailMessage

    private SimpleMailMessage prepareMailMessage(String template, Map<String, Object> params) {

        // Create a mail message instance for this thread from the injected template
        SimpleMailMessage mailMessage = new SimpleMailMessage(templateMessage);

        mailMessage.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, params));
        mailMessage.setTo((String) params.get(MailParameter.TO.toString()));
        mailMessage.setSubject((String) params.get(MailParameter.SUBJECT.toString()));
        mailMessage.setFrom(from);
        mailMessage.setReplyTo(from);

        printMailMessage(mailMessage);

        return mailMessage;
    }
    */

    /*
    private void printMailMessage(SimpleMailMessage mailMessage) {
        if (log.isTraceEnabled()) {
            log.trace("Email content: \n" + mailMessage.toString());
        }
    }
    */

    /**
     * Sends a MIME email based on a velocity template and image assets with
     * (key: identifier, value: image name)
     *
     * @param template    template
     * @param mailParameters      mailParameters
     * @param imageAssets imageAssets
     * @param attachments attachments
     * @param headers     headers
     * @throws MailServiceException
     */
    @Async
    private void sendMIMEEmail(String template, Map<MailParameter, Object> mailParameters, Map<String, String> imageAssets,
                               Map<String, String> attachments, Map<String, String> headers)
            throws MailServiceException {

        Map<String, Object> params = convert(mailParameters);

        if (isAvailable()) {
            validateTemplate(template);

            try {
                // add base url to params
                params.put(MailParameter.BASE_URL.toString(), createBaseUrl());

                // Validate params
                validateMimeParams(params);

                MyMimeMessagePreparator mimeMessagePreparator = new MyMimeMessagePreparator(templateMimeMessagePreparator);

                mimeMessagePreparator.setFrom(from);
                mimeMessagePreparator.setReplyTo(from);
                mimeMessagePreparator.setTemplate(template);
                mimeMessagePreparator.setParams(params);
                mimeMessagePreparator.setImageAssets(imageAssets);
                mimeMessagePreparator.setAttachments(attachments);
                mimeMessagePreparator.setHeaders(headers);

                mailSender.send(mimeMessagePreparator);
            } catch (Exception ex) {
                log.error("Problem retrieving template for email: " + ex.getMessage(), ex);

                throw new MailServiceException("error.notification.velocity: " + ex.getMessage(), ex);
            }
        } else {
            log.warn("Email service is disabled!");
        }
    }

    /*
     * Creates a MailMessage for you that you can then send Required keys in the
     * map are: key: to key: subject (is a message bundle key that will be
     * translated)
     *
     * @param template template
     * @param mailParameters   params
     * @throws MailServiceException

    private void sendPlainEmail(String template, Map<MailParameter, Object> mailParameters) throws MailServiceException {

        Map<String, Object> params = convert(mailParameters);
        if (isAvailable()) {
            validateTemplate(template);

            // Validate message
            validateMailMessage(templateMessage, params);

            // retrieve template
            try {
                // add base url to params
                params.put(MailParameter.BASE_URL.toString(), createBaseUrl());

                SimpleMailMessage mailMessage = prepareMailMessage(template, params);

                mailSender.send(mailMessage);
            } catch (VelocityException ex) {
                log.error("Problem retrieving template for email: " + ex.getMessage(), ex);

                throw new MailServiceException("error.velocity: " + ex.getMessage(), ex);
            }
        } else {
            log.warn("Email service is disabled!");
        }
    }
    */

    /**
     * Method description
     *
     * @param template template
     * @throws MailServiceException MailServiceException
     */
    private void validateTemplate(String template) throws MailServiceException {

        if (StringUtils.isBlank(template)) {
            log.error("Missing template. Template cannot be null");

            throw new MailServiceException("error.missing.argument.exception",
                                           "Missing template. Template cannot be null");
        }
    }

    /*
     * Method description
     *
     * @param mailMessage   mailMessage
     * @param params   params
     * @throws MailServiceException MailServiceException

    private void validateMailMessage(SimpleMailMessage mailMessage,
                                     Map<String, Object> params) throws MailServiceException {

        if (params == null) {
            log.error("Missing params. Minimum fields needed are \"to\" and \"subject\"");

            throw new MailServiceException("error.missing.argument.exception",
                                           "Missing params. Minimum fields needed are \"to\" and \"subject\"");
        } else if ((!params.containsKey(MailParameter.TO.toString()) && (mailMessage.getTo() == null))
                   || (!params.containsKey(MailParameter.SUBJECT.toString()) && StringUtils.isBlank(mailMessage.getSubject()))) {
            log.error("Missing values in map. Missing params. Minimum fields needed are \"to\" and \"subject\"");

            throw new MailServiceException(
                "error.missing.argument.exception",
                "Missing values in map. Missing params. Minimum fields needed are \"to\" and \"subject\"");
        }
    }
    */

    /**
     * Method description
     *
     * @param params   params
     * @throws MailServiceException MailServiceException
     */
    private void validateMimeParams(Map<String, Object> params) throws MailServiceException {

        if (params == null) {
            log.error("Missing params. Minimum fields needed are \"to\" and \"subject\"");

            throw new MailServiceException("error.missing.argument.exception",
                                           "Missing params. Minimum fields needed are \"to\" and \"subject\"");
        } else if ((!params.containsKey(MailParameter.TO.toString()))
                   || (!params.containsKey(MailParameter.SUBJECT.toString()))) {
            log.error("Missing values in map. Missing params. Minimum fields needed are \"to\" and \"subject\"");

            throw new MailServiceException(
                "error.missing.argument.exception",
                "Missing values in map. Missing params. Minimum fields needed are \"to\" and \"subject\"");
        }
    }

    private String createBaseUrl() {
        StringBuilder sb = new StringBuilder();

        sb.append(protocol).append("://");
        sb.append(server);

        if (port != 80) {
            sb.append(":").append(port);
        }

        if (StringUtils.isNotBlank(context)) {
            sb.append(context);
        }

        return sb.toString();
    }

    private Map<String, Object> convert(Map<MailParameter, Object> parameters) {
        Map<String, Object> converted = new HashMap<String, Object>();

        if (parameters != null) {
            for (MailParameter key : parameters.keySet()) {
                converted.put(key.toString(), parameters.get(key));
            }
        }

        return converted;
    }
}
