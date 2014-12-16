package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "MerchantCategoryId", columnDefinition = "int(11)"))
@Table(name = "merchant_category", uniqueConstraints = {@UniqueConstraint(name = "UK_merchant_category", columnNames={"MerchantId", "CategoryId"})})
public class MerchantCategory extends AbstractEntity{

    @NotNull
    @ManyToOne
    @JoinColumn(name = "MerchantId", nullable = false)
    @ForeignKey(name = "FK_merchant_category_merchant")
    private Merchant merchant;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_merchant_category_category")
    private Category category;

    @Column(name = "EnableOffers", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean enableOffers;


}
