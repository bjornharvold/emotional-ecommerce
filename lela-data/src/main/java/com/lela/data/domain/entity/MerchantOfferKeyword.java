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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table="merchant_offer_keyword")
@AttributeOverride(name = "id", column = @Column(name = "MerchantOfferKeywordId", columnDefinition = "int(11)"))
@RooSerializable
public class MerchantOfferKeyword extends AbstractEntity{


    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_merchant_offer_keyword_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "ActionTypeId", nullable = false)
    @ForeignKey(name = "FK_action_type_category")
    private ActionType actionType;

    @NotNull
    @Column(name="OfferKeyword")
    private String offerKeyword;

}
