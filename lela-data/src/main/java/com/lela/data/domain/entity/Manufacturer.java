package com.lela.data.domain.entity;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooSerializable
@Table(name = "manufacturer", uniqueConstraints = {@UniqueConstraint(name = "UK_manufacturer_ManufacturerName", columnNames={"ManufacturerName"})})
@AttributeOverride(name = "id", column = @Column(name = "ManufacturerId", columnDefinition = "int(11)"))
public class Manufacturer extends AbstractEntity {

    @Column(name = "ManufacturerName", length = 255)
    @NotNull
    private String manufacturerName;
}
