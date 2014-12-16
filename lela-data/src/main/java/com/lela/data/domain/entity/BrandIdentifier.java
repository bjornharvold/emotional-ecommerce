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
@Table(name = "brand_identifier", uniqueConstraints = {@UniqueConstraint(columnNames={"BrandId", "IdentifierTypeId", "IdentifierValue"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandIdentifierId", columnDefinition = "int(11)"))
public class BrandIdentifier extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_brand_identifier_brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "IdentifierTypeId", nullable = false)
    @ForeignKey(name = "FK_brand_identifier_identifier_type")
    private IdentifierType identifierType;

    @Column(name = "IdentifierValue", length = 255)
    @NotNull
    private String identifierValue;
}
