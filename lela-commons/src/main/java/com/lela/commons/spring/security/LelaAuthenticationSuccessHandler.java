package com.lela.commons.spring.security;

import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.domain.enums.AuthenticationType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Chris Tallent
 * Date: 7/30/12
 * Time: 7:11 PM
 */
public class LelaAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserSessionTrackingHelper userSessionHelper;
    private final RememberMeServices rememberMeServices;

    public LelaAuthenticationSuccessHandler(UserSessionTrackingHelper userSessionTrackingHelper,
                                            RememberMeServices rememberMeServices) {
        this.userSessionHelper = userSessionTrackingHelper;
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Track the login
        Object principal = authentication.getPrincipal();
        if (principal instanceof Principal) {
            User user = ((Principal)principal).getUser();

            // Merge the transient and persistent user
            rememberMeServices.loginSuccess(request, response, authentication);
            userSessionHelper.processPostLogin(request, user, AuthenticationType.USERNAME_PASSWORD);
        }

        // Allow the Spring implementation to redirect to the configured URL
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /*
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        if (isAlwaysUseDefaultTargetUrl()) {
            return getDefaultTargetUrl();
        }

        // Check for the parameter and use that if available
        String targetUrl = null;

        if (getTargetUrlParameter() != null  ) {
            targetUrl = request.getParameter(getTargetUrlParameter());

            if (StringUtils.hasText(targetUrl)) {
                logger.debug("Found targetUrlParameter in request: " + targetUrl);

                return targetUrl;
            }
        }

        // Check the saved request
        targetUrl = extractOriginalUrl(request, response);

        if (!StringUtils.hasText(targetUrl)) {
            targetUrl = getDefaultTargetUrl();
            logger.debug("Using default Url: " + targetUrl);
        }

        return targetUrl;
    }

    private String extractOriginalUrl(HttpServletRequest request, HttpServletResponse response) {
        // else look to see if there is another saved request defined somewhere else
        SavedRequest saved = requestCache.getRequest(request, response);
        if (saved == null) {
            return null;
        }
        requestCache.removeRequest(request, response);
        return saved.getRedirectUrl();
    }
    */
}
