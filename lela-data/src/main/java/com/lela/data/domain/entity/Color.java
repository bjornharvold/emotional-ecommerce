package com.lela.data.domain.entity;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooSerializable
@Table(name = "color", uniqueConstraints = {@UniqueConstraint(name = "UK_color_ColorName", columnNames={"ColorName"})})
@AttributeOverride(name = "id", column = @Column(name = "ColorId", columnDefinition = "int(11)"))
public class Color extends AbstractEntity {
    @Column(name = "ColorName", length = 255)
    @NotNull
    private String colorName;
}
