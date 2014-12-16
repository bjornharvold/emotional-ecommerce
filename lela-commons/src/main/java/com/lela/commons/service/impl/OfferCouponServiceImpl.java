package com.lela.commons.service.impl;

import com.lela.commons.mail.MailServiceException;
import com.lela.commons.repository.OfferRepository;
import com.lela.commons.service.BranchService;
import com.lela.commons.service.FavoritesListService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.MailService;
import com.lela.commons.service.OfferCouponService;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.OfferValidationException;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Item;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.Offer;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.list.ListEntry;
import com.lela.domain.dto.coupon.CouponGenerationRequest;
import com.lela.domain.dto.coupon.CouponGenerationResult;
import com.lela.domain.dto.coupon.CouponRedemptionRequest;
import com.lela.domain.dto.coupon.CouponRedemptionResult;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.list.ListCardType;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service("offerCouponService")
public class OfferCouponServiceImpl implements OfferCouponService {
    private static final String COUPON_FULL_NAME_NOT_GIVEN = "error.coupon.full.name.not.given";
    private static final String COUPON_ALREADY_REDEEMED = "error.coupon.already.redeemed";
    private static final String COUPON_NOT_BELONG_USER = "error.coupon.not.belong.user";
    private static final String OFFER_NOT_EXISTS = "error.offer.not.exists";
    private static final String OFFER_EXPIRED = "error.offer.expired";
    private static final String OFFER_REMOVED = "error.offer.removed";
    private static final String BRANCH_LOCAL_CODE_NOT_FOUND = "error.branch.local.code.not.found";
    private static final String COUPON_NOT_EXISTS = "error.coupon.not.exists";

    private final Logger log = LoggerFactory.getLogger(OfferCouponServiceImpl.class);
    private final int COUPON_CODE_LENGTH = 6;
    private final char[] CODE_CHARS = {'2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private final ItemService itemService;
    /**
     * Field description
     */
    private final UserService userService;

    /**
     * Field description
     */
    private final OfferRepository offerRepository;

    /**
     * Field description
     */
    private final FavoritesListService favoritesListService;

    /**
     * Field description
     */
    private final MailService mailService;

    private final StoreService storeService;

    private final BranchService branchService;

    private final OfferService offerService;

    @Autowired
    public OfferCouponServiceImpl(ItemService itemService,
                                  OfferRepository offerRepository,
                                  UserService userService,
                                  FavoritesListService favoritesListService,
                                  MailService mailService,
                                  StoreService storeService,
                                  BranchService branchService,
                                  OfferService offerService) {
        this.itemService = itemService;
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.favoritesListService = favoritesListService;
        this.mailService = mailService;
        this.storeService = storeService;
        this.branchService = branchService;
        this.offerService = offerService;
    }

    /**
     * Generates a unique coupon code
     *
     * @return Returns a unique coupon code
     */
    @Override
    public String generateCouponCode() {
        String result = null;
        boolean isUnique = false;

        while (!isUnique) {
            result = RandomStringUtils.random(COUPON_CODE_LENGTH, 0, CODE_CHARS.length, false, false, CODE_CHARS);

            Long count = userService.findUserCountByCouponCode(result);

            if (count == null || count < 1) {
                isUnique = true;
            }
        }

        return result;
    }

    /**
     * @param userCode      userCode
     * @param couponRequest couponRequest
     * @return CouponGenerationResult object
     */
    @Override
    public CouponGenerationResult createCouponForItem(String userCode, CouponGenerationRequest couponRequest) throws OfferValidationException {
        CouponGenerationResult result = new CouponGenerationResult();

        UserSupplement us = userService.findUserSupplement(userCode);

        if (us != null) {
            // make sure user has filled in first and last name in his profile
            if (StringUtils.isBlank(us.getFnm()) && StringUtils.isBlank(us.getLnm())) {
                throw new OfferValidationException(COUPON_FULL_NAME_NOT_GIVEN);
            }

            Offer offer = offerService.findOfferByUrlName(couponRequest.getOfferUrlName());
            Branch branch = branchService.findBranchByUrlName(offer.getBrnchrlnm());

            // this throws an exception if something is wrong with the offer
            validateOffer(offer);

            List<Coupon> coupons = userService.findCoupons(userCode);

            // Check if user has coupons
            if (coupons != null && !coupons.isEmpty()) {
                // Check if user already has a coupon(s) for this offer
                for (Coupon existingCoupon : coupons) {
                    if (existingCoupon.getFfrrlnm().equals(couponRequest.getOfferUrlName()) && existingCoupon.getRdmptndt() == null) {
                        // Coupon has not been redeemed, so return it and clear any past coupon
                        result.setCoupon(existingCoupon);
                        return result;
                    }
                }

                Coupon lastRedeemedCouponForOffer = null;
                for (Coupon existingCoupon : coupons) {
                    if (existingCoupon.getFfrrlnm().equals(couponRequest.getOfferUrlName()) && existingCoupon.getRdmptndt() != null) {
                        // Track redeemed coupons to store last redeemed coupon.
                        // This is used to determine if user might be able to create new coupon
                        if (lastRedeemedCouponForOffer == null) {
                            lastRedeemedCouponForOffer = existingCoupon;
                        } else if (existingCoupon.getRdmptndt().after(lastRedeemedCouponForOffer.getRdmptndt())) {
                            lastRedeemedCouponForOffer = existingCoupon;
                        }
                    }
                }

                // Determine if only coupon(s) for offer have already been redeemed
                if (lastRedeemedCouponForOffer != null) {
                    // Check if user can create another coupon for offer
                    if (!offer.getNprprsn()) {

                        if ((offer.getFrqncy() != null) && (offer.getFrqncy() > 0)) {
                            // Calculate frequency of coupon creation from last redeemed coupon.
                            Calendar frequencyDate = new GregorianCalendar();
                            frequencyDate.setTimeInMillis(lastRedeemedCouponForOffer.getCrtddt().getTime());
                            frequencyDate.add(Calendar.DAY_OF_MONTH, offer.getFrqncy());

                            // Check if next available date to create coupon has not been reached yet
                            if (frequencyDate.getTime().after(new Date())) {
                                // User cannot create new coupon so set coupon to last redeemed coupon
                                result.setCoupon(lastRedeemedCouponForOffer);
                                result.setExisting(true);

                                return result;
                            }
                        }
                    } else {
                        throw new OfferValidationException(COUPON_ALREADY_REDEEMED);
                    }
                }
            }

            result.setExisting(false);
            String couponCode = generateCouponCode();

            Coupon coupon = new Coupon();
            coupon.setCpnd(new ObjectId());
            coupon.setFfrrlnm(couponRequest.getOfferUrlName());
            coupon.setFnm(us.getFnm());
            coupon.setLnm(us.getLnm());
            coupon.setCpncd(couponCode);
            coupon.setCrtddt(new Date());

            if (StringUtils.isNotBlank(couponRequest.getItemUrlName())) {
                coupon.setTmrlnm(couponRequest.getItemUrlName());
            }

            // add coupon to our result object
            result.setCoupon(coupon);

            // check if there are currently no coupons
            if (coupons == null) {
                coupons = new ArrayList<Coupon>();
            }

            // add the newly created coupon to the user
            coupons.add(coupon);

            // save coupons list again
            userService.saveCoupons(userCode, coupons);

            // Add store for the branch to user's store list if it doesn't already exist
            boolean foundStoreInList = false;

            List<ListCard> stores = favoritesListService.findAllListCards(userCode, ListCardType.STORE);

            if (stores != null && !stores.isEmpty()) {
                for (ListCard ss : stores) {
                    Store store = storeService.findStoreByUrlName(ss.getRlnm());

                    if (store != null) {
                        if (store.getMrchntd().equals(branch.getMrchntd())) {
                            foundStoreInList = true;
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Store: " + ss.getRlnm() + " is not available but it should be");
                        }
                    }
                }
            }

            if (!foundStoreInList) {
                ListEntry li = new ListEntry();
                Store store = storeService.findStoreByMerchantId(branch.getMrchntd());
                li.setRlnm(store.getRlnm());
                li.setUserCode(userCode);
                favoritesListService.addStoreToList(li);
            }

            // Send email notifying requester user
            sendCouponCreationConfirmationEmail(coupon, offer, us, branch);
        }
        return result;

    }

    /**
     * Sends out a coupon confirmation email
     *
     * @param coupon coupon
     * @param offer  offer
     * @param us   user
     * @param branch branch
     */
    private void sendCouponCreationConfirmationEmail(Coupon coupon, Offer offer, UserSupplement us, Branch branch) {
        Locale locale = us.getLcl();

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.USER_FIRST_NAME, us.getFnm());
            parameters.put(MailParameter.USER_LAST_NAME, us.getLnm());
            parameters.put(MailParameter.COUPON_CODE, coupon.getCpncd());
            parameters.put(MailParameter.COUPON_FIRST_NAME, coupon.getFnm());
            parameters.put(MailParameter.COUPON_LAST_NAME, coupon.getLnm());
            parameters.put(MailParameter.BRANCH_URL_NAME, branch.getRlnm());
            parameters.put(MailParameter.BRANCH_NAME, branch.getNm());
            parameters.put(MailParameter.BRANCH_ADDRESS, branch.getDdrss());
            parameters.put(MailParameter.BRANCH_CITY, branch.getCty());
            parameters.put(MailParameter.BRANCH_STATE, branch.getSt());
            parameters.put(MailParameter.BRANCH_ZIP, branch.getZp());
            parameters.put(MailParameter.OFFER_URL_NAME, offer.getRlnm());
            parameters.put(MailParameter.OFFER_VALUE_TERM, offer.getVlndtrms());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM d, yyyy");
            String offerExpirationDate = simpleDateFormat.format(offer.getXprtndt());
            parameters.put(MailParameter.OFFER_EXPIRATION_DATE, offerExpirationDate);

            parameters.put(MailParameter.OFFER_DESCRIPTION, offer.getDscrptn());

            mailService.sendCouponConfirmation(us.getMl(), parameters, locale);
        } catch (MailServiceException e) {
            log.error(e.getMessage(), e);
        }

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.USER_EMAIL, us.getMl());
            parameters.put(MailParameter.USER_FIRST_NAME, us.getFnm());
            parameters.put(MailParameter.USER_LAST_NAME, us.getLnm());
            parameters.put(MailParameter.COUPON_CODE, coupon.getCpncd());
            parameters.put(MailParameter.COUPON_REQUEST_DATE, coupon.getCrtddt());
            parameters.put(MailParameter.BRANCH_NAME, branch.getNm());

            if (StringUtils.isNotBlank(coupon.getTmrlnm())) {
                Item item = itemService.findItemByUrlName(coupon.getTmrlnm());
                parameters.put(MailParameter.COUPON_ITEM, item.getNm());
            }

            mailService.sendCouponRequest(us.getMl(), parameters, locale);
        } catch (MailServiceException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Redeemes a coupon code for one user
     *
     * @param crr request
     * @return Returns the coupon object that was redeemed
     */
    @Override
    public CouponRedemptionResult redeemCoupon(CouponRedemptionRequest crr) throws OfferValidationException {
        CouponRedemptionResult result = new CouponRedemptionResult();

        Coupon coupon = findCoupon(crr.getCouponCode());

        Branch branch = branchService.findBranchByLocalCode(crr.getLocalCode());

        // add to result
        result.setBranch(branch);

        if (coupon != null) {

            // add to result
            result.setCoupon(coupon);

            if (coupon.getRdmptndt() != null) {
                throw new OfferValidationException(COUPON_ALREADY_REDEEMED);
            }

            Offer offer = offerService.findOfferByUrlName(coupon.getFfrrlnm());

            // add to result
            result.setOffer(offer);

            // throws exception if anything is wrong
            validateOffer(offer);

            // let's validate a little first
            if (!isMerchantLocalCode(branch.getMrchntd(), crr.getLocalCode())) {
                throw new OfferValidationException(BRANCH_LOCAL_CODE_NOT_FOUND);
            }

            UserSupplement us = userService.findUserSupplementByCouponCode(crr.getCouponCode());

            if (us != null && us.getCpns() != null && !us.getCpns().isEmpty()) {
                boolean dirty = false;

                for (Coupon existingCoupon : us.getCpns()) {
                    if (StringUtils.equals(existingCoupon.getCpncd(), coupon.getCpncd())) {
                        dirty = true;
                        existingCoupon.setRdmptndt(new Date());

                        // send email
                        sendCouponRedemptionConfirmationEmail(us, branch, existingCoupon);

                        break;
                    }
                }

                if (dirty) {
                    // save the update coupon list
                    userService.saveCoupons(us.getCd(), us.getCpns());
                }
            }
        } else {
            throw new OfferValidationException(COUPON_NOT_EXISTS);
        }

        return result;
    }

    /**
     * Sends out an email confirmation the user redeemed her coupon
     *
     * @param us           user
     * @param branch         branch
     * @param existingCoupon existingCoupon
     */
    private void sendCouponRedemptionConfirmationEmail(UserSupplement us, Branch branch, Coupon existingCoupon) {
        // Send email notifying requester user
        Locale locale = us.getLcl();

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.USER_EMAIL, us.getMl());
            parameters.put(MailParameter.USER_FIRST_NAME, us.getFnm());
            parameters.put(MailParameter.USER_LAST_NAME, us.getLnm());
            parameters.put(MailParameter.COUPON_CODE, existingCoupon.getCpncd());
            parameters.put(MailParameter.COUPON_REQUEST_DATE, existingCoupon.getCrtddt());
            parameters.put(MailParameter.COUPON_REDEEM_DATE, existingCoupon.getRdmptndt());
            parameters.put(MailParameter.BRANCH_NAME, branch.getNm());

            if (StringUtils.isNotBlank(existingCoupon.getTmrlnm())) {
                Item item = itemService.findItemByUrlName(existingCoupon.getTmrlnm());
                parameters.put(MailParameter.COUPON_ITEM, item.getNm());
            }

            mailService.sendCouponTransaction(us.getMl(), parameters, locale);
        } catch (MailServiceException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Returns the coupon based on the coupon code
     *
     * @param couponCode couponCode
     * @return Coupon object
     */
    @Override
    public Coupon findCoupon(String couponCode) {
        return userService.findCouponByCouponCode(couponCode);
    }

    /**
     * Validates to true if the merchant "owns" the local code
     *
     * @param merchantId merchantId
     * @param localCode  localCode
     * @return Boolean
     */
    @Override
    public Boolean isMerchantLocalCode(String merchantId, String localCode) {
        boolean valid = false;

        List<Branch> branches = branchService.findBranchesByMerchantId(merchantId);
        for (Branch branch : branches) {
            if (branch.getLclcd().equalsIgnoreCase(localCode)) {
                valid = true;
                break;
            }
        }

        return valid;
    }

    /**
     * Finds all coupons that matches a specific offer url name
     *
     * @param offerUrlName offerUrlName
     * @return List of coupons
     */
    @Override
    public List<Coupon> findCouponsByOfferUrlName(String offerUrlName) {
        return userService.findCouponsByOfferUrlName(offerUrlName);
    }

    /**
     * Returns a list of all coupons for a specific branch
     *
     * @param branchUrlName branchUrlName
     * @return List of coupons
     */
    @Override
    public List<Coupon> findCouponsByBranch(String branchUrlName) {
        List<Coupon> result = null;

        List<Offer> offers = offerRepository.findByBranchUrlName(branchUrlName);

        if ((offers != null) && (!offers.isEmpty())) {
            result = new ArrayList<Coupon>();

            for (Offer offer : offers) {
                List<Coupon> coupons = findCouponsByOfferUrlName(offer.getRlnm());
                if ((coupons != null) && (!coupons.isEmpty())) {
                    for (Coupon coupon : coupons) {
                        if (StringUtils.equals(coupon.getFfrrlnm(), offer.getRlnm())) {
                            result.add(coupon);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Sets the flag isDeleted on the offer if any users already have a coupon against.
     * If no users have a coupon for it, delete it.
     *
     * @param offerUrlName offerUrlName
     */
    @Override
    public void removeOrDisableOffer(String offerUrlName) {
        Offer offer = offerRepository.findByUrlName(offerUrlName);
        if (offer != null) {
            Long userCount = userService.findUserByCouponOfferUrlNameCount(offerUrlName);

            if (userCount > 0) {
                offer.setSdltd(true);
                offerService.saveOffer(offer);
            } else {
                offerService.removeOffer(offer.getRlnm());
            }
        }
    }

    /**
     * Some generic validation on the offer object
     *
     * @param offer offer
     * @throws com.lela.commons.service.OfferValidationException
     *          exception
     */
    @Override
    public void validateOffer(Offer offer) throws OfferValidationException {
        if (offer == null) {
            throw new OfferValidationException(OFFER_NOT_EXISTS);
        } else if (offer.getXprtndt() != null && offer.getXprtndt().before(new Date())) {
            throw new OfferValidationException(OFFER_EXPIRED);
        } else if (offer.getSdltd() != null && offer.getSdltd()) {
            throw new OfferValidationException(OFFER_REMOVED);
        }
    }

    @Override
    public void validateUserCouponOwnership(String userCode, Coupon coupon) throws OfferValidationException {
        if (StringUtils.isNotBlank(userCode) && coupon != null) {
            List<Coupon> coupons = userService.findCoupons(userCode);

            if ((coupons != null) && (coupons.size() > 0)) {
                boolean found = false;
                for (Coupon userCoupon : coupons) {
                    if (userCoupon.getCpncd().equals(coupon.getCpncd())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    throw new OfferValidationException(COUPON_NOT_BELONG_USER);
                }
            }
        }
    }
}
