/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller.user;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.MetricService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.AjaxUtils;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Favorite;
import com.lela.domain.dto.Principal;
import com.lela.domain.enums.MetricType;
import com.lela.web.web.controller.AbstractController;
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
import java.util.Locale;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: ProfileController</p>
 * <p>Description: User profile.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("favoritesController")
@SessionAttributes(types = Favorite.class)
public class FavoritesController extends AbstractController {
    private final static Logger log = LoggerFactory.getLogger(FavoritesController.class);
    private static final String USER_FAVORITES_UPDATED = "user.favorites.updated";
    private final UserService userService;
    private final MessageSource messageSource;
    private final MetricService metricService;

    @Autowired
    public FavoritesController(UserService userService, MessageSource messageSource,
                               MetricService metricService) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.metricService = metricService;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/user/favorites", method = RequestMethod.GET)
    public String showFavorites(HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());
        model.addAttribute(WebConstants.FAVORITE, new Favorite(us));

        retrieveReferenceData(model);

        return "user.favorites";
    }

    @RequestMapping(value = "/user/favorites", method = RequestMethod.PUT)
    public String updateFavorites(@Valid Favorite favorite, BindingResult result,
                                    WebRequest request, RedirectAttributes redirectAttributes,
                                    HttpSession session, Model model, Locale locale) throws Exception {
        Boolean isAjax = AjaxUtils.isAjaxRequest(request);

        if (result.hasErrors()) {
            model.addAttribute(WebConstants.AJAX_REQUEST, isAjax);
            model.addAttribute(WebConstants.FAVORITE, favorite);
            return null;
        }

        User user = retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());

        us.updateProfile(favorite);
        userService.saveUserSupplement(us);

        String message = messageSource.getMessage(USER_FAVORITES_UPDATED, null, locale);

        // success response handling
        if (isAjax) {
            // prepare map for rendering success message in this request
            model.addAttribute(WebConstants.MESSAGE, message);
            model.addAttribute(WebConstants.AJAX_REQUEST, true);
            return null;
        } else {
            // store a success message for rendering on the next request after redirect
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

            // redirect back to the form to render the success message along with newly bound values
            return "redirect:/user/favorites";
        }
    }

    private void retrieveReferenceData(Model model) {
        model.addAttribute(WebConstants.CAR_MAKER, metricService.findMetricByType(MetricType.CAR_MAKER));
        model.addAttribute(WebConstants.SHAMPOO, metricService.findMetricByType(MetricType.SHAMPOO));
        model.addAttribute(WebConstants.FASHION_BRAND, metricService.findMetricByType(MetricType.FASHION_BRAND));
        model.addAttribute(WebConstants.BEER, metricService.findMetricByType(MetricType.BEER));
        model.addAttribute(WebConstants.ONLINE_STORE, metricService.findMetricByType(MetricType.ONLINE_STORE));
        model.addAttribute(WebConstants.GROCERY_STORE, metricService.findMetricByType(MetricType.GROCERY_STORE));
    }
}
