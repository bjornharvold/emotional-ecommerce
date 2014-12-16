package com.lela.commons.spring.security;

import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.domain.enums.AuthenticationType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Chris Tallent
 * Date: 7/31/12
 * Time: 10:43 AM
 */
public class LelaRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

    private final UserSessionTrackingHelper userSessionHelper;

    public LelaRememberMeAuthenticationFilter(AuthenticationManager authenticationManager,
                                              RememberMeServices rememberMeServices,
                                              UserSessionTrackingHelper userSessionTrackingHelper) {
        super(authenticationManager, rememberMeServices);
        this.userSessionHelper = userSessionTrackingHelper;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // Track the login
        Object principal = authentication.getPrincipal();
        if (principal instanceof Principal) {
            User user = ((Principal)principal).getUser();

            // Merge the transient and persistent user
            userSessionHelper.processPostLogin(request, user, AuthenticationType.REMEMBER_ME);
        }

        super.onSuccessfulAuthentication(request, response, authentication);
    }
}
