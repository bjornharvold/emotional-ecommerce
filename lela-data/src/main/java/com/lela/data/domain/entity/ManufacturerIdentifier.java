package com.lela.data.domain.entity;

import com.lela.data.domain.entity.IdentifierType;
import com.lela.data.domain.entity.Manufacturer;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "manufacturer_identifier", uniqueConstraints = {@UniqueConstraint(columnNames={"ManufacturerId", "IdentifierTypeId", "IdentifierValue"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ManufacturerIdentifierId", columnDefinition = "int(11)"))
public class ManufacturerIdentifier extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "ManufacturerId", nullable=false)
    @ForeignKey(name = "FK_manufacturer_identifier_manufacturer")
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "IdentifierTypeId", nullable=false)
    @ForeignKey(name = "FK_manufacturer_identifier_identifier_type")
    private IdentifierType identifierType;

    @Column(name = "IdentifierValue", nullable=false)
    private String identifierValue;
}
