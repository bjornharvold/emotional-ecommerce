package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "color_primary_color", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId", "PrimaryColorId", "ColorId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ColorPrimaryColorId", columnDefinition = "int(11)"))
public class ColorPrimaryColor extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_color_primary_color_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "ColorId", nullable = false)
    @ForeignKey(name = "FK_color_primary_color_color")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "PrimaryColorId", nullable = false)
    @ForeignKey(name = "FK_color_primary_color_primary_color")
    private PrimaryColor primaryColor;


}
