/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.press;

import com.lela.commons.service.PressService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.PressImageValidator;
import com.lela.data.web.validator.PressValidator;
import com.lela.domain.document.Press;
import com.lela.domain.document.Testimonial;
import com.lela.domain.document.Tweet;
import com.lela.domain.dto.press.PressImageEntry;
import com.lela.domain.enums.TestimonialType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 6/8/12
 * Time: 7:00 PM
 * Responsibility: Admin interface to the Press object
 */
@Controller
@SessionAttributes(types = {Press.class, PressImageEntry.class, Testimonial.class, Tweet.class})
public class PressManagerController {
    private final PressService pressService;
    private final MessageSource messageSource;
    private final PressValidator pressValidator;

    private static final Integer MAX_RESULTS = 12;

    @Autowired
    public PressManagerController(PressService pressService,
                                  MessageSource messageSource,
                                  PressValidator pressValidator) {
        this.pressService = pressService;
        this.messageSource = messageSource;
        this.pressValidator = pressValidator;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Displays a list of all press objects in the system
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/administration/press/list", method = RequestMethod.GET)
    public String showPresses(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              Model model) throws Exception {

        model.addAttribute(WebConstants.PRESSES, pressService.findPressPages(page, MAX_RESULTS));

        return "administration.press.list";
    }

    /**
     * Shows a form to either insert or update a press
     *
     * @param urlName urlName
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}", method = RequestMethod.GET)
    public String showPress(@PathVariable("urlName") String urlName, Model model) throws Exception {

        model.addAttribute(WebConstants.PRESS, pressService.findWholePressByUrlName(urlName));

        return "administration.press";
    }

    /**
     * Shows a form to either insert or update a press
     *
     * @param urlName urlName
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/form", method = RequestMethod.GET)
    public String showPressForm(@RequestParam(value = "urlName", required = false) String urlName, Model model) throws Exception {

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.PRESS, pressService.findWholePressByUrlName(urlName));
        } else {
            model.addAttribute(WebConstants.PRESS, new Press());
        }

        return "administration.press.form";
    }

    /**
     * Deletes a press object
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/press/{urlName}", method = RequestMethod.DELETE)
    public String deletePress(@PathVariable("urlName") String urlName,
                              RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        pressService.removePress(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/list";
    }

    /**
     * Submits a form to either insert or update a press object
     *
     * @param press              press
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @param locale             locale
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/form", method = RequestMethod.POST)
    public String submitPress(Press press, BindingResult errors,
                              RedirectAttributes redirectAttributes,
                              Model model, Locale locale) throws Exception {

        // validate on business rules
        pressValidator.validate(press, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.PRESS, press);
            return "administration.press";
        }

        press = pressService.savePress(press);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.saved.successfully", new String[]{press.getNm()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + press.getRlnm();
    }

    /**
     * Shows a form to either insert or update new press image
     *
     * @param urlName urlName
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}/image", method = RequestMethod.GET)
    public String showPressImageForm(@PathVariable("urlName") String urlName,
                                     @RequestParam(value = "id", required = false) String id,
                                     Model model) throws Exception {

        model.addAttribute(WebConstants.PRESS_IMAGE_ENTRY, pressService.findPressImageEntry(urlName, id));

        return "administration.press.image.form";
    }

    /**
     * Submits a form to either insert a press image object
     *
     * @param pressImageEntry    press
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @param locale             locale
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}/image", method = RequestMethod.POST)
    public String submitPressImage(@PathVariable("urlName") String urlName,
                                   @Valid PressImageEntry pressImageEntry, BindingResult errors,
                                   RedirectAttributes redirectAttributes,
                                   Model model, Locale locale) throws Exception {

        new PressImageValidator().validate(pressImageEntry, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.PRESS_IMAGE_ENTRY, pressImageEntry);
            return "administration.press.image.form";
        }

        pressService.savePressImage(pressImageEntry);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.image.saved.successfully", new String[]{pressImageEntry.getHdr()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + urlName;
    }


    /**
     * Deletes a press image object
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/press/{urlName}/image/{id}", method = RequestMethod.DELETE)
    public String deletePressImage(@PathVariable("urlName") String urlName,
                                   @PathVariable("id") String id,
                                   RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        pressService.removePressImage(urlName, id);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.image.deleted.successfully", null, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + urlName;
    }

    /**
     * Shows a form to either insert or update testimonial
     *
     * @param urlName urlName
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}/testimonial", method = RequestMethod.GET)
    public String showTestimonialForm(@PathVariable("urlName") String urlName,
                                      @RequestParam(value = "id", required = false) String id,
                                      Model model) throws Exception {

        model.addAttribute(WebConstants.TESTIMONIAL, pressService.findTestimonial(urlName, id));
        model.addAttribute(WebConstants.TESTIMONIAL_TYPES, TestimonialType.values());

        return "administration.press.testimonial.form";
    }

    /**
     * Submits a form to either insert a testimonial object
     *
     * @param testimonial        testimonial
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @param locale             locale
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}/testimonial", method = RequestMethod.POST)
    public String submitTestimonial(@PathVariable("urlName") String urlName,
                                    @Valid Testimonial testimonial, BindingResult errors,
                                    RedirectAttributes redirectAttributes,
                                    Model model, Locale locale) throws Exception {

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.TESTIMONIAL, testimonial);
            model.addAttribute(WebConstants.TESTIMONIAL_TYPES, TestimonialType.values());
            return "administration.press.testimonial.form";
        }

        pressService.saveTestimonial(testimonial);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.testimonial.saved.successfully", new String[]{testimonial.getHdr()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + urlName;
    }

    /**
     * Deletes a press testimonial object
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/press/{urlName}/testimonial/{id}", method = RequestMethod.DELETE)
    public String deleteTestimonial(@PathVariable("urlName") String urlName,
                                    @PathVariable("id") String id,
                                    RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        pressService.removeTestimonial(urlName, id);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.testimonial.deleted.successfully", null, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + urlName;
    }

    /**
     * Shows a form to either insert or update tweet
     *
     * @param urlName urlName
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}/tweet", method = RequestMethod.GET)
    public String showTweetForm(@PathVariable("urlName") String urlName,
                                @RequestParam(value = "id", required = false) String id,
                                Model model) throws Exception {

        model.addAttribute(WebConstants.TWEET, pressService.findTweet(urlName, id));

        return "administration.press.tweet.form";
    }

    /**
     * Submits a form to either insert a tweet object
     *
     * @param tweet              testimonial
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @param locale             locale
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/press/{urlName}/tweet", method = RequestMethod.POST)
    public String submitTweet(@PathVariable("urlName") String urlName,
                              @Valid Tweet tweet, BindingResult errors,
                              RedirectAttributes redirectAttributes,
                              Model model, Locale locale) throws Exception {

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.TWEET, tweet);
            return "administration.press.tweet.form";
        }

        pressService.saveTweet(tweet);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.tweet.saved.successfully", new String[]{tweet.getTxt()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + urlName;
    }

    /**
     * Deletes a press tweet object
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/press/{urlName}/tweet/{id}", method = RequestMethod.DELETE)
    public String deleteTweet(@PathVariable("urlName") String urlName,
                              @PathVariable("id") String id,
                              RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        pressService.removeTweet(urlName, id);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("press.tweet.deleted.successfully", null, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/press/" + urlName;
    }
}
