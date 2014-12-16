package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_formula_attribute_type",
        uniqueConstraints = {@UniqueConstraint(name = "UK_attribute_type_AttributeName", columnNames={"AttributeFormulaId", "AttributeTypeId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeFormulaAttributeTypeId", columnDefinition = "int(11)"))
public class AttributeFormulaAttributeType extends AbstractEntity {

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AttributeFormulaId", nullable = false, columnDefinition = "INT(11)")
    @ForeignKey(name = "FK_attribute_formula_type_attribute_formula")
    AttributeFormula attributeFormula;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AttributeTypeId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_attribute_formula_type_attribute_type")
    AttributeType attributeType;

    @NotNull
    @Column(name="AttributeSequence", columnDefinition = "INT(11) DEFAULT 1")
    Integer attributeSequence;

    @Column(name = "outerJoin", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    Boolean outerJoin;
}
