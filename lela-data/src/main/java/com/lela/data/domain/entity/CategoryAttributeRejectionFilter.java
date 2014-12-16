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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "category_attribute_rejection_filter")
@AttributeOverride(name = "id", column = @Column(name = "CategoryAttributeRejectionFilterId", columnDefinition = "int(11)"))
@RooSerializable
public class CategoryAttributeRejectionFilter extends AbstractEntity {

    @NotNull
    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_attribute_rejection_filter_category")
    private Category category;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_attribute_rejection_filter_attribute_type")
    private AttributeType attributeType;

    @NotNull
    @Column(name = "AttributeValue", nullable = false)
    private String attributeValue;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "RejectionReasonId", nullable = false)
    @ForeignKey(name = "FK_attribute_rejection_filter_rejection_reason")
    private RejectionReason rejectionReason;
}
