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
@Table(name = "accessory_value_group", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId","AttributeTypeId", "AttributeValue"})})
@AttributeOverride(name = "id", column = @Column(name = "AccessoryValueGroupId", columnDefinition = "int(11)"))
@RooSerializable
public class AccessoryValueGroup extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "AccessoryGroupId", nullable = true)
    @ForeignKey(name = "FK_accessory_value_group_accessory_group")
    private AccessoryGroup accessoryGroup;

    @ManyToOne
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_accessory_value_group_attribute_type")
    private AttributeType attributeType;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_accessory_value_group_category")
    private Category category;

    @Column(name = "AttributeValue", length = 255)
    @NotNull
    private String attributeValue;

    @NotNull
    @Column(name = "AccessoryValueLabel", nullable = false)
    private String accessoryValueLabel;
}
