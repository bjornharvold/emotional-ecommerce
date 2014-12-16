package com.lela.data.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_type_normalization", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId", "AttributeTypeIdSource"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeTypeNormalizationId", columnDefinition = "int(11)"))

public class AttributeTypeNormalization extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "AttributeTypeIdNormalized", nullable = false)
    @ForeignKey(name = "FK_attribute_type_normalization_attribute_type")
    @NotNull
    private AttributeType attributeTypeIdNormalized;

    @ManyToOne
    @JoinColumn(name = "AttributeTypeIdSource", nullable = false)
    @ForeignKey(name = "FK_attribute_type_normalization_attribute_type_source")
    @NotNull
    private AttributeType attributeTypeIdSource;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_normalization_category")
    @NotNull
    private Category categoryId;

    @Column(name = "AttributeTypeOrder", nullable= false, columnDefinition = "INT(11) DEFAULT 1")
    private Integer attributeTypeOrder;
}
