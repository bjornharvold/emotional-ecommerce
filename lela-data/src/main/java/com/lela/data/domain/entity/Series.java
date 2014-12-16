package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "series")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "SeriesId", columnDefinition = "int(11)"))
public class Series extends AbstractEntity{

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SeriesTypeId")
    @ForeignKey(name = "FK_series_series_type")
    private SeriesType seriesType;

    @NotNull
    @Column(name = "SeriesName")
    private String seriesName;

    @NotNull
    @Column(name = "SeriesDisplayName")
    private String seriesDisplayName;

    @Column(name = "UseThisSeries", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean useThisSeries;

}
