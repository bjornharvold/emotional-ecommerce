package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "accessory_type" )
@AttributeOverride(name = "id", column = @Column(name = "AccessoryTypeId", columnDefinition = "int(11)"))
@RooSerializable
public class AccessoryType extends AbstractEntity {

    @NotNull
    @Column(name = "AccessoryTypeName", nullable = false, unique = true)
    String accessoryTypeName;


}
