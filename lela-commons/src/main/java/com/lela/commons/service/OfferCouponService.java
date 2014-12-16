package com.lela.commons.service;

import com.lela.domain.document.Coupon;
import com.lela.domain.document.Offer;
import com.lela.domain.document.User;
import com.lela.domain.dto.coupon.CouponGenerationRequest;
import com.lela.domain.dto.coupon.CouponGenerationResult;
import com.lela.domain.dto.coupon.CouponRedemptionRequest;
import com.lela.domain.dto.coupon.CouponRedemptionResult;

import java.util.List;

public interface OfferCouponService {
    String generateCouponCode();
    CouponGenerationResult createCouponForItem(String userCode, CouponGenerationRequest couponRequest) throws OfferValidationException;
    CouponRedemptionResult redeemCoupon(CouponRedemptionRequest couponCode) throws OfferValidationException;
    Coupon findCoupon(String couponCode);
    Boolean isMerchantLocalCode(String merchantId, String localCode);
    List<Coupon> findCouponsByOfferUrlName(String offerUrlName);
    List<Coupon> findCouponsByBranch(String branchUrlName);
    void removeOrDisableOffer(String offerUrlName);
    void validateOffer(Offer offer) throws OfferValidationException;
    void validateUserCouponOwnership(String userCode, Coupon coupon) throws OfferValidationException;
}
