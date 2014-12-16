/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.utilities;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.LoginEvent;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.RegistrationType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * User: Chris Tallent
 * Date: 8/3/12
 * Time: 9:18 AM
 */
@Component("userSessionTrackingHelper")
public class UserSessionTrackingHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTrackerService userTrackerService;

    /**
     * This method will look up the user in various places in the app
     * 1. Logged in user
     * 2. Session user
     * 3. Create and return session user
     *
     * @param session session
     * @return User
     */
    public User retrieveUserFromPrincipalOrSession(HttpSession session) {
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        User transientUser = (User) session.getAttribute(WebConstants.USER);
        User user;

        if (principal != null) {
            user = principal.getUser();
        } else if (transientUser != null) {
            user = transientUser;
        } else {

            // !!! NOTE... THIS IS A FALLBACK... the transient user SHOULD be created by the UserSessionAndTrackingHandlerInterceptor
            // this is a non-registered user with only a session object to work with
            user = new User();

            // stick the user back in the session
            setSessionUserAndTrackVisit(null, session, user);
        }

        return user;
    }

    public void setSessionUserAndTrackVisit(HttpServletRequest request, User user) {
        setSessionUserAndTrackVisit(request, request.getSession(), user);
    }

    private void setSessionUserAndTrackVisit(HttpServletRequest request, HttpSession session, User user) {
        session.setAttribute(WebConstants.USER, user);

        // A new transient user was created, also create a new UserTracker object for analytics
        session.setAttribute(WebConstants.SESSION_USER_CODE, userTrackerService.trackUserVisitStart(user, request));
    }

    public User getSessionUser(HttpSession session) {
        return (User) session.getAttribute(WebConstants.USER);
    }

    public void deleteSessionUser(HttpSession session) {
        session.removeAttribute(WebConstants.USER);
    }

    public User processPostLogin(NativeWebRequest nativeRequest, User persistedUser, AuthenticationType authenticationType) {
        return processPostLogin(nativeRequest.getNativeRequest(HttpServletRequest.class), persistedUser, authenticationType);
    }

    /**
     * This method should be called on every login so we can sync the user session object
     * with that of the registered / persisted user
     *
     * @param persistedUser persistedUser
     * @return Persisted user
     */
    public User processPostLogin(HttpServletRequest request, User persistedUser, AuthenticationType authenticationType) {

        HttpSession session = request.getSession();
        User transientUser = getSessionUser(session);

        session.setAttribute(WebConstants.SESSION_USER_CODE,
                userTrackerService.trackLogin(
                        persistedUser,
                        (String) session.getAttribute(WebConstants.SESSION_USER_CODE),
                        authenticationType,
                        request));

        if (transientUser != null) {
            // merge data and persist
            mergeTransientWithPersistentUser(persistedUser, transientUser);

            // Delete the transient user
            deleteSessionUser(session);
        }

        // Login Event
        EventHelper.post(new LoginEvent(persistedUser, authenticationType));

        return persistedUser;
    }

    /**
     * This method should only be called upon registration
     *
     * @param request       request
     * @param persistedUser persistedUser
     * @return Persisted user
     */
    public User processPostRegistration(NativeWebRequest request, User persistedUser, RegistrationType registrationType) {
        return processPostRegistration(
                request.getNativeRequest(HttpServletRequest.class),
                request.getNativeResponse(HttpServletResponse.class),
                persistedUser, registrationType);
    }

    /**
     * This method should only be called upon registration
     *
     * @param request       request
     * @param persistedUser persistedUser
     * @return Persisted user
     */
    public User processPostRegistration(HttpServletRequest request, HttpServletResponse response, User persistedUser, RegistrationType registrationType) {
        // Log the analytic
        HttpSession session = request.getSession();
        userTrackerService.trackRegistrationComplete((String) session.getAttribute(WebConstants.SESSION_USER_CODE), registrationType);

        return persistedUser;
    }

    /**
     * This method should only be called by an API registration
     */
    public User processPostApiRegistration(HttpServletRequest request, HttpServletResponse response, User persistedUser) {
        HttpSession session = request.getSession();
        session.setAttribute(WebConstants.SESSION_USER_CODE, userTrackerService.trackApiRegistration(persistedUser, (String) session.getAttribute(WebConstants.SESSION_USER_CODE), request));

        return persistedUser;
    }

    /**
     * Look for situations where a user has completed the quiz BEFORE signing-up
     * using facebook thus bypassing the standard code in Registration Controller
     * that associates the transient quiz with the newly created user
     * TODO this could all go away if we use a cookie to track the user code
     * TODO the user supplement objects are being persisted for transient users as well [BH 20120812]
     *
     * @param actualUser    actualUser
     * @param transientUser transientUser
     */
    private void mergeTransientWithPersistentUser(User actualUser, User transientUser) {
        boolean dirty = false;

        if (actualUser != null && transientUser != null
                && !StringUtils.equals(actualUser.getCd(), transientUser.getCd())) {
            UserSupplement actualUS = userService.findUserSupplement(actualUser.getCd());
            UserSupplement transientUS = userService.findUserSupplement(transientUser.getCd());

            if (transientUS != null && actualUS != null) {

                /* TODO all functionality is behind a registration wall and we don't have to do this kind of merging any more
                // quiz answers added to session user before registering
                if ((actualUS.getNswrs() == null || actualUS.getNswrs().isEmpty())
                        && (transientUS.getNswrs() != null && !transientUS.getNswrs().isEmpty())) {
                    actualUS.setNswrs(transientUS.getNswrs());
                    dirty = true;
                }

                // motivators added to session user before registering
                if ((actualUS.getMtvtrmp() == null || actualUS.getMtvtrmp().isEmpty())
                        && (transientUS.getMtvtrmp() != null && !transientUS.getMtvtrmp().isEmpty())) {
                    actualUS.setMtvtrmp(transientUS.getMtvtrmp());
                    dirty = true;
                }

                // always merge stores
                if (transientUS.getBrds() != null && !transientUS.getBrds().isEmpty()) {
                    actualUS.addListCardBoards(transientUS.getBrds());
                    dirty = true;
                }
                */

                // Handle any user attributes from the transient user if this is a new user registration
                if ((actualUS.getAttrs() == null || actualUS.getAttrs().isEmpty())
                        && (transientUS.getAttrs() != null && !transientUS.getAttrs().isEmpty())) {
                    actualUS.setAttrs(transientUS.getAttrs());
                    dirty = true;
                }

                if (dirty) {
                    userService.saveUserSupplement(actualUS);
                }
            }
        }
    }
}
