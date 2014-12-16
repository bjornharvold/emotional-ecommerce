package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_review")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemReviewId", columnDefinition = "int(11)"))
public class ItemReview extends AbstractEntity {

    @NotNull
    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_review_item")
    Item item;

    @NotNull
    @ManyToOne(targetEntity = ReviewProvider.class)
    @JoinColumn(name = "ReviewProviderId", nullable = false)
    @ForeignKey(name = "FK_item_review_review_provider")
    ReviewProvider reviewProvider;

    @NotNull
    @ManyToOne(targetEntity = ReviewSource.class)
    @JoinColumn(name = "ReviewSourceId", nullable = false)
    @ForeignKey(name = "FK_item_review_review_source")
    ReviewSource reviewSource;

    @NotNull
    @Column(name="ReviewProviderInternalId", nullable = false)
    String reviewProviderInternalId;

    @Column(name="ReviewTitle", length = 1024)
    String reviewTitle;

    @Column(name="ReviewLanguage")
    String reviewLanguage;

    @Column(name="ReviewScore")
    Integer reviewScore;

    @Column(name="ReviewAuthor")
    String reviewAuthor;

    @Column(name="ReviewSummary", length = 1024)
    String reviewSummary;

    @Column(name="ReviewVerdict", length = 1024)
    String reviewVerdict;

    @Column(name="ReviewURL")
    String reviewUrl;

    @Column(name="TestDate")
    Date testDate;

    @Column(name="TestPros", length = 1024)
    String testPros;

    @Column(name="TestCons", length = 1024)
    String testCons;

    @Column(name="Award")
    String award;

    @Column(name="AwardImage")
    String awardImage;
}
