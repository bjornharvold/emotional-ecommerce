/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.data.web.controller.administration;

//~--- non-JDK imports --------------------------------------------------------

import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.StaticContentService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.StaticContentValidator;
import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.staticcontent.StaticContentEntry;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: UserController</p>
 * <p>Description: User homepage.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("staticContentController")
@SessionAttributes(types = StaticContent.class)
public class StaticContentController {

    private final StaticContentService staticContentService;
    private final StaticContentValidator staticContentValidator;
    private final MessageSource messageSource;
    private static final String MAX_RESULTS = "12";
    
    @Value("${preview.url:http://www.lela.com}")
    private String previewUrl;

    @Autowired
    public StaticContentController(StaticContentService staticContentService,
                                   StaticContentValidator staticContentValidator, MessageSource messageSource) {
        this.staticContentService = staticContentService;
        this.staticContentValidator = staticContentValidator;
        this.messageSource = messageSource;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/administration/static/content/list", method = RequestMethod.GET)
    public String showStaticContents(@RequestParam(required = false, defaultValue = "0") Integer page,
                                     @RequestParam(required = false, defaultValue = MAX_RESULTS) Integer size,
                                     Model model) throws Exception {
        model.addAttribute(WebConstants.STATIC_CONTENTS, staticContentService.findStaticContents(page, size));
        model.addAttribute(WebConstants.PREVIEW_URL, previewUrl);
        return "static.content.list";
    }

    @RequestMapping(value = "/administration/static/content", method = RequestMethod.GET)
    public String showStaticContent(@RequestParam(value = "urlName", required = false) String urlName,
                                     Model model) throws Exception {
        if (StringUtils.isNotBlank(urlName)) {
            StaticContent sc = staticContentService.findStaticContentByUrlName(urlName);

            if (sc != null) {
                model.addAttribute(WebConstants.STATIC_CONTENT, new StaticContentEntry(sc));
                model.addAttribute(WebConstants.IMAGE_URL, sc.getLmgrl());
            } else {
                sc = new StaticContent();
                sc.setRlnm(urlName);
                model.addAttribute(WebConstants.STATIC_CONTENT,  new StaticContentEntry(sc));
            }
        } else {
            model.addAttribute(WebConstants.STATIC_CONTENT, new StaticContentEntry(new StaticContent()));
        }

        return "static.content.form";
    }

    @RequestMapping(value = "/administration/static/content", method = RequestMethod.POST)
    public String saveStaticContent(@Valid StaticContentEntry staticContentEntry, BindingResult errors,
                                     Model model, RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view;

        staticContentValidator.validate(staticContentEntry, errors);

        if (errors.hasErrors()) {
                model.addAttribute(WebConstants.STATIC_CONTENT, staticContentEntry);
            view = "static.content.form";
        } else {
            StaticContentEntry entry = staticContentService.saveStaticContent(staticContentEntry);
            String message = messageSource.getMessage("static.content.saved.successfully", new String[]{entry.getStaticContent().getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
            view = "redirect:/administration/static/content?urlName=" + entry.getStaticContent().getRlnm();
        }
        

        return view;
    }

    @RequestMapping(value = "/administration/static/content", method = RequestMethod.DELETE)
    public String deleteStaticContent(@RequestParam(value = "urlName", required = false) String urlName) throws Exception {
        StaticContent staticContent = staticContentService.findStaticContentByUrlName(urlName);

        if (staticContent != null) {
            staticContentService.removeStaticContent(staticContent.getRlnm());
        }

        return "redirect:/administration/static/content/list";
    }
   

}
