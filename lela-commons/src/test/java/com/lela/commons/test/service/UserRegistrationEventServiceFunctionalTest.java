package com.lela.commons.test.service;

import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.UserSupplementService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.service.impl.UserRegistrationEventServiceImpl;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.enums.RegistrationType;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mortbay.jetty.*;
import org.mortbay.jetty.servlet.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;


/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/10/12
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationEventServiceFunctionalTest {

    @Mock
    UserTrackerService userTrackerService;

    @Mock
    UserSupplementService userSupplementService;

    @Mock
    AffiliateService affiliateService;

    @InjectMocks
    UserRegistrationEventServiceImpl userRegistrationEventService;

    Server server = null;
    Context root = null;
    boolean complete;

    public void setup(HttpServlet servlet) throws Exception {
        server = new Server(8199);
        root = new Context(server, "/", Context.SESSIONS);
        root.setResourceBase(".");

        root.addServlet(new ServletHolder(servlet), "/service");
        server.start();
    }


    @Test
    public void registerUserWithSuccess() throws Exception {
        try {


            final StringBuffer result = new StringBuffer();
            complete = false;
            String userCode = "123-456";

            setup(new HttpServlet() {
                @Override
                protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    try {
                        char[] buff = new char[1024];
                        IOUtils.read(req.getReader(), buff);
                        String json = new String(buff);
                        result.append(json);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getOutputStream().close();
                    } finally {
                        complete();
                    }

                }

                protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
                {
                    System.out.println("no");
                }

            });
            User user = new User();
            Date createDate = new Date();
            user.setCdt(createDate);
            UserSupplement us = new UserSupplement();
            us.setFnm("FIRST");
            us.setLnm("LAST");
            AffiliateIdentifiers ai = new AffiliateIdentifiers();
            ai.setFfltccntrlnm("domain");
            AffiliateAccount affiliateAccount = new AffiliateAccount();
            affiliateAccount.setRgstrrl("http://localhost:8199/service");
            when(affiliateService.findAffiliateAccountByUrlName("domain")).thenReturn(affiliateAccount);
            when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(ai);

            RegistrationEvent event = new RegistrationEvent(user, us, RegistrationType.FACEBOOK);
            event.setRawPassword("password");
            userRegistrationEventService.registerUser(event);
            waitForComplete();

            assertEquals("Unexpected JSON result", "{\"firstName\":\"FIRST\",\"lastName\":\"LAST\",\"email\":null,\"createDate\":"+createDate.getTime()+",\"userCode\":\"" + user.getCd() + "\",\"rawPassword\":\"password\"}", result.toString().trim());
            verify(userSupplementService).trackAffiliateNotified(user.getCd(), false);
            verify(userSupplementService).trackAffiliateNotified(user.getCd(), true);
        } finally {
            server.stop();
        }
    }

    private void waitForComplete() throws InterruptedException {
        int tries = 0;
        while (!complete && tries < 10) {
            Thread.sleep(100);
            tries++;
        }
    }

    @Test
    public void registerUserWith404() throws Exception {
        try {
            final StringBuffer result = new StringBuffer();

            complete = false;
            String userCode = "123-456";

            setup(new HttpServlet() {
                @Override
                protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    try {
                        super.doPost(req, resp);

                        char[] buff = new char[1024];
                        IOUtils.read(req.getReader(), buff);
                        String json = new String(buff);
                        result.append(json);
                        resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                    } finally {
                        complete();
                    }
                }

            });
            User user = new User();
            Date createDate = new Date();
            user.setCdt(createDate);
            UserSupplement us = new UserSupplement();
            us.setFnm("FIRST");
            us.setLnm("LAST");
            AffiliateIdentifiers ai = new AffiliateIdentifiers();
            ai.setFfltccntrlnm("domain");
            AffiliateAccount affiliateAccount = new AffiliateAccount();
            affiliateAccount.setRgstrrl("http://localhost:8199/service");
            when(affiliateService.findAffiliateAccountByUrlName("domain")).thenReturn(affiliateAccount);
            when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(ai);

            RegistrationEvent event = new RegistrationEvent(user, us, RegistrationType.FACEBOOK);
            event.setRawPassword("password");
            userRegistrationEventService.registerUser(event);
            waitForComplete();
            assertEquals("Unexpected JSON result", "{\"firstName\":\"FIRST\",\"lastName\":\"LAST\",\"email\":null,\"createDate\":"+createDate.getTime()+",\"userCode\":\"" + user.getCd() + "\",\"rawPassword\":\"password\"}", result.toString().trim());
            verify(userSupplementService).trackAffiliateNotified(user.getCd(), false);
            verify(userSupplementService, never()).trackAffiliateNotified(user.getCd(), true);
        } finally {
            server.stop();
        }
    }

    @Test
    public void registerUserServiceDown() throws Exception {
        final StringBuffer result = new StringBuffer();
        complete = false;

        User user = new User();
        UserSupplement us = new UserSupplement(user.getCd());
        RegistrationEvent event = new RegistrationEvent(user, us, RegistrationType.FACEBOOK);
        userRegistrationEventService.registerUser(event);
        verify(userSupplementService).trackAffiliateNotified(user.getCd(), false);
    }

    private void complete() {
        this.complete = true;
    }
}
