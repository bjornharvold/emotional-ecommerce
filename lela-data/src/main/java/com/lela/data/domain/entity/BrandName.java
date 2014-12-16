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
@Table(name = "brand_name", uniqueConstraints = {@UniqueConstraint(columnNames={"BrandId","LocaleId", "BrandName"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandNameId", columnDefinition = "int(11)"))
public class BrandName extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_brand_name_brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "LocaleId", nullable = false, referencedColumnName = "LocaleId")
    @ForeignKey(name = "FK_brand_name_locale")
    private Locale locale;

    @Column(name = "BrandName", length = 80)
    @NotNull
    private String brandName;

    @Column(name = "MainName")
    @NotNull
    private Integer mainName;
}
