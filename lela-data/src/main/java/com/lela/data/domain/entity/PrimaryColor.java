package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "primary_color")
@RooSerializable
@Table(name = "primary_color", uniqueConstraints = {@UniqueConstraint(name = "UK_primary_color_PrimaryColorName", columnNames={"PrimaryColorName"})})
@AttributeOverride(name = "id", column = @Column(name = "PrimaryColorId", columnDefinition = "int(11)"))
public class PrimaryColor extends AbstractEntity {

    @Column(name = "PrimaryColorName", length = 255)
    private String primaryColorName;

    @Column(name = "RGBCode", length = 32)
    private String rgbCode;

    @Column(name = "Hex", length = 32)
    private String hex;
}
