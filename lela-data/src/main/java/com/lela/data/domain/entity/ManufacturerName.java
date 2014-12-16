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
@Table(name = "manufacturer_name", uniqueConstraints = {@UniqueConstraint(columnNames={"ManufacturerId", "LocaleId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ManufacturerNameId", columnDefinition = "int(11)"))
public class ManufacturerName extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "LocaleId", nullable = false, referencedColumnName = "LocaleId")
    @ForeignKey(name = "FK_manufacturer_name_locale")
    private Locale locale;

    @ManyToOne
    @JoinColumn(name = "ManufacturerId", nullable = false)
    @ForeignKey(name = "FK_manufacturer_name_manufacturer")
    private Manufacturer manufacturer;

    @Column(name = "ManufacturerName", length = 255)
    @NotNull
    private String manufacturerName;
}
