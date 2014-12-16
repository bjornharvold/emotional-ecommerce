/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.PressService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.press.PressManagerController;
import com.lela.domain.document.Press;
import com.lela.domain.document.Testimonial;
import com.lela.domain.document.Tweet;
import com.lela.domain.dto.press.PressImageEntry;
import com.lela.domain.enums.TestimonialType;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class PressControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(PressControllerFunctionalTest.class);
    private static final String PRESS_URL_NAME = "PressControllerFunctionalTest";
    private Resource resource = new ClassPathResource("testdata/image/blogimage.png");

    @Autowired
    private PressManagerController pressManagerController;

    @Autowired
    private PressService pressService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    private Press press = null;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        press = pressService.findPressByUrlName(PRESS_URL_NAME);

        if (press != null) {
            pressService.removePress(press.getRlnm());
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (press != null) {
            pressService.removePress(press.getRlnm());
            localCacheEvictionService.evictPress(press.getRlnm());
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testPressController() {
        log.info("Testing press controller...");

        SpringSecurityHelper.secureChannel();

        try {
            log.info("View the press list page");
            Model model = new BindingAwareModelMap();
            String view = pressManagerController.showPresses(0, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press.list", view);

            model = new BindingAwareModelMap();
            view = pressManagerController.showPressForm(null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press.form", view);
            assertNotNull("press object is null", model.asMap().get(WebConstants.PRESS));
            press = (Press) model.asMap().get(WebConstants.PRESS);

            log.info("Now we want to create a new press object");
            press.setNm("Press name");
            press.setRlnm(PRESS_URL_NAME);
            press.setSrlnm(PRESS_URL_NAME);

            model = new BindingAwareModelMap();
            BindingResult errors = new BindException(press, "press");
            RedirectAttributes ra = new RedirectAttributesModelMap();
            view = pressManagerController.submitPress(press, errors, ra, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + press.getRlnm(), view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.saved.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));
            log.info("Press object saved successfully");

            log.info("Now we want to go to its own press page");
            model = new BindingAwareModelMap();
            view = pressManagerController.showPress(PRESS_URL_NAME, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press", view);
            assertNotNull("Press object is null", model.asMap().get(WebConstants.PRESS));
            press = (Press) model.asMap().get(WebConstants.PRESS);
            assertNull("Press images are not null", press.getMgs());
            assertNull("Testimonials are not null", press.getTstmnls());
            assertNull("Tweets are not null", press.getTwts());

            log.info("Let's create a press image");
            model = new BindingAwareModelMap();
            view = pressManagerController.showPressImageForm(PRESS_URL_NAME, null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press.image.form", view);
            assertNotNull("PressImageEntry is null", model.asMap().get(WebConstants.PRESS_IMAGE_ENTRY));

            PressImageEntry pie = (PressImageEntry) model.asMap().get(WebConstants.PRESS_IMAGE_ENTRY);
            pie.setRdr(1);
            pie.setTxt("Some text");
            pie.setRl("http://www.google.com");
            pie.setRlnm(PRESS_URL_NAME);
            pie.setDt(new Date());
            pie.setHdr("Header");
            pie.setPblshdt(new Date());
            pie.setPblshr("Publisher");
            pie.setMultipartFile(new MockMultipartFile("test_file", "test_file.png", "image/png", resource.getInputStream()));

            log.info("We're not creating any errors here. We're just showing what it a good request looks like.");
            model = new BindingAwareModelMap();
            errors = new BindException(pie, "pressImageEntry");
            ra = new RedirectAttributesModelMap();
            view = pressManagerController.submitPressImage(PRESS_URL_NAME, pie, errors, ra, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + PRESS_URL_NAME, view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.image.saved.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));

            log.info("Let's create a testimonial");
            model = new BindingAwareModelMap();
            view = pressManagerController.showTestimonialForm(PRESS_URL_NAME, null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press.testimonial.form", view);
            assertNotNull("Testimonial is null", model.asMap().get(WebConstants.TESTIMONIAL));
            assertNotNull("Testimonial types is null", model.asMap().get(WebConstants.TESTIMONIAL_TYPES));

            Testimonial testimonial = (Testimonial) model.asMap().get(WebConstants.TESTIMONIAL);
            testimonial.setRlnm(PRESS_URL_NAME);
            testimonial.setDt(new Date());
            testimonial.setHdr("Header");
            testimonial.setPblshdt(new Date());
            testimonial.setPstdb("Posted by");
            testimonial.setTp(TestimonialType.BABY_CARRIERS);
            testimonial.setTxt("Text");

            log.info("We're not creating any errors here. We're just showing what it a good request looks like.");
            model = new BindingAwareModelMap();
            errors = new BindException(testimonial, "testimonial");
            ra = new RedirectAttributesModelMap();
            view = pressManagerController.submitTestimonial(PRESS_URL_NAME, testimonial, errors, ra, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + PRESS_URL_NAME, view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.testimonial.saved.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));

            log.info("Let's create a tweet");
            model = new BindingAwareModelMap();
            view = pressManagerController.showTweetForm(PRESS_URL_NAME, null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press.tweet.form", view);
            assertNotNull("Tweet is null", model.asMap().get(WebConstants.TWEET));

            Tweet tweet = (Tweet) model.asMap().get(WebConstants.TWEET);
            tweet.setRlnm(PRESS_URL_NAME);
            tweet.setDt(new Date());
            tweet.setRl("http://www.google.com");
            tweet.setPblshdt(new Date());
            tweet.setNm("Bill Maher");
            tweet.setHndl("@BillMaher");
            tweet.setMgrl("@BillMaher");
            tweet.setTxt("Text");

            log.info("We're not creating any errors here. We're just showing what it a good request looks like.");
            model = new BindingAwareModelMap();
            ra = new RedirectAttributesModelMap();
            errors = new BindException(tweet, "tweet");
            view = pressManagerController.submitTweet(PRESS_URL_NAME, tweet, errors, ra, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + PRESS_URL_NAME, view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.tweet.saved.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));

            log.info("Now, when we get the press object again, we should see press images, testimonials and tweets.");
            model = new BindingAwareModelMap();
            view = pressManagerController.showPress(PRESS_URL_NAME, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press", view);
            assertNotNull("Press object is null", model.asMap().get(WebConstants.PRESS));
            press = (Press) model.asMap().get(WebConstants.PRESS);
            assertNotNull("Press images are not null", press.getMgs());
            assertEquals("Press image size is incorrect", 1, press.getMgs().size());
            assertNotNull("Testimonials are not null", press.getTstmnls());
            assertEquals("Testimonials size is incorrect", 1, press.getTstmnls().size());
            assertNotNull("Tweets are not null", press.getTwts());
            assertEquals("Tweet size is incorrect", 1, press.getTwts().size());

            log.info("All nested press artifacts are present. Let's delete them all again.");

            ra = new RedirectAttributesModelMap();
            view = pressManagerController.deletePressImage(PRESS_URL_NAME, press.getMgs().get(0).getMgid(), ra, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + PRESS_URL_NAME, view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.image.deleted.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));

            ra = new RedirectAttributesModelMap();
            view = pressManagerController.deleteTestimonial(PRESS_URL_NAME, press.getTstmnls().get(0).getTid(), ra, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + PRESS_URL_NAME, view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.testimonial.deleted.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));

            ra = new RedirectAttributesModelMap();
            view = pressManagerController.deleteTweet(PRESS_URL_NAME, press.getTwts().get(0).getTid(), ra, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/" + PRESS_URL_NAME, view);
            assertNotNull("Redirect attributes is null", ra.getFlashAttributes());
            assertFalse("Redirect attributes is empty", ra.getFlashAttributes().isEmpty());
            assertEquals("Redirect attribute incorrect", "press.tweet.deleted.successfully", ra.getFlashAttributes().get(WebConstants.MESSAGE));

            log.info("Now, when we get the press object again, we should see press images, testimonials and tweets are again all empty.");
            model = new BindingAwareModelMap();
            view = pressManagerController.showPress(PRESS_URL_NAME, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press", view);
            assertNotNull("Press object is null", model.asMap().get(WebConstants.PRESS));
            press = (Press) model.asMap().get(WebConstants.PRESS);
            assertTrue("Press images are not empty", press.getMgs().isEmpty());
            assertTrue("Testimonials are not empty", press.getTstmnls().isEmpty());
            assertTrue("Tweets are not empty", press.getTwts().isEmpty());

            log.info("All nested press artifacts are present. Let's delete them all again.");

            log.info("Finally we want to delete the press object again");
            ra = new RedirectAttributesModelMap();
            view = pressManagerController.deletePress(PRESS_URL_NAME, ra, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/press/list", view);

            log.info("Make sure press object has been completely deleted");
            model = new BindingAwareModelMap();
            view = pressManagerController.showPress(PRESS_URL_NAME, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "administration.press", view);
            assertNull("Press object is not null", model.asMap().get(WebConstants.PRESS));
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing press controller complete");
    }

}
