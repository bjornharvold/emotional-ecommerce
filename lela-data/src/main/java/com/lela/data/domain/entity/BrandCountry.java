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
@Table(name = "brand_country", uniqueConstraints = {@UniqueConstraint(columnNames={"BrandName", "CountryId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandCountryId", columnDefinition = "int(11)"))
public class BrandCountry extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_brand_country_brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "CountryId", nullable = false)
    @ForeignKey(name = "FK_brand_country_country")
    private Country country;

    @Column(name = "IsAvailable", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean available;

    @Column(name = "BrandName", length = 255)
    private String brandName;
}
