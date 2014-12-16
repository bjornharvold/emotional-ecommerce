package com.lela.data.domain.entity;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@AttributeOverride(name = "id", column = @Column(name = "ProductImageId", columnDefinition = "int(11)"))
@Table(name = "product_image", uniqueConstraints = {@UniqueConstraint(name = "UK_product_image", columnNames={"SourceId", "SourceKey", "ImageUrl"})})
@RooSerializable
public class ProductImage extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "SourceId")
    @ForeignKey(name = "FK_product_image_image_source")
    private ImageSource imageSource;

    /*
    @ManyToOne
    @JoinColumn(name = "SourceTypeId")
    @ForeignKey(name = "FK_product_image_merchant_source_type")
    private MerchantSourceType merchantSourceType;
    */

    /*
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    @ForeignKey(name = "FK_product_image_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "ItemId")
    @ForeignKey(name = "FK_product_image_item")
    private Item item;
    */
    /*
    @ManyToOne
    @JoinColumn(name = "MerchantId")
    @ForeignKey(name = "FK_merchant_image_merchant")
    private Merchant merchant;
    */

    @Column(name = "SourceKey", length = 255)
    private String sourceKey;

    @Column(name = "UploadReason", length = 255)
    private String uploadReason;

    /*
    @Column(name = "MerchantName", length = 255)
    private String merchantName;
    */

    @Column(name = "ImageUrl", length = 255)
    private String imageUrl;

    @Column(name = "ResizedUrl", length = 512)
    private String resizedUrl;

    @Column(name = "ImageDate", length = 255)
    private String imageDate;

    @Column(name = "RValue")
    private Integer rValue;

    @Column(name = "GValue")
    private Integer gValue;

    @Column(name = "BValue")
    private Integer bValue;

    @Column(name = "HexValue")
    private String hexValue;

    @Column(name = "ImageURLSmall", length = 800)
    private String imageURLSmall;

    @Column(name = "ImageURLMedium", length = 800)
    private String imageURLMedium;

    @Column(name = "ImageURLLarge", length = 800)
    private String imageURLLarge;

    @Column(name = "ImageURLScaled", length = 800)
    private String imageURLScaled;

    @Column(name = "DoNotUse", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean doNotUse;

    @Column(name = "ImageAngle", length=200)
    private String imageAngle;

    @Column(name = "ImageType", length=200)
    private String imageType;

    @Column(name = "Resolution", length=200)
    private String resolution;

    @Column(name = "Preferred", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean preferred;

    @Column(name = "ImageHeight")
    private Integer imageHeight;

    @Column(name = "ImageWidth")
    private Integer imageWidth;

}
