/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.api;

import com.lela.commons.service.CampaignService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.dto.ApiValidationResponse;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.RegisterUserRequest;
import com.lela.domain.dto.user.RegisterUserResponse;
import com.lela.domain.enums.AddressType;
import com.lela.domain.enums.AuthenticationType;
import com.lela.util.utilities.jackson.CustomObjectMapper;
import com.lela.web.web.controller.AbstractApiController;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 11/5/11
 * Time: 3:16 AM
 * Responsibility:
 */
@Controller
public class ApiUserController extends AbstractApiController {
    private final static Logger log = LoggerFactory.getLogger(ApiUserController.class);

    private final static ObjectMapper mapper = new CustomObjectMapper();

    private final Validator validator;
    private final SignInAdapter signInAdapter;
    private final ProfileService profileService;

    @Autowired
    public ApiUserController(AffiliateService affiliateService,
                             UserService userService,
                             ApplicationService applicationService,
                             CampaignService campaignService,
                             UserTrackerService userTrackerService,
                             UserSessionTrackingHelper userSessionTrackingHelper,
                             ProfileService profileService,
                             Validator validator,
                             SignInAdapter signInAdapter) {
        super(userService, affiliateService, applicationService, campaignService, userTrackerService, userSessionTrackingHelper);
        this.validator = validator;
        this.signInAdapter = signInAdapter;
        this.profileService = profileService;
    }

    /**
     * Validates and creates a new user externally from the website.
     *
     * @param request request
     * @param response  response
     * @return RegisterUserResponse
     * @throws Exception
     */
    @RequestMapping(value = "/api/user",
                    method = RequestMethod.POST,
                    headers = "content-type=text/plain")
    public void registerApiUserIECors(InputStream input,
                                                      NativeWebRequest webRequest,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      HttpSession session,
                                                      Locale locale) throws Exception {


        String content = IOUtils.toString(input, "UTF-8");
        RegisterUserRequest dto = mapper.readValue(content, RegisterUserRequest.class);

        RegisterUserResponse result = registerApiUser(dto, webRequest, request, response, session, locale);

        response.setContentType("text/plain");
        ServletOutputStream out = response.getOutputStream();

        mapper.writeValue(out, result);
        response.flushBuffer();
    }

    /**
    * Validates and creates a new user externally from the website.
    *
    * @param dto       user
    * @param request request
    * @param response  response
    * @return RegisterUserResponse
    * @throws Exception
    */
    @RequestMapping(value = "/api/user", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public RegisterUserResponse registerApiUser(@RequestBody RegisterUserRequest dto,
                                                NativeWebRequest webRequest,
                                                HttpServletRequest request,
                                                HttpServletResponse response,
                                                HttpSession session,
                                                Locale locale) throws Exception {

        // Add the locale
        dto.setLocale(locale);

        RegisterUserResponse result = new RegisterUserResponse(dto.getEmail());

        // validate on annotations
        Set<ConstraintViolation<RegisterUserRequest>> failures = validator.validate(dto);

        if (!failures.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setMessage(ApplicationConstants.APPLICATION_MISSING_DATA);
        } else {
            // validate that the caller has the right to register
            ApiValidationResponse validationResponse = validateRequest(dto.getAffiliateId(), dto.getApplicationId());

            if (validationResponse.getValid()) {
                // validate that user doesn't already exist
                User user = userService.findUserByEmail(dto.getEmail());

                if (user != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    result.setUserAlreadyExists(true);
                    result.setMessage(ApplicationConstants.APPLICATION_EXISTING_USER);
                } else {
                    // track analytics
                    handleAnalyticsCapture(request, response, dto.getAffiliateId(), dto.getApplicationId(), null);

                    // Convert old style address attributes to an Address object if present
                    if (dto.getAttributes() != null &&
                            (dto.getAttributes().containsKey(ApplicationConstants.USER_ATTRIBUTE_ADDRESS) ||
                             dto.getAttributes().containsKey(ApplicationConstants.USER_ATTRIBUTE_CITY) ||
                             dto.getAttributes().containsKey(ApplicationConstants.USER_ATTRIBUTE_STATE) ||
                             dto.getAttributes().containsKey(ApplicationConstants.USER_ATTRIBUTE_ZIPCODE))) {
                        Address address = new Address(AddressType.HOME,
                                getAttr(dto, ApplicationConstants.USER_ATTRIBUTE_ADDRESS),
                                getAttr(dto, ApplicationConstants.USER_ATTRIBUTE_ADDRESS_2),
                                getAttr(dto, ApplicationConstants.USER_ATTRIBUTE_CITY),
                                getAttr(dto, ApplicationConstants.USER_ATTRIBUTE_STATE),
                                getAttr(dto, ApplicationConstants.USER_ATTRIBUTE_ZIPCODE)
                                );

                        if (dto.getAddresses() == null) {
                            dto.setAddresses(new ArrayList<Address>());
                        }

                        dto.getAddresses().add(address);
                    }

                    // Register the user
                    user = profileService.registerApiUser(
                            dto,
                            (String)session.getAttribute(WebConstants.SESSION_USER_CODE),
                            (AffiliateAccount)request.getAttribute(WebConstants.DOMAIN_AFFILIATE));

                    result.setSrcd(user.getCd());
                    result.setMessage(ApplicationConstants.APPLICATION_CREATED_USER);

                    // handle post process work such as tracking and affiliation
                    session.setAttribute(WebConstants.AFFILIATE_SESSION_KEY, dto.getAffiliateId());
                    processPostApiRegistration(request, response, user);
                    processPostLogin(request, user, AuthenticationType.API);
                }

                // Attempt to sign in... this may not operate in cross-domain embedded
                // conditions in browsers blocking third-party cookies like safari
                // This is done even for existing registrations so that in situations
                // like Eyeon where they register the user first before calling the quiz,
                // the user can still be logged in
                // TODO - Add some sort of one shot login token for the existing user use case
                signInAdapter.signIn(user.getIdString(), null, webRequest);

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.setMessage(ApplicationConstants.APPLICATION_NOT_AUTHENTICATED);
            }
        }

        return result;
    }

    private String getAttr(RegisterUserRequest us, String attribute) {
        List<String> values = us.getAttributes().get(attribute);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }
}
