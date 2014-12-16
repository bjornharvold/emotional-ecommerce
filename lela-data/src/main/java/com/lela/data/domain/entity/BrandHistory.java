package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "brand_history", uniqueConstraints = {@UniqueConstraint(columnNames={"BrandId","AsOfDate"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandHistoryId", columnDefinition = "int(11)"))
public class BrandHistory extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_brand_history_brand")
    private Brand brand;

    @Column(name = "AsOfDate")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date asOfDate;

    @Column(name = "FacebookLikes")
    private Integer facebookLikes;

    @Column(name = "TwitterFollowers")
    private Integer twitterFollowers;
}
