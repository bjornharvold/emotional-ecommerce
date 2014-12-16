/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.ApplicationConstants;
import com.lela.domain.dto.Favorite;
import com.lela.domain.dto.Profile;
import com.lela.domain.dto.UserAccountDto;
import com.lela.domain.dto.UserDto;
import com.lela.domain.dto.list.ListCardBoardName;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.RegisterUserRequest;
import com.lela.domain.enums.AddressType;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MetricType;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.ProfileStatus;
import com.lela.domain.enums.list.ListCardType;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.social.connect.UserProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/7/12
 * Time: 10:07 PM
 * Responsibility:
 */
@Document
public class UserSupplement extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 6172102508023958255L;

    /**
     * User code
     */
    @Indexed(unique = true)
    private String cd;

    /**
     * Motivators
     */
    private Map<MotivatorSource, Motivator> mtvtrmp;

    /**
     * Saved functional filters
     */
    private List<FunctionalFilterPreset> fltrprsts;

    /**
     * User attributes validated by UserAttributeType enum
     */
    private Map<String, List<String>> attrs;

    /**
     * User associations (e.g. - Blogs)
     */
    private List<UserAssociation> ssctns;

    /**
     * Feedback
     */
    private Feedback fdbck;

    /**
     * Events
     */
    private List<UserEvent> vnts;

    /**
     * Coupons
     */
    private List<Coupon> cpns;

    /**
     * Answers
     */
    private List<UserAnswer> nswrs;

    /**
     * Social network connections
     */
    private List<Social> scls;

    /**
     * Metrics
     */
    private Map<MetricType, String> mtrcs;

    /**
     * Lela List
     */
    private List<ListCardBoard> brds;

    /**
     * Rewards
     */
    private List<Reward> rwrds;

    /**
     * Friend level percentage
     */
    private Integer frndlvl = 0;

    /**
     * Current zip code
     */
    private String czp;

    /**
     * Country
     */
    private String cntry;

    /**
     * Date of Birth
     */
    private Date db;

    /**
     * Gender
     */
    private Gender gndr;

    /**
     * Age of User
     */
    private Integer age;

    /**
     * User Addresses
     */
    private Map<AddressType, Address> ddrss;

    /**
     * Home zip code
     */
    private String hzp;

    /**
     * Mobile phone number we can use to pre-populate the price alert with
     */
    private String phn;

    /**
     * Has the affiliate been notified of the user registration
     */
    private Boolean ffltntfd;

    /**
     * Locale
     */
    private Locale lcl = Locale.US;    // default

    /** Profile image */
    private Map<String, String> mg;

    /**
     * Full name
     */
    private String fllnm;

    /**
     * First name
     */
    private String fnm;

    /**
     * Last name
     */
    private String lnm;

    /**
     * Duplicate of User.ml
     */
    private String ml;

    /**
     * if the user opted in for news and info at registration
     */
    private Boolean ptn = Boolean.FALSE;

    /** Email verified */
    private Boolean vrfd = Boolean.FALSE;

    /** Affiliate Account Url Name */
    private String ffltccntrlnm;

    public UserSupplement() {
    }

    public UserSupplement(String cd) {
        this.cd = cd;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public Map<MotivatorSource, Motivator> getMtvtrmp() {
        return mtvtrmp;
    }

    public void setMtvtrmp(Map<MotivatorSource, Motivator> mtvtrmp) {
        this.mtvtrmp = mtvtrmp;
    }

    public List<FunctionalFilterPreset> getFltrprsts() {
        return fltrprsts;
    }

    public void setFltrprsts(List<FunctionalFilterPreset> fltrprsts) {
        this.fltrprsts = fltrprsts;
    }

    public Map<String, List<String>> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, List<String>> attrs) {
        this.attrs = attrs;
    }

    public List<UserAssociation> getSsctns() {
        return ssctns;
    }

    public void setSsctns(List<UserAssociation> ssctns) {
        this.ssctns = ssctns;
    }

    public Feedback getFdbck() {
        return fdbck;
    }

    public void setFdbck(Feedback fdbck) {
        this.fdbck = fdbck;
    }

    public List<UserEvent> getVnts() {
        return vnts;
    }

    public void setVnts(List<UserEvent> vnts) {
        this.vnts = vnts;
    }

    public List<Coupon> getCpns() {
        return cpns;
    }

    public void setCpns(List<Coupon> cpns) {
        this.cpns = cpns;
    }

    public List<UserAnswer> getNswrs() {
        return nswrs;
    }

    public void setNswrs(List<UserAnswer> nswrs) {
        this.nswrs = nswrs;
    }

    public List<Social> getScls() {
        return scls;
    }

    public void setScls(List<Social> scls) {
        this.scls = scls;
    }

    public Map<MetricType, String> getMtrcs() {
        return mtrcs;
    }

    public void setMtrcs(Map<MetricType, String> mtrcs) {
        this.mtrcs = mtrcs;
    }

    public List<ListCardBoard> getBrds() {
        return brds;
    }

    public void setBrds(List<ListCardBoard> brds) {
        this.brds = brds;
    }

    public List<Reward> getRwrds() {
        return rwrds;
    }

    public void setRwrds(List<Reward> rwrds) {
        this.rwrds = rwrds;
    }

    public Integer getFrndlvl() {
        return frndlvl;
    }

    public void setFrndlvl(Integer frndlvl) {
        this.frndlvl = frndlvl;
    }

    public String getCzp() {
        return czp;
    }

    public void setCzp(String czp) {
        this.czp = czp;
    }

    public String getCntry() {
        return cntry;
    }

    public void setCntry(String cntry) {
        this.cntry = cntry;
    }

    public Date getDb() {
        return db;
    }

    public void setDb(Date db) {
        this.db = db;
    }

    public Gender getGndr() {
        return gndr;
    }

    public void setGndr(Gender gndr) {
        this.gndr = gndr;
    }

    public String getHzp() {
        return hzp;
    }

    public void setHzp(String hzp) {
        this.hzp = hzp;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }


    /**
     * Method description
     *
     * @return Return value
     */
    public String getFllnm() {
        if (fllnm != null) {
            return fllnm;
        } else {
            if (getFnm() != null && getLnm() != null) {
                return String.format("%s %s", getFnm(), getLnm());
            } else if (getFnm() != null) {
                return getFnm();
            } else {
                return getLnm();
            }
        }
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getFnm() {
        return fnm;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public Locale getLcl() {
        return lcl;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getLnm() {
        return lnm;
    }

    public String getMl() {
        return ml;
    }

    /**
     * Method description
     *
     * @param fllnm fllnm
     */
    public void setFllnm(String fllnm) {
        this.fllnm = fllnm;
    }

    /**
     * Method description
     *
     * @param fnm fnm
     */
    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    /**
     * Method description
     *
     * @param lcl locale
     */
    public void setLcl(Locale lcl) {
        this.lcl = lcl;
    }

    /**
     * Method description
     *
     * @param lnm lnm
     */
    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public Boolean getPtn() {
        return ptn;
    }

    public Map<String, String> getMg() {
        return mg;
    }

    public void setMg(Map<String, String> mg) {
        this.mg = mg;
    }

    public void setPtn(Boolean ptn) {
        this.ptn = ptn;
    }

    public Boolean getVrfd() {
        return vrfd;
    }

    public void setVrfd(Boolean vrfd) {
        this.vrfd = vrfd;
    }

    public String getFfltccntrlnm() {
        return ffltccntrlnm;
    }

    public void setFfltccntrlnm(String ffltccntrlnm) {
        this.ffltccntrlnm = ffltccntrlnm;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Map<AddressType, Address> getDdrss() {
        return ddrss;
    }

    public void setDdrss(Map<AddressType, Address> ddrss) {
        this.ddrss = ddrss;
    }

    public Boolean getFfltntfd() {
        return ffltntfd;
    }

    public void setFfltntfd(Boolean ffltntfd) {
        this.ffltntfd = ffltntfd;
    }

    //~--- methods ------------------------------------------------------------

    // convenience method user by the profile controller


    /**
     * Method description
     *
     * @param boardCode boardCode
     * @param urlName   rlnm
     * @return Return value
     */
    public boolean containsListCard(String boardCode, String urlName) {
        ListCardBoard lcb = findListCardBoard(boardCode);
        boolean result = false;

        if (lcb != null) {
            ListCard lc = lcb.findListCard(urlName);

            if (lc != null) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param boardCode boardCode
     * @param urlName   rlnm
     */
    public void removeListCard(String boardCode, String urlName) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.removeListCard(urlName);
        }

    }

    /**
     * Method description
     *
     * @return Return value
     */
    public Integer getFrndlvlnmbr() {
        Integer result = 0;

        if ((frndlvl >= 0) && (frndlvl <= 10)) {
            result = 1;
        }

        if ((frndlvl > 10) && (frndlvl <= 20)) {
            result = 2;
        }

        if ((frndlvl > 20) && (frndlvl <= 30)) {
            result = 3;
        }

        if ((frndlvl > 30) && (frndlvl <= 40)) {
            result = 4;
        }

        if ((frndlvl > 40) && (frndlvl <= 50)) {
            result = 5;
        }

        if ((frndlvl > 50) && (frndlvl <= 60)) {
            result = 6;
        }

        if ((frndlvl > 60) && (frndlvl <= 70)) {
            result = 7;
        }

        if ((frndlvl > 70) && (frndlvl <= 80)) {
            result = 8;
        }

        if ((frndlvl > 80) && (frndlvl <= 90)) {
            result = 9;
        }

        if ((frndlvl > 90) && (frndlvl <= 100)) {
            result = 9;
        }

        return result;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public ListCardBoard findListCardBoard(String boardCode) {

        if ((brds != null) && !brds.isEmpty()) {
            for (ListCardBoard lcb : brds) {
                if (StringUtils.equals(lcb.getCd(), boardCode)) {
                    return lcb;
                }
            }
        }

        return null;
    }

    public ListCard findListCard(String boardCode, String itemUrlName) {
        ListCardBoard lcb = findListCardBoard(boardCode);
        ListCard result = null;

        if (lcb != null) {
            result = lcb.findListCard(itemUrlName);
        }

        return result;
    }

    public Social getSocial(String providerId) {

        if ((scls != null) && !scls.isEmpty()) {

            for (Social social : scls) {
                if (StringUtils.equals(social.getProviderId(), providerId)) {
                    return social;
                }
            }
        }

        return null;
    }

    public Map<String, Social> getSocials() {
        Map<String, Social> result = null;

        if ((scls != null) && !scls.isEmpty()) {
            result = new HashMap<String, Social>();

            for (Social social : scls) {
                result.put(social.getProviderId(), social);
            }
        }

        return result;
    }

    public Boolean getFacebook() {
        return getSocial(ApplicationConstants.FACEBOOK) != null;
    }

    public void updateProfile(Profile profile) {
        this.db = profile.getDb();
        this.hzp = profile.getHzp();
        this.czp = profile.getCzp();
        this.gndr = profile.getGndr();
        this.cntry = profile.getCntry();
        this.fnm = profile.getFnm();
        this.lnm = profile.getLnm();
        this.lcl = profile.getLcl();

        if (this.mtrcs == null) {
            this.mtrcs = new HashMap<MetricType, String>();
        }

        if (StringUtils.isNotBlank(profile.getNdstry())) {
            this.mtrcs.put(MetricType.INDUSTRY, profile.getNdstry());
        }

        if (StringUtils.isNotBlank(profile.getCmpnysz())) {
            this.mtrcs.put(MetricType.COMPANY_SIZE, profile.getCmpnysz());
        }

        if (StringUtils.isNotBlank(profile.getJbttl())) {
            this.mtrcs.put(MetricType.JOB_TITLE, profile.getJbttl());
        }

        if (StringUtils.isNotBlank(profile.getNcm())) {
            this.mtrcs.put(MetricType.ANNUAL_HOUSEHOLD_INCOME, profile.getNcm());
        }
    }

    public void updateProfile(Favorite favorite) {
        if (StringUtils.isNotBlank(favorite.getCrmkr())) {
            if (this.mtrcs == null) {
                this.mtrcs = new HashMap<MetricType, String>();
            }
            this.mtrcs.put(MetricType.CAR_MAKER, favorite.getCrmkr());
        }

        if (StringUtils.isNotBlank(favorite.getShmp())) {
            if (this.mtrcs == null) {
                this.mtrcs = new HashMap<MetricType, String>();
            }
            this.mtrcs.put(MetricType.SHAMPOO, favorite.getShmp());
        }

        if (StringUtils.isNotBlank(favorite.getBr())) {
            if (this.mtrcs == null) {
                this.mtrcs = new HashMap<MetricType, String>();
            }
            this.mtrcs.put(MetricType.BEER, favorite.getBr());
        }

        if (StringUtils.isNotBlank(favorite.getFshnbrnd())) {
            if (this.mtrcs == null) {
                this.mtrcs = new HashMap<MetricType, String>();
            }
            this.mtrcs.put(MetricType.FASHION_BRAND, favorite.getFshnbrnd());
        }

        if (StringUtils.isNotBlank(favorite.getNlnstr())) {
            if (this.mtrcs == null) {
                this.mtrcs = new HashMap<MetricType, String>();
            }
            this.mtrcs.put(MetricType.ONLINE_STORE, favorite.getNlnstr());
        }

        if (StringUtils.isNotBlank(favorite.getGrcrstr())) {
            if (this.mtrcs == null) {
                this.mtrcs = new HashMap<MetricType, String>();
            }
            this.mtrcs.put(MetricType.GROCERY_STORE, favorite.getGrcrstr());
        }
    }

    public void updateUserAccount(UserAccountDto userAccountDto) {
        this.fnm = userAccountDto.getFnm();
        this.lnm = userAccountDto.getLnm();

        //The user has changed their email address
        this.setVrfd(!StringUtils.equals(this.ml, userAccountDto.getMl()));

        this.ml = userAccountDto.getMl();

        this.setPtn(userAccountDto.getOptin());
    }

    public Locale getLocale() {
        return getLcl();
    }

    public void addCoupon(Coupon coupon) {
        if (cpns == null) {
            cpns = new ArrayList<Coupon>();
        }

        cpns.add(coupon);
    }

    /**
     * Adds an alert to a saved item
     *
     * @param alert alert
     */
    public void addAlert(String boardCode, String urlName, Alert alert) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.addAlert(urlName, alert);
        }

    }

    public void addListCardBoards(List<ListCardBoard> lcbs) {
        if (lcbs != null && !lcbs.isEmpty()) {
            for (ListCardBoard lcb : lcbs) {
                // check to see if the board already exists
                ListCardBoard listCardBoard = findListCardBoard(lcb.getCd());

                if (listCardBoard == null) {
                    listCardBoard = addListCardBoard(lcb);
                }

                if (lcb.getCrds() != null && !lcb.getCrds().isEmpty()) {
                    listCardBoard.addListCards(lcb.getCrds());
                }
            }
        }
    }

    /**
     * Create / Update a board
     * @param lcb lcb
     */
    public ListCardBoard addListCardBoard(ListCardBoard lcb) {
        ListCardBoard existing = findListCardBoard(lcb.getCd());

        if (existing != null) {
            // update name only
            existing.setNm(lcb.getNm());

            return existing;
        } else {
            // ok creating board
            lcb.setDt(new Date());

            if (this.brds == null) {
                this.brds = new ArrayList<ListCardBoard>();
            }

            this.brds.add(lcb);
        }

        return lcb;
    }

    /**
     * If the board specified doesn't exist, we go ahead and add it to the default board
     * @param boardCode boardCode
     * @param lc lc
     */
    public void addListCard(String boardCode, ListCard lc) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb == null) {
            lcb = findListCardBoard(ApplicationConstants.DEFAULT_BOARD_NAME);

            if (lcb == null) {
                lcb = new ListCardBoard(ApplicationConstants.DEFAULT_BOARD_NAME);
                lcb.setCd(lcb.getNm());

                if (this.brds == null) {
                    this.brds = new ArrayList<ListCardBoard>();
                }

                this.brds.add(lcb);
            }
        }

        lcb.addListCard(lc);
    }

    public void addUserAnswers(List<UserAnswer> list) {
        if (this.nswrs == null) {
            this.nswrs = new ArrayList<UserAnswer>();
        }

        List<UserAnswer> dupes = null;

        for (UserAnswer ua : list) {
            // check to see if user has already answered this question
            for (UserAnswer answer : this.nswrs) {
                if (StringUtils.equals(ua.getQstn().getRlnm(), answer.getQstn().getRlnm())) {
                    if (dupes == null) {
                        dupes = new ArrayList<UserAnswer>();
                    }

                    dupes.add(answer);
                }
            }
        }

        if (dupes != null) {
            this.nswrs.removeAll(dupes);
        }

        this.nswrs.addAll(list);
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public Integer findListCardMaxOrder(String boardCode) {
        Integer result = 0;

        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            result = lcb.findListCardMaxOrder();
        }

        return result;
    }

    public ProfileStatus getProfileStatus() {
        ProfileStatus result = ProfileStatus.MISSING;
        Motivator motivator = getMotivator();

        if (motivator != null) {
            if (MotivatorSource.QUIZ.equals(motivator.getTp()) || ((MotivatorSource.FACEBOOK.equals(motivator.getTp()) && motivator.hasStyleMotivators()))) {
                result = ProfileStatus.FULL;
            } else if (MotivatorSource.FACEBOOK.equals(motivator.getTp()) && !motivator.hasStyleMotivators()) {
                result = ProfileStatus.PARTIAL;
            }
        }

        return result;
    }

    public Motivator getMotivator() {
        Motivator result = null;

        MotivatorSource source = whichMotivatorSourceShouldUserUse(this.mtvtrmp);

        // the first conditional returns the motivator set that is the most complete
        if (source != null) {

            switch (source) {
                case QUIZ:
                    result = this.mtvtrmp.containsKey(MotivatorSource.QUIZ) ? this.mtvtrmp.get(MotivatorSource.QUIZ) : null;
                    break;
                case FACEBOOK:
                    result = this.mtvtrmp.containsKey(MotivatorSource.FACEBOOK) ? this.mtvtrmp.get(MotivatorSource.FACEBOOK) : null;
                    if (result != null) {
                        // Merge quiz based motivators not captured by Facebook
                        result = mergeQuizMotivators(result, this.mtvtrmp.get(MotivatorSource.QUIZ));
                    }
                    break;
            }
        }

        return result;
    }

    private Motivator mergeQuizMotivators(Motivator result, Motivator quiz) {
        result = new Motivator(result);
        if (quiz != null) {
            for (String key : quiz.getMtvtrs().keySet()) {
                if (!result.getMtvtrs().containsKey(key)) {
                    result.getMtvtrs().put(key, quiz.getMtvtrs().get(key));
                }
            }
        }

        return result;
    }

    private MotivatorSource whichMotivatorSourceShouldUserUse(Map<MotivatorSource, Motivator> map) {
        MotivatorSource result = null;

        if (map != null) {
            if (map.containsKey(MotivatorSource.QUIZ) && isQuizMotivatorQualityOk(map.get(MotivatorSource.QUIZ))) {
                result = MotivatorSource.QUIZ;
            } else if (map.containsKey(MotivatorSource.FACEBOOK) && isFacebookMotivatorQualityOk(map.get(MotivatorSource.FACEBOOK))) {
                result = MotivatorSource.FACEBOOK;
            }
        }

        return result;
    }

    private boolean isQuizMotivatorQualityOk(Motivator motivator) {
        boolean result = false;

        if (motivator != null && motivator.getMtvtrs() != null && !motivator.getMtvtrs().isEmpty()) {
            Map<String, Integer> mtvtrs = motivator.getMtvtrs();
            Integer motivatorA = 0;
            Integer motivatorB = 0;
            Integer motivatorC = 0;
            Integer motivatorD = 0;
            Integer motivatorE = 0;
            Integer motivatorF = 0;
            Integer motivatorG = 0;
            // but that's not enough - if the user only has style motivators
            // we don't recommend any products as we don't have enough information yet
            if (mtvtrs.containsKey("A")) {
                motivatorA = mtvtrs.get("A");
            }
            if (mtvtrs.containsKey("B")) {
                motivatorB = mtvtrs.get("B");
            }
            if (mtvtrs.containsKey("C")) {
                motivatorC = mtvtrs.get("C");
            }
            if (mtvtrs.containsKey("D")) {
                motivatorD = mtvtrs.get("D");
            }
            if (mtvtrs.containsKey("E")) {
                motivatorE = mtvtrs.get("E");
            }
            if (mtvtrs.containsKey("F")) {
                motivatorF = mtvtrs.get("F");
            }
            if (mtvtrs.containsKey("G")) {
                motivatorG = mtvtrs.get("G");
            }

            // for the quiz scores to be good enough to calculate motivator scores
            // we need these motivators to be greater than 0
            if (motivatorA > 0 && motivatorB > 0 && motivatorC > 0 &&
                    motivatorD > 0 && motivatorE > 0 && motivatorF > 0 && motivatorG > 0) {
                result = true;
            }
        }

        return result;
    }

    private boolean isFacebookMotivatorQualityOk(Motivator motivator) {
        boolean result = false;

        if (motivator != null && motivator.getMtvtrs() != null && !motivator.getMtvtrs().isEmpty()) {
            Map<String, Integer> mtvtrs = motivator.getMtvtrs();
            Integer motivatorA = 0;
            Integer motivatorB = 0;
            Integer motivatorC = 0;
            Integer motivatorD = 0;
            Integer motivatorE = 0;
            Integer motivatorF = 0;

            // but that's not enough - if the user only has style motivators
            // we don't recommend any products as we don't have enough information yet
            if (mtvtrs.containsKey("A")) {
                motivatorA = mtvtrs.get("A");
            }
            if (mtvtrs.containsKey("B")) {
                motivatorB = mtvtrs.get("B");
            }
            if (mtvtrs.containsKey("C")) {
                motivatorC = mtvtrs.get("C");
            }
            if (mtvtrs.containsKey("D")) {
                motivatorD = mtvtrs.get("D");
            }
            if (mtvtrs.containsKey("E")) {
                motivatorE = mtvtrs.get("E");
            }
            if (mtvtrs.containsKey("F")) {
                motivatorF = mtvtrs.get("F");
            }

            // for the facebook scores to be good enough to calculate motivator scores
            // we need these motivators to be greater than 0 and specifically B needs to be greater than 1
            if (motivatorA > 0 && motivatorB > 1 && motivatorC > 0 &&
                    motivatorD > 0 && motivatorE > 0 && motivatorF > 0) {
                result = true;
            }
        }

        return result;
    }

    public List<Coupon> findCouponsByOfferUrlName(String offerUrlName) {
        List<Coupon> result = null;

        if (this.cpns != null && !this.cpns.isEmpty()) {
            result = new ArrayList<Coupon>();
            for (Coupon existingCoupon : this.cpns) {
                if (StringUtils.equals(existingCoupon.getFfrrlnm(), offerUrlName)) {
                    result.add(existingCoupon);
                }
            }
        }

        return result;
    }

    public void addReward(Reward reward) {
        if (this.rwrds == null) {
            this.rwrds = new ArrayList<Reward>();
        }

        this.rwrds.add(reward);
    }

    /**
     * This replaces a motivator if type already exists
     *
     * @param motivator motivator
     */
    public void addMotivator(Motivator motivator) {
        if (motivator != null && motivator.getTp() != null && motivator.getMtvtrs() != null) {
            if (mtvtrmp == null) {
                mtvtrmp = new HashMap<MotivatorSource, Motivator>();
            }

            if (mtvtrmp.containsKey(motivator.getTp())) {
                mtvtrmp.remove(motivator.getTp());
            }

            mtvtrmp.put(motivator.getTp(), motivator);
        }
    }

    public void addNote(String boardCode, String urlName, Note note) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.addNote(urlName, note);
        }
    }

    public void addComment(String boardCode, String urlName, Comment comment) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.addComment(urlName, comment);
        }
    }

    public void removeNote(String boardCode, String urlName, String noteCode) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.removeNote(urlName, noteCode);
        }
    }

    public void removeComment(String boardCode, String urlName, String commentCode) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.removeComment(urlName, commentCode);
        }
    }

    public void addReview(String boardCode, String urlName, Review review) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.addReview(urlName, review);
        }
    }

    public void removeReview(String boardCode, String urlName, String reviewCode) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.removeReview(urlName, reviewCode);
        }
    }

    public void removeAlert(String boardCode, String urlName) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.removeAlert(urlName);
        }
    }

    public void removePicture(String boardCode, String urlName, String pictureCode) {
        ListCardBoard lcb = findListCardBoard(boardCode);

        if (lcb != null) {
            lcb.removePicture(urlName, pictureCode);
        }
    }

    // convenience method to return the number of stores the user has in her lela list
    public Integer getStoreCount() {
        return getListCardTypeCount(ListCardType.STORE);
    }

    // convenience method to return the number of items the user has in her lela list
    public Integer getItemCount() {
        return getListCardTypeCount(ListCardType.ITEM);
    }

    // convenience method to return the number of externals the user has in her lela list
    public Integer getExternalCount() {
        return getListCardTypeCount(ListCardType.EXTERNAL);
    }

    // convenience method to return the number of externals the user has in her lela list
    public Integer getOwnerCount() {
        return getListCardTypeCount(ListCardType.OWNER);
    }

    // convenience method to return the number of externals the user has in her lela list
    public Integer getBranchCount() {
        return getListCardTypeCount(ListCardType.BRANCH);
    }

    // convenience method to return the number of externals the user has in her lela list
    public Integer getListCardCount() {
        int result = 0;
        if (this.brds != null && !this.brds.isEmpty()) {
            for (ListCardBoard lc : this.brds) {
                if (lc.getCrds() != null && !lc.getCrds().isEmpty()) {
                    result += lc.getCrds().size();
                }
            }
        }

        return result;
    }

    public Integer getListCardTypeCount(ListCardType type) {
        int result = 0;
        if (this.brds != null && !this.brds.isEmpty()) {
            for (ListCardBoard lc : this.brds) {
                result += lc.getListCardTypeCount(type);
            }
        }

        return result;
    }

    public Map<String, ListCard> getSavedItems() {
        return getAllListCardsAsMap(ListCardType.ITEM);
    }

    public Map<String, ListCard> getSavedStores() {
        return getAllListCardsAsMap(ListCardType.STORE);
    }

    public Map<String, ListCard> getSavedOwners() {
        return getAllListCardsAsMap(ListCardType.OWNER);
    }

    public Map<String, ListCard> getSavedBranches() {
        return getAllListCardsAsMap(ListCardType.BRANCH);
    }

    private Map<String, ListCard> getAllListCardsAsMap(ListCardType type) {
        Map<String, ListCard> result = new HashMap<String, ListCard>();

        if (brds != null && !brds.isEmpty()) {
            for (ListCardBoard lcb : brds) {
                List<ListCard> cards = lcb.findListCardsByType(type);
                if (cards != null && !cards.isEmpty()) {
                    for (ListCard lc : lcb.getCrds()) {
                        result.put(lc.getRlnm(), lc);
                    }
                }
            }
        }

        return result;
    }

    private Long getAllListCardCount(ListCardType type) {
        Long result = 0L;

        if (brds != null && !brds.isEmpty()) {
            for (ListCardBoard lcb : brds) {
                List<ListCard> cards = lcb.findListCardsByType(type);
                if (cards != null && !cards.isEmpty()) {
                    for (ListCard lc : lcb.getCrds()) {
                        result++;
                    }
                }
            }
        }

        return result;
    }

    public List<ListCardBoardName> findListCardBoardNames() {
        List<ListCardBoardName> result = null;

        if (this.brds != null && !this.brds.isEmpty()) {
            result = new ArrayList<ListCardBoardName>(this.brds.size());
            for (ListCardBoard board : this.brds) {
                result.add(new ListCardBoardName(board.getCd(), board.getNm()));
            }
        }

        return result;
    }

    public void addAddress(Address address) {
        if (this.ddrss == null) {
            this.ddrss = new HashMap<AddressType, Address>();
        }

        if (address.getTp() == null) {
            throw new IllegalArgumentException("Address must have a type defined");
        }

        this.ddrss.put(address.getTp(), address);
    }

    public void removeListCardBoard(String boardCode) {
        ListCardBoard toRemove = null;

        if (this.brds != null && !this.brds.isEmpty()) {
            for (ListCardBoard board : this.brds) {
                if (StringUtils.equals(boardCode, board.getCd())) {
                    toRemove = board;
                    break;
                }
            }
        }

        if (toRemove != null) {
            this.brds.remove(toRemove);
        }
    }

    public Boolean getRegistered() {
        return StringUtils.isNotBlank(this.ml);
    }

    public Boolean getRegisteredViaFacebook() {
        return getRegistered() && getFacebook();
    }

    public Boolean getRegisteredViaFacebookNeedsFullQuiz() {
        return getRegisteredViaFacebook() && getMotivator() != null
                && getMotivator().getTp().equals(MotivatorSource.FACEBOOK)
                && getMotivator().getMtvtrs().get("B") < 1;
    }

    public Boolean getRegisteredViaFacebookNeedsPartialQuiz() {
        return getRegisteredViaFacebook() && getMotivator() != null
                && getMotivator().getTp().equals(MotivatorSource.FACEBOOK)
                && getMotivator().getMtvtrs().get("B") > 1
                && !getMotivator().hasStyleMotivators();
    }

    public Boolean getProfileComplete() {

        return getRegistered() &&
                getMotivator() != null &&
                getMotivator().hasStyleMotivators();
    }

    public Boolean getRegisteredNoQuiz() {
        return getRegistered() && getMotivator() == null;
    }


}
