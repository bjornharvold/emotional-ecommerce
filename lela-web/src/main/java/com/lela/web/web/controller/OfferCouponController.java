package com.lela.web.web.controller;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.OfferCouponService;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.OfferValidationException;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Offer;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.coupon.CouponGenerationRequest;
import com.lela.domain.dto.coupon.CouponGenerationResult;
import com.lela.domain.dto.coupon.CouponRedemptionRequest;
import com.lela.domain.dto.coupon.CouponRedemptionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class OfferCouponController {

    private final static Logger log = LoggerFactory.getLogger(ItemController.class);

    /**
     * Field description
     */
    private final OfferCouponService offerCouponService;

    private final BranchService branchService;

    private final OfferService offerService;

    /**
     * Constructs ...
     *
     * @param offerCouponService offerCouponService
     * @param branchService branchService
     * @param offerService offerService
     */
    @Autowired
    public OfferCouponController(OfferCouponService offerCouponService,
                                 BranchService branchService,
                                 OfferService offerService) {
        this.offerCouponService = offerCouponService;
        this.branchService = branchService;
        this.offerService = offerService;
    }

    @RequestMapping(value = "/local/deals", method = RequestMethod.GET)
    public String deals(Model model) {
        return "redirect:/store/giggle";
    }

    @RequestMapping(value = "/user/local/coupon/create", method = RequestMethod.GET)
    public String show(@RequestParam(value = "offerUrlName", required = true) String offerUrlName,
                       Model model) throws Exception {

        try {
            Offer offer = offerService.findOfferByUrlName(offerUrlName);
            offerCouponService.validateOffer(offer);
            Branch branch = branchService.findBranchByUrlName(offer.getBrnchrlnm());
            model.addAttribute(WebConstants.OFFER, offer);
            model.addAttribute(WebConstants.BRANCH, branch);
        } catch (OfferValidationException ex) {
            model.addAttribute(WebConstants.ERROR, ex.getMessage());
        }

        return "local.coupon.create";
    }

    /**
     * Item URL Name is NOT required
     *
     * @param couponRequest couponRequest
     * @param model         model
     * @return Tile def
     */
    @RequestMapping(value = "/user/local/coupon/create", method = RequestMethod.POST)
    public String generateCouponForUser(@Valid CouponGenerationRequest couponRequest,
                                        Model model) {
        String view = "local.coupon.confirmation";
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        String error = null;

        try {
            CouponGenerationResult couponResult = offerCouponService.createCouponForItem(principal.getUser().getCd(), couponRequest);
            model.addAttribute(WebConstants.COUPON_GENERATION_RESULT, couponResult);
        } catch (OfferValidationException e) {
            model.addAttribute(WebConstants.ERROR, error);
            view = "local.coupon.create";
        }

        return view;
    }

    /**
     * Shows the view to redeem a coupon
     *
     * @param branchUrlName branchUrlName
     * @param couponCode    couponCode
     * @param model         model
     * @return Tile def
     */
    @RequestMapping(value = "/local/coupon/redeem", method = RequestMethod.GET)
    public String showRedeemCouponForm(@RequestParam(value = "branchUrlName", required = true) String branchUrlName,
                                       @RequestParam(value = "couponCode", required = true) String couponCode,
                                       Model model) {

        Coupon coupon = offerCouponService.findCoupon(couponCode);
        Offer offer = offerService.findOfferByUrlName(coupon.getFfrrlnm());
        Branch branch = branchService.findBranchByUrlName(offer.getBrnchrlnm());

        model.addAttribute(WebConstants.COUPON, coupon);
        model.addAttribute(WebConstants.OFFER, offer);
        model.addAttribute(WebConstants.BRANCH, branch);

        return "local.coupon.redeem";
    }

    @RequestMapping(value = "/local/coupon/redeem", method = RequestMethod.PUT)
    public String redeemCoupon(@Valid CouponRedemptionRequest request, Model model, RedirectAttributes redirectAttributes) {
        String view = "local.coupon.redeem.success";

        try {
            CouponRedemptionResult result = offerCouponService.redeemCoupon(request);
            model.addAttribute(WebConstants.COUPON_REDEMPTION_RESULT, result);
        } catch (OfferValidationException e) {
            redirectAttributes.addFlashAttribute(WebConstants.ERROR, e.getMessage());
            view = "redirect:/local/coupon/redeem";
        }


        return view;
    }


    @RequestMapping(value = "/user/local/coupon/confirmation", method = RequestMethod.GET)
    public String confirmation(@RequestParam(value = "offerUrlName", required = true) String offerUrlName,
                               @RequestParam(value = "couponCode", required = true) String couponCode,
                               Model model) throws Exception {

        Offer offer = offerService.findOfferByUrlName(offerUrlName);
        Branch branch = branchService.findBranchByUrlName(offer.getBrnchrlnm());
        Coupon coupon = offerCouponService.findCoupon(couponCode);

        model.addAttribute(WebConstants.OFFER, offer);
        model.addAttribute(WebConstants.BRANCH, branch);
        model.addAttribute(WebConstants.COUPON, coupon);

        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        User user = principal.getUser();

        try {
            offerCouponService.validateUserCouponOwnership(user.getCd(), coupon);
        } catch (OfferValidationException e) {
            model.addAttribute(WebConstants.ERROR, e.getMessage());
        }

        return "local.coupon.confirmation";
    }
}
