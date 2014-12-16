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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "country")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CountryId", columnDefinition = "int(11)"))
public class Country extends AbstractEntity {
    @Column(name = "CountryName", length = 255)
    @NotNull
    private String countryName;

    @Column(name = "ISO2", length = 2)
    private String iso2;

    @Column(name = "LelaActive", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean lelaActive;
}
