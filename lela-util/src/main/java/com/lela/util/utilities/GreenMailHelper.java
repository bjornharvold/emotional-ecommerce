package com.lela.util.utilities;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 12/20/11
 * Time: 8:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class GreenMailHelper {
    private static final Logger log = LoggerFactory.getLogger(GreenMailHelper.class);
    private static GreenMail greenMail = null;

    public static GreenMail getGreenMail() {
        boolean complete = false;
        while (!complete) {
            if (greenMail == null) {
                greenMail = new GreenMail(ServerSetupTest.SMTP);
                greenMail.start();
                complete = true;
            } else {
                try {
                    synchronized (GreenMail.class) {
                        GreenMail.class.wait(1000);
                    }
                } catch (InterruptedException e) {
                    log.info("GreenMailHelp getGreenMail wait exception:" + e.getMessage());
                }
            }
        }

        return greenMail;
    }

    public static void stopGreenMail() {
        if (greenMail != null) {
            try {
                synchronized (GreenMail.class) {
                    GreenMail.class.wait(1000);
                }
            } catch (InterruptedException e) {
                log.info("GreenMailHelp stopGreenMail wait exception:" + e.getMessage());
            }
            greenMail.stop();
            greenMail = null;
        }
    }

    public static List<MimeMessage> getReceivedMessages(String email) {
        List<MimeMessage> receivedMessagesForEmail = new ArrayList<MimeMessage>();

        MimeMessage[] mimeMessages = greenMail.getReceivedMessages();

        if (mimeMessages.length > 1) {
            Arrays.sort(mimeMessages, new Comparator<MimeMessage>() {
                @Override
                public int compare(MimeMessage o1, MimeMessage o2) {
                    int o1ReceivedDate = o1.getMessageNumber();
                    int o2ReceivedDate = o2.getMessageNumber();
                    if (o1ReceivedDate > o2ReceivedDate) {
                        return -1;
                    } else if (o1ReceivedDate < o2ReceivedDate) {
                        return 1;
                    }
                    return 0;
                }
            });
        }

        MimeMessage mimeMessage = null;
        Address address = null;
        for(int i = 0; i < mimeMessages.length; i++) {
            mimeMessage = mimeMessages[i];
            try {
                address = mimeMessage.getAllRecipients()[0];
                if (address.toString().equals(email)) {
                    receivedMessagesForEmail.add(mimeMessage);
                }
            } catch (MessagingException e) {
                log.info("GreenMailHelp getting received messages exception:" + e.getMessage());
            }
        }

        return receivedMessagesForEmail;
    }
}
