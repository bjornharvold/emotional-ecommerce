package com.lela.commons.service.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.UserRegistrationEventService;
import com.lela.commons.service.UserSupplementService;
import com.lela.commons.service.UserTrackerService;
import com.lela.domain.document.*;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.enums.MixpanelEngagementPropertyType;
import com.lela.domain.enums.MixpanelEventType;
import com.lela.domain.enums.MixpanelPeoplePropertyType;
import com.mixpanel.java.mpmetrics.MPMetrics;
import com.sun.jersey.json.impl.reader.Jackson2StaxReader;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/10/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserRegistrationEventServiceImpl implements UserRegistrationEventService{
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationEventServiceImpl.class);

    @Autowired
    private UserSupplementService userSupplementService;

    @Autowired
    private UserTrackerService userTrackerService;

    @Autowired
    private AffiliateService affiliateService;

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void registerUser(RegistrationEvent event) {
        log.info("Registering user:", event);
        //track that the information has not been sent
        userSupplementService.trackAffiliateNotified(event.getUser().getCd(), Boolean.FALSE);

        //find the users affiliation and the registration url
        AffiliateIdentifiers ai = userTrackerService.findAffiliateIdentifiers(event.getUser().getCd());

        //if the user has an affiliation
        if(ai != null && StringUtils.isNotBlank(ai.getFfltccntrlnm()))
        {
            AffiliateAccount affiliateAccount = affiliateService.findAffiliateAccountByUrlName(ai.getFfltccntrlnm());

            //does the affiliate have a callback url?
            if(affiliateAccount!=null && StringUtils.isNotBlank(affiliateAccount.getRgstrrl()))
            {
                String url = affiliateAccount.getRgstrrl();

                RegistrationDto registrationDto = buildRegistrationDto(event);

                try{
                    String json = buildJson(registrationDto);
                    log.debug("Sending user to " + event.getUser().getCd(), json);
                    int status = Request.Post(url).bodyString(json, ContentType.APPLICATION_JSON).execute().returnResponse().getStatusLine().getStatusCode();
                    if(status == HttpServletResponse.SC_OK){
                        userSupplementService.trackAffiliateNotified(event.getUser().getCd(), Boolean.TRUE);
                    }
                }
                catch(IOException ioe)
                {
                    log.error("Unable to send user information to affiliate for user:"+event.getUser().getCd(), ioe);
                }
            }
        }
    }

    private RegistrationDto buildRegistrationDto(RegistrationEvent event) {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail(event.getUser().getMl());
        registrationDto.setFirstName(event.getUserSupplement().getFnm());
        registrationDto.setLastName(event.getUserSupplement().getLnm());
        registrationDto.setUserCode(event.getUser().getCd());
        registrationDto.setCreateDate(event.getUser().getCdt());
        registrationDto.setRawPassword(event.getRawPassword());
        return registrationDto;
    }

    private String buildJson(RegistrationDto registrationDto) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, registrationDto);
        return writer.getBuffer().toString();
    }

    private class RegistrationDto
    {
        private String firstName;
        private String lastName;
        private String email;
        private Date createDate;
        private String userCode;
        private String rawPassword;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getRawPassword() {
            return rawPassword;
        }

        public void setRawPassword(String rawPassword) {
            this.rawPassword = rawPassword;
        }
    }
}
