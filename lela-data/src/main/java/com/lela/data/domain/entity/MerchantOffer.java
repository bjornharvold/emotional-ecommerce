package com.lela.data.domain.entity;

import com.lela.data.enums.ProductImageItemStatuses;
import com.lela.data.enums.ReviewStatuses;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "merchant_offer")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "OfferId", columnDefinition = "int(11)"))
public class MerchantOffer extends AbstractEntity {

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "SourceId", nullable = false)
    @ForeignKey(name = "FK_merchant_offer_merchant_source")
    private MerchantSource merchantSource;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "SourceTypeId", nullable = true)
    @ForeignKey(name = "FK_merchant_offer_merchant_source_type")
    private MerchantSourceType merchantSourceType;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "ConditionTypeId", nullable= false )
    @ForeignKey(name = "FK_merchant_offer_condition_type")
    private ConditionType conditionType;

    @ManyToOne
    @JoinColumn(name = "ReviewStatusId", nullable= false)
    @ForeignKey(name = "FK_merchant_offer_review_status")
    private ReviewStatus reviewStatus;

    @ManyToOne
    @JoinColumn(name = "UniqueItemId", nullable = true)
    @ForeignKey(name = "FK_merchant_offer_item_UniqueItemId")
    private Item item;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "MerchantId", nullable = true)
    @ForeignKey(name = "FK_merchant_offer_merchant")
    private Merchant merchant;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "BrandId", nullable = true)
    @ForeignKey(name = "FK_merchant_offer_brand")
    private Brand brand;

    @Column(name = "SourceKey", length = 255)
    private String sourceKey;

    @Column(name = "MerchantName", length = 255)
    private String merchantName;

    @Column(name = "OfferItemName", length = 500)
    private String offerItemName;

    @Column(name = "ModelNumber", length = 255)
    private String modelNumber;

    @Column(name="UPC", length = 25)
    private String upc;

    @Column(name="Color", length = 255)
    private String color;

    @Column(name = "OfferUrl", length = 512)
    private String offerUrl;

    @Column(name = "OfferPrice", length = 18)
    @Size( max = 18)
    private String offerPrice;

    @Column(name = "OfferSalePrice", length = 18)
    @Size( max=18 )
    private String offerSalePrice;

    @Column(name = "OfferDate", length = 255)
    private String offerDate;

    @Column(name = "IsAvailable", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean available;

    @Column(name = "LoadDateId")
    private Integer loadDateId;

    @Column(name = "DoNotUse", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean doNotUse;

    @Column(name = "UseThisOffer", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    @NotNull
    private Boolean useThisOffer;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    @NotNull
    private Boolean dirty;

    @Override
    public void prePersist() {
        this.brand = item.getBrand();
        this.merchantSourceType = merchantSource.getMerchantSourceType();
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        this.brand = item.getBrand();
        this.merchantSourceType = merchantSource.getMerchantSourceType();
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static long countValidMerchantOfferByItem(Item item) {
        Query query = entityManager().createQuery("SELECT COUNT(o) FROM MerchantOffer o where item = :item and reviewStatus = :passed and available = :available and doNotUse = :doNotUse").setParameter("item", item).setParameter("passed", ReviewStatuses.PASSED.getReviewStatus()).setParameter("available", Boolean.TRUE).setParameter("doNotUse", Boolean.FALSE);
        return (Long)query.getSingleResult();
    }

    public static long countMerchantOfferByItem(Item item) {
        Query query = entityManager().createQuery("SELECT COUNT(o) FROM MerchantOffer o where item = :item").setParameter("item", item);
        return (Long)query.getSingleResult();
    }

}
