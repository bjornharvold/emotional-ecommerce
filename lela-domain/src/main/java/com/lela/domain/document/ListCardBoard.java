/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.lela.domain.enums.list.ListCardType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/10/12
 * Time: 12:44 PM
 * Responsibility:
 */
public class ListCardBoard implements Serializable {
    private static final long serialVersionUID = 9108916159120047468L;

    /** Board name */
    @NotNull
    @NotEmpty
    private String nm;

    /** Creation date */
    private Date dt;

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    /**
     * Checksum that we can use to uniquely identify object
     */
    private String cd = uuidGenerator.generate().toString();

    /**
     * Cards
     */
    private List<ListCard> crds;

    public ListCardBoard() {
    }

    public ListCardBoard(String nm) {
        this.nm = nm;
        this.dt = new Date();
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public List<ListCard> getCrds() {
        return crds;
    }

    public void setCrds(List<ListCard> crds) {
        this.crds = crds;
    }

    public ListCard findListCard(String urlName) {

        if (this.crds != null && !this.crds.isEmpty()) {
            for (ListCard lc : this.crds) {
                if (StringUtils.equals(lc.getRlnm(), urlName)) {
                    return lc;
                }
            }
        }

        return null;
    }

    public void addListCards(List<ListCard> list) {
        if (this.crds == null) {
            this.crds = new ArrayList<ListCard>();
        }

        List<ListCard> toRemove = null;

        if (list != null && !list.isEmpty()) {
            for (ListCard lc : list) {
                // don't add duplicates
                if (findListCard(lc.getRlnm()) != null) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<ListCard>();
                    }

                    toRemove.add(lc);
                } else {
                    // set creation date
                    lc.setDt(new Date());
                }
            }

            if (toRemove != null) {
                list.removeAll(toRemove);
            }
        }

        if (list != null && !list.isEmpty()) {
            this.crds.addAll(list);
        }
    }

    public void addListCard(ListCard ss) {
        if (this.crds == null) {
            this.crds = new ArrayList<ListCard>();
        }

        removeListCard(ss.getRlnm());

        ss.setDt(new Date());
        this.crds.add(ss);
    }

    public void removeListCard(String urlName) {

        if (this.crds != null && !this.crds.isEmpty()) {
            ListCard toRemove = null;
            for (ListCard lc : this.crds) {

                if (StringUtils.equals(lc.getRlnm(), urlName)) {
                    toRemove = lc;
                    break;
                }

            }

            if (toRemove != null) {
                this.crds.remove(toRemove);
            }

        }

    }

    public Integer findListCardMaxOrder() {
        Integer result = 0;

        if ((this.crds != null) && !this.crds.isEmpty()) {
            for (ListCard item : this.crds) {
                if (item.getRdr() > result) {
                    result = item.getRdr();
                }
            }
        }

        return result;
    }

    public void addNote(String urlName, Note note) {

        ListCard lc = findListCard(urlName);

        if (lc != null) {
            if (lc.getNts() == null) {
                lc.setNts(new ArrayList<Note>());
            }

            // if this is an existing note, we just remove it
            Note exsistingNote = lc.getNote(note.getCd());
            if (exsistingNote != null) {
                lc.getNts().remove(exsistingNote);
            }

            note.setDt(new Date());
            lc.getNts().add(note);
        }
    }

    public void removeNote(String urlName, String noteCode) {

        ListCard lc = findListCard(urlName);

        if (lc != null) {

            // if this is an existing note, we just remove it
            Note exsistingNote = lc.getNote(noteCode);
            if (exsistingNote != null) {
                lc.getNts().remove(exsistingNote);
            }

        }
    }

    public void addAlert(String urlName, Alert alert) {

        ListCard lc = findListCard(urlName);

        if (lc != null) {
            alert.setDt(new Date());
            lc.setLrt(alert);
        }
    }

    public void removeAlert(String urlName) {

        ListCard lc = findListCard(urlName);

        if (lc != null) {
            // if this is an existing alert, we just remove it
            lc.setLrt(null);
        }
    }

    public void addReview(String urlName, Review review) {
        ListCard lc = findListCard(urlName);

        if (lc != null) {
            if (lc.getRvws() == null) {
                lc.setRvws(new ArrayList<Review>());
            }

            // if this is an existing review, we just remove it
            Review exsistingReview = lc.getReview(review.getCd());
            if (exsistingReview != null) {
                lc.getRvws().remove(exsistingReview);
            }

            review.setDt(new Date());
            lc.getRvws().add(review);
        }
    }

    public void removeReview(String urlName, String reviewCode) {
        ListCard lc = findListCard(urlName);

        if (lc != null) {

            // if this is an existing review, we just remove it
            Review exsistingReview = lc.getReview(reviewCode);
            if (exsistingReview != null) {
                lc.getRvws().remove(exsistingReview);
            }

        }
    }

    public void addPicture(String urlName, Picture picture) {
        ListCard lc = findListCard(urlName);

        if (lc != null) {
            if (lc.getPctrs() == null) {
                lc.setPctrs(new ArrayList<Picture>());
            }

            // if this is an existing picture, we just remove it
            Picture exsistingPicture = lc.getPicture(picture.getCd());
            if (exsistingPicture != null) {
                lc.getPctrs().remove(exsistingPicture);
            }

            picture.setDt(new Date());
            lc.getPctrs().add(picture);
        }
    }

    public void addComment(String urlName, Comment comment) {
        ListCard lc = findListCard(urlName);

        if (lc != null) {
            if (lc.getCmmnts() == null) {
                lc.setCmmnts(new ArrayList<Comment>());
            }

            // if this is an existing comment, we just remove it
            Comment exsistingComment = lc.getComment(comment.getCd());
            if (exsistingComment != null) {
                lc.getCmmnts().remove(exsistingComment);
            }

            comment.setDt(new Date());
            lc.getCmmnts().add(comment);
        }
    }

    public void removePicture(String urlName, String pictureCode) {
        ListCard lc = findListCard(urlName);

        if (lc != null) {

            // if this is an existing picture, we just remove it
            Picture exsistingPicture = lc.getPicture(pictureCode);
            if (exsistingPicture != null) {
                lc.getPctrs().remove(exsistingPicture);
            }

        }
    }

    public void removeComment(String urlName, String commentCode) {
        ListCard lc = findListCard(urlName);

        if (lc != null) {

            // if this is an existing comment, we just remove it
            Comment exsistingComment = lc.getComment(commentCode);
            if (exsistingComment != null) {
                lc.getCmmnts().remove(exsistingComment);
            }

        }
    }

    public Integer getListCardTypeCount(ListCardType type) {
        int result = 0;
        if (this.crds != null && !this.crds.isEmpty()) {
            for (ListCard lc : this.crds) {
                if (lc.getTp().equals(type)) {
                    result++;
                }
            }
        }

        return result;
    }

    public List<ListCard> findListCardsByType(ListCardType type) {
        List<ListCard> result = null;

        if (this.crds != null && !this.crds.isEmpty()) {
            for (ListCard card : this.crds) {
                if (card.getTp().equals(type)) {
                    if (result == null) {
                        result = new ArrayList<ListCard>();
                    }
                    result.add(card);
                }
            }
        }

        return result;
    }

    public List<ListCard> findListCardsByType(ListCardType type, String urlName) {
        List<ListCard> result = null;

        if (this.crds != null && !this.crds.isEmpty()) {
            for (ListCard card : this.crds) {
                if (card.getTp().equals(type) && StringUtils.equals(card.getRlnm(), urlName)) {
                    if (result == null) {
                        result = new ArrayList<ListCard>();
                    }
                    result.add(card);
                }
            }
        }

        return result;
    }
}
