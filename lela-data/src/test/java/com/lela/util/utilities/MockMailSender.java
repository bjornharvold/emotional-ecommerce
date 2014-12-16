package com.lela.util.utilities;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/14/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockMailSender implements JavaMailSender {
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MimeMessage createMimeMessage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(MimeMessage[] mimeMessages) throws MailException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
