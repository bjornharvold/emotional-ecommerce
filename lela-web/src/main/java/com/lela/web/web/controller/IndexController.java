/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.BlogService;
import com.lela.commons.service.PressService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Press;
import com.lela.domain.document.StaticContent;
import com.lela.domain.document.Testimonial;
import com.lela.domain.dto.blog.BlogsQuery;
import com.lela.domain.enums.SortBlogBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: IndexController</p>
 * <p>Description: Entry point into the app.</p>
 *
 * @author Bjorn Harvold
 */
@Controller
public class IndexController extends AbstractController {

    private static final String MAIN_PRESS_URL_NAME = "homepress";
    private static final String PRESS_AND_BUZZ_URL_NAME = "pressandbuzz";
    private final StaticContentService staticContentService;
    private final PressService pressService;
    private final BlogService blogService;

    @Autowired
    public IndexController(StaticContentService staticContentService, PressService pressService, BlogService blogService, BlogService service, BlogService blogService1) {
        this.staticContentService = staticContentService;
        this.pressService = pressService;
        this.blogService = blogService;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() throws Exception {
        return "403";
    }

    /**
     * Shows the home page
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndex(Model model, Device device, HttpServletRequest request, HttpSession session) throws Exception {

        // Check the request for a Domain Affiliate that has a branded subdomain
        AffiliateAccount domainAffiliate = (AffiliateAccount)request.getAttribute(WebConstants.DOMAIN_AFFILIATE);
        if (domainAffiliate != null && StringUtils.hasText(domainAffiliate.getDmnrdrct())) {
            return "redirect:" + domainAffiliate.getDmnrdrct();
        } else {
            // Handle the Lela.com home page
            Press press = pressService.findPressByUrlName(MAIN_PRESS_URL_NAME);

            /** Load up a random list of testimonials */
            if (press != null && press.getTstmnls() != null && !press.getTstmnls().isEmpty()) {
                List<Testimonial> list = press.getTstmnls();

                Collections.shuffle(list);

                if (list.size() >= 3) {
                    model.addAttribute(WebConstants.TESTIMONIALS, list.subList(0, 3));
                } else {
                    model.addAttribute(WebConstants.TESTIMONIALS, list.subList(0, list.size()));
                }
            }

            return returnMobileViewIfNecessary(model, device, session, "index");
        }
    }

    /**
     * Shows the categories landing page
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String showCategories(Model model, Device device, HttpSession session) throws Exception {
        return returnMobileViewIfNecessary(model, device, session, "categories");
    }

    /**
     *
     * @param view view
     * @return view tile definition
     * @throws Exception Exception
     */
    @RequestMapping(value = "/page/{view}", method = RequestMethod.GET)
    public String dispatchStatic(@PathVariable("view") String view) throws Exception {
        return "static.page";
    }

    /**
     *
     * @param urlName urlName
     * @param model model
     * @return view tile definition
     * @throws Exception Exception
     */
    @RequestMapping(value = "/sc/{urlName}", method = RequestMethod.GET)
    public String dispatchStatic(@PathVariable("urlName") String urlName, Model model) throws Exception {
        String view = "static.content";

        StaticContent sc = staticContentService.findStaticContentByUrlName(urlName);

        if (sc == null) {
            view = "resourceNotFound";
        } else {
            model.addAttribute(WebConstants.STATIC_CONTENT, sc);
        }

        return view;
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public String showJobs(Model model) throws Exception {
        String view = "static.content";

        StaticContent sc = staticContentService.findStaticContentByUrlName("jobs");

        if (sc == null) {
            view = "resourceNotFound";
        } else {
            model.addAttribute(WebConstants.STATIC_CONTENT, sc);
        }

        return view;
    }

    @RequestMapping(value = "/press", method = RequestMethod.GET)
    public String showPress(Model model) throws Exception {
        String view = "press";

        Press press = pressService.findPressByUrlName(PRESS_AND_BUZZ_URL_NAME);

        if (press == null) {
            view = "resourceNotFound";
        } else {
            model.addAttribute(WebConstants.BLOGS, blogService.findBlogs(new BlogsQuery(SortBlogBy.D, 0)));
            model.addAttribute(WebConstants.PRESS, press);
        }

        return view;
    }

    /**
     * Shows the facebook page iframe content
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/facebook/page", method = RequestMethod.GET)
    public String facebook() throws Exception {
        return "facebook.page";
    }

    /**
     * Comment back in for development only
     *
     * @param view view
     * @return view tile definition
     * @throws Exception Exception

    @RequestMapping(value = "/dispatcher", method = RequestMethod.GET)
    public String dispatch(@RequestParam("view") String view) throws Exception {
        return view;
    }
    */

    @RequestMapping(value = "/desirefullversiononmobile", method = RequestMethod.GET)
    public String desireFullVersionOnMobile(@RequestParam("url") String url,
                                            @RequestParam("full") Boolean full,
                                            HttpSession session) throws Exception {
        setUserDesiresFullSiteResolutionOnMobileDevice(session, full);
        return "redirect:" + url;
    }
}
