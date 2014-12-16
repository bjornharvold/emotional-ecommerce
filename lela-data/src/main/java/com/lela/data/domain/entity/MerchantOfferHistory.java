package com.lela.data.domain.entity;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "merchant_offer_history")
@AttributeOverride(name = "id", column = @Column(name = "MerchnatOfferHistoryId", columnDefinition = "int(11)"))
@RooSerializable
public class MerchantOfferHistory extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "UniqueItemId")
    @ForeignKey(name = "FK_merchant_offer_history_item")
    private Item item;

    @Column(name = "IsAvailable")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "MerchantId")
    @ForeignKey(name = "FK_merchant_offer_history_merchant")
    private Merchant merchant;

    @Column(name = "OfferItemName", length = 500)
    private String offerItemName;

    @Column(name = "OfferPrice", length = 18)
    private String offerPrice;

    @Column(name = "OfferSalePrice", length = 18)
    private String offerSalePrice;

    @Column(name = "StartDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date startDate;

    @Column(name = "EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date endDate;

    @Column(name="OfferId")
    private Integer offerId;

    @Column(name="SourceId")
    private String sourceId;

    @Column(name="SourceKey")
    private String sourceKey;

    @Column(name="SourceTypeId")
    private String sourceTypeId;
}
