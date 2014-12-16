package com.lela.commons.service;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/12/12
 * Time: 9:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailService {
    void sendEmailWithAttachment(String to, String text, File attachment, String fileName);
    void sendEmailWithAttachment(String to, String cc, String text, File attachment, String fileName);

    void sendEmail(String to, String subject, String text);

}
