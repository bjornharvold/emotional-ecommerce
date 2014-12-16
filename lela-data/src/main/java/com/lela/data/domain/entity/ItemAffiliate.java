package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_affiliate")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemAffiliateId", columnDefinition = "int(11)"))
public class ItemAffiliate extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "UniqueItemLelaId", nullable = false, referencedColumnName = "UniqueItemId")
    @ForeignKey(name = "FK_item_affiliate_item_lela")
    @NotNull
    private Item uniqueItemLelaId;

    @ManyToOne
    @JoinColumn(name = "UniqueItemAffiliateId", nullable = false, referencedColumnName = "UniqueItemId")
    @ForeignKey(name = "FK_item_affiliate_item_affiliate")
    @NotNull
    private Item uniqueItemAffiliateId;

    @ManyToOne
    @JoinColumn(name = "AffiliateId", nullable = false, referencedColumnName = "AffiliateId")
    @ForeignKey(name = "FK_item_affiliate_affiliate")
    @NotNull
    private Affiliate affiliate;
}
