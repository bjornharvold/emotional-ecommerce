package com.lela.commons.service.impl;

import com.lela.commons.service.EmailService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/12/12
 * Time: 8:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmailServiceImpl implements EmailService {

    /**
     * Field description
     */
    private final JavaMailSender mailSender;

    private static final String from = "donotreply@lela.com";

    public EmailServiceImpl(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }
    @Override
    public void sendEmailWithAttachment(final String to, final String text, final File attachment, final String fileName)
    {
        sendEmailWithAttachment(to, null, text, attachment, fileName);
    }

    @Override
    public void sendEmailWithAttachment(final String to, final String cc, final String text, final File attachment, final String fileName)
    {
        mailSender.send(new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom(from);
                message.setTo(to);
                if(cc!=null)
                  message.setCc(cc);
                message.setSubject(fileName);
                message.setText(text, false);
                message.addAttachment(fileName, new FileSystemResource(attachment));
            }
        });
    }

    @Override
    public void sendEmail(final String to, final String subject, final String text)
    {
        mailSender.send(new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom(from);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text, false);
            }
        });
    }
}
