package com.lela.data.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table = "affiliate_transaction")
@AttributeOverride(name = "id", column = @Column(name = "AffiliateTransactionId", columnDefinition = "int(11)"))
public class AffiliateTransaction extends AbstractEntity{

    @Column(name = "network")
    private String network;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "transactionDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date transactionDate;

    @Column(name = "ReferralDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date referralDate;

    @Column(name = "AdvertiserName")
    private String advertiserName;

    @Column(name = "SalesAmount")
    private BigDecimal salesAmount;

    @Column(name = "CommissionAmount")
    private BigDecimal commissionAmount;


    @Column(name = "ProcessDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date processDate;

    @Column(name = "RevenueId")
    private Long revenueId;

    @Column(name = "ProviderId")
    private Integer providerId;

    @Column(name = "Provider")
    private String provider;

    @Column(name = "OrganizationId")
    private Integer organizationId;

    @Column(name = "EventDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date eventDate;

    @Column(name = "ClickDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date clickDate;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "ProductCategory")
    private String productCategory;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "SubId")
    private String subId;

    @Column(name = "TrackingId")
    private String trackingId;

    @Column(name = "ReferringProductId")
    private String referringProductId;

    @Column(name = "RedirectId")
    private String redirectId;

    @Column(name = "UserId")
    private String userId;

    @Column(name="FirstName")
    private String firstName;

    @Column(name="LastName")
    private String lastName;

    @Column(name="Association")
    private String association;

    @Column(name="Email")
    private String email;

    @Column(name = "redirectUrl", length=512)
    private String redirectUrl;

    @Column(name = "ProductId")
    private String productId;

    @Column(name = "SyncdMongo", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean syncdMongo;

    /*
    @Column(name = "AdvertiserId")
    private Long advertiserId;

    @Column(name = "WebsiteId")
    private Long websiteId;

    @Column(name = "CId")
    private Long cId;

    @Column(name = "OrderDiscount")
    private BigDecimal orderDiscount;

    @Column(name = "CommissionId")
    private Long commissionId;

    @Column(name = "PublisherCommission")
    private Long publisherCommission;

    @Column(name = "Status")
    private String status;

    @Column(name = "ActionType")
    private String actionType;

    @Column(name = "ActionTrackerId")
    private String actionTrackerId;

    @Column(name = "ActionTrackerName")
    private String actionTrackerName;

    @Column(name="aid")
    private String aid;
    */
    //commissionid
    //country
    //lockingdate
    //original
    //originalactionid
    //categoryid
    //avgitemvalue
    //subtype
    //linktype
    //productlinkconversions
    //productlinkclicks
    //itemsorderedthroughproductlinks
    //allotheritemsordered
    //advertisingfeerate


    @ManyToOne
    @JoinColumn(name = "AffiliateReportId", nullable = false)
    @ForeignKey(name = "FK_affiliate_transaction_affiliate_report")
    private AffiliateReport affiliateReport;
}
