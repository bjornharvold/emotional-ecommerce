package com.lela.commons.test.utilities;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.LoginEvent;
import com.lela.commons.event.PageViewEvent;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.enums.AuthenticationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/8/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class UserSessionTrackingHelperUnitTest {

    @Mock
    private UserTrackerService userTrackerService;

    @InjectMocks
    private UserSessionTrackingHelper userSessionTrackingHelper;

    @Test
    public void testProcessPostLogin()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(WebConstants.SESSION_USER_CODE, "user-code");
        request.setSession(session);
        User persistedUser = new User();

        when(userTrackerService.trackLogin(persistedUser, "user-code", AuthenticationType.USERNAME_PASSWORD, request)).thenReturn("user-code");

        ArgumentCaptor < Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        userSessionTrackingHelper.processPostLogin(request, persistedUser, AuthenticationType.USERNAME_PASSWORD);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("LoginEvent not posted", trackEvent.getAllValues().get(0) instanceof LoginEvent);
    }
}
