package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "series_type")
@Table(name = "series_type", uniqueConstraints = {@UniqueConstraint(name = "UK_series_type_SeriesTypeName", columnNames={"SeriesTypeName"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "SeriesTypeId", columnDefinition = "int(11)"))
public class SeriesType extends AbstractEntity{

    @NotNull
    @Column(name = "SeriesTypeName")
    private String seriesTypeName;
}
