/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/8/12
 * Time: 4:39 PM
 * Responsibility:
 */
@Document
public class Press extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -2511561720142896190L;

    /** Identifiable name */
    private String nm;

    /** Url name */
    @Indexed
    private String rlnm;

    /** SEO friendly url name */
    private String srlnm;

    /** Press images */
    private List<PressImage> mgs;

    /** Testimonials */
    private List<Testimonial> tstmnls;

    /** Tweets */
    private List<Tweet> twts;

    public Press() {
    }

    public Press(Press press) {
        setId(press.getId());
        setCdt(press.getCdt());
        setLdt(press.getLdt());

        this.nm = press.getNm();
        this.rlnm = press.getRlnm();
        this.srlnm = press.getSrlnm();

        if (press.getTwts() != null && !press.getTwts().isEmpty()) {
            this.twts = new ArrayList<Tweet>(press.getTwts().size());
            for (Tweet tweet : press.getTwts()) {
                this.twts.add(new Tweet(tweet));
            }
        }

        if (press.getTstmnls() != null && !press.getTstmnls().isEmpty()) {
            this.tstmnls = new ArrayList<Testimonial>(press.getTstmnls().size());
            for (Testimonial testimonial : press.getTstmnls()) {
                this.tstmnls.add(new Testimonial(testimonial));
            }
        }

        if (press.getMgs() != null && !press.getMgs().isEmpty()) {
            this.mgs = new ArrayList<PressImage>(press.getMgs().size());
            for (PressImage mg : mgs) {
                this.mgs.add(new PressImage(mg));
            }
        }

        this.mgs = press.getMgs();
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public List<PressImage> getMgs() {
        return mgs;
    }

    public void setMgs(List<PressImage> mgs) {
        this.mgs = mgs;
    }

    public List<Testimonial> getTstmnls() {
        return tstmnls;
    }

    public void setTstmnls(List<Testimonial> tstmnls) {
        this.tstmnls = tstmnls;
    }

    public List<Tweet> getTwts() {
        return twts;
    }

    public void setTwts(List<Tweet> twts) {
        this.twts = twts;
    }

    public void addPressImage(PressImage pressImage) {
        if (this.mgs == null) {
            this.mgs = new ArrayList<PressImage>();
        }

        PressImage dupe = null;

        for (PressImage mg : mgs) {
            if (StringUtils.equals(mg.getMgid(), pressImage.getMgid())) {
                dupe = mg;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.mgs.remove(dupe);
        }

        this.mgs.add(pressImage);
    }

    public void addTestimonial(Testimonial testimonial) {
        if (this.tstmnls == null) {
            this.tstmnls = new ArrayList<Testimonial>();
        }

        Testimonial dupe = null;

        for (Testimonial mg : tstmnls) {
            if (StringUtils.equals(mg.getTid(), testimonial.getTid())) {
                dupe = mg;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.tstmnls.remove(dupe);
        }

        this.tstmnls.add(testimonial);
    }

    public void addTweet(Tweet tweet) {
        if (this.twts == null) {
            this.twts = new ArrayList<Tweet>();
        }

        Tweet dupe = null;

        for (Tweet mg : twts) {
            if (StringUtils.equals(mg.getTid(), tweet.getTid())) {
                dupe = mg;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.twts.remove(dupe);
        }

        this.twts.add(tweet);
    }
}
