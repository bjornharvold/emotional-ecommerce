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
@Table(name = "category_load_rule")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CategoryLoadRuleId", columnDefinition = "int(11)"))
public class CategoryLoadRule extends AbstractEntity{

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "CategoryId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_attribute_formula_type_category")
    Category category;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AttributeTypeId", nullable = false, columnDefinition = "INT(11)")
    @ForeignKey(name = "FK_category_load_rule_attribute_type")
    AttributeType attributeType;

    @NotNull
    @Column(name="AttributeValue", nullable = false)
    String attributeValue;

    @NotNull
    @Column(name = "loadFlag", nullable = false, columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    Boolean loadFlag;
}
