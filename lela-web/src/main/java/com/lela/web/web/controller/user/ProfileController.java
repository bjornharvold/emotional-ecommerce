/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller.user;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.MetricService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.utilities.LocaleHelper;
import com.lela.commons.web.utils.AjaxUtils;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Social;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Profile;
import com.lela.domain.dto.user.DisableUser;
import com.lela.domain.dto.user.ProfilePictureUpload;
import com.lela.domain.enums.MetricType;
import com.lela.web.web.controller.AbstractController;
import com.lela.web.web.validator.ProfileImageValidator;
import com.lela.web.web.validator.ProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: ProfileController</p>
 * <p>Description: User profile.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("profileController")
@SessionAttributes(types = Profile.class)
public class ProfileController extends AbstractController {
    private final static Logger log = LoggerFactory.getLogger(ProfileController.class);
    private static final String USER_PROFILE_UPDATED = "user.profile.updated";
    private final ProfileService profileService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final MetricService metricService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService,
                             MessageSource messageSource,
                             MetricService metricService) {
        this.profileService = profileService;
        this.userService = userService;
        this.messageSource = messageSource;
        this.metricService = metricService;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/user/profile", method = RequestMethod.GET)
    public String showUser(HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            UserSupplement us = userService.findUserSupplement(user.getCd());
            model.addAttribute(WebConstants.PROFILE, new Profile(us));

            model.addAttribute(WebConstants.COUNTRIES, new LocaleHelper().getUniqueCountries());
            model.addAttribute(WebConstants.LANGUAGES, new LocaleHelper().getUniqueLanguages());
            model.addAttribute(WebConstants.INDUSTRIES, metricService.findMetricByType(MetricType.INDUSTRY));
            model.addAttribute(WebConstants.ANNUAL_HOUSEHOLD_INCOME, metricService.findMetricByType(MetricType.ANNUAL_HOUSEHOLD_INCOME));
            model.addAttribute(WebConstants.COMPANY_SIZE, metricService.findMetricByType(MetricType.COMPANY_SIZE));
            model.addAttribute(WebConstants.JOB_TITLE, metricService.findMetricByType(MetricType.JOB_TITLE));

            Calendar calender = new GregorianCalendar();
            int currentYear = calender.get(Calendar.YEAR);
            List<Integer> years = new ArrayList<Integer>();
            for (int i = 0; i < 100; i++) {
                years.add(currentYear - i);
            }
            model.addAttribute(WebConstants.YEARS, years);

            List<Social> socialList = us.getScls();
            boolean facebookConnected = false;

            if (socialList != null && !socialList.isEmpty()) {
                for (Social social : socialList) {
                    if (social.getProviderId().equals(ApplicationConstants.FACEBOOK)) {
                        facebookConnected = true;
                        break;
                    }
                }
            }
            model.addAttribute(WebConstants.FACEBOOK_CONNECTED, facebookConnected);

            model.addAttribute(WebConstants.ACCOUNT, "account-info");
        }

        return "user.profile";
    }

    /**
     * Deactivate a user form
     *
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/user/deactivate", method = RequestMethod.GET)
    public String showDisableForm() throws Exception {
        return "user.deactivate";
    }

    /**
     * Deactivates a user
     *
     * @param du      du
     * @param session session
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/user/deactivate", method = RequestMethod.POST)
    public String disableUser(DisableUser du, HttpSession session) throws Exception {
        profileService.removeLoggedInUserProfile(du);

        return "redirect:/logout";
    }

    @RequestMapping(value = "/user/profile", method = RequestMethod.PUT)
    public String updateUserProfile(@Valid Profile profile, BindingResult result,
                                    WebRequest request, RedirectAttributes redirectAttributes,
                                    HttpSession session, Model model, Locale locale) throws Exception {
        if (result.hasErrors()) {
            model.addAttribute(WebConstants.AJAX_REQUEST, AjaxUtils.isAjaxRequest(request));
            model.addAttribute(WebConstants.PROFILE, profile);
            return null;
        }

        new ProfileValidator().validate(profile, result);

        if (result.hasErrors()) {
            model.addAttribute(WebConstants.AJAX_REQUEST, AjaxUtils.isAjaxRequest(request));
            model.addAttribute(WebConstants.PROFILE, profile);
            return null;
        }

        User user = retrieveUserFromPrincipalOrSession(session);

        userService.updateProfile(user.getCd(), profile);

        String message = messageSource.getMessage(USER_PROFILE_UPDATED, null, locale);

        // success response handling
        if (AjaxUtils.isAjaxRequest(request)) {
            // prepare map for rendering success message in this request
            model.addAttribute(WebConstants.MESSAGE, message);
            model.addAttribute(WebConstants.AJAX_REQUEST, true);
            return null;
        } else {
            // store a success message for rendering on the next request after redirect
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

            // redirect back to the form to render the success message along with newly bound values
            return "redirect:/user/profile";
        }
    }

    /**
     * Displays the user profile upload page
     * @return Tile def
     */
    @RequestMapping(value = "/user/profile/picture", method = RequestMethod.GET)
    public String showProfilePicture() {
        return "user.profile.picture";
    }

    /**
     * Uploads a picture to the users profile
     * @param picture picture
     * @param result result
     * @param redirectAttributes redirectAttributes
     * @param session session
     * @param model model
     * @param locale locale
     * @return Redirect
     * @throws Exception
     */
    @RequestMapping(value = "/user/profile/picture", method = RequestMethod.POST)
    public String updateProfilePicture(@Valid ProfilePictureUpload picture,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session, Model model, Locale locale) throws Exception {

        new ProfileImageValidator().validate(picture, result);

        if (result.hasErrors()) {
            model.addAttribute(WebConstants.PROFILE, picture);
            return "user.profile.picture";
        }

        User user = retrieveUserFromPrincipalOrSession(session);

        userService.updateProfilePicture(user.getCd(), picture);

        String message = messageSource.getMessage(USER_PROFILE_UPDATED, null, locale);

        // store a success message for rendering on the next request after redirect
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        // redirect back to the form to render the success message along with newly bound values
        return "redirect:/user/profile/picture";
    }

}
