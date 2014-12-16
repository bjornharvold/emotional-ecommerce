package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooSerializable
@Table(name = "category_data_source_series_type", uniqueConstraints = {@UniqueConstraint(name = "UK_merchant_category", columnNames={"CategoryDataSourceId", "SeriesTypeId"})})
@AttributeOverride(name = "id", column = @Column(name = "CategoryDataSourceSeriesTypeId", columnDefinition = "int(11)"))
public class CategoryDataSourceSeriesType extends AbstractEntity {


    @ManyToOne(targetEntity = CategoryDataSource.class)
    @JoinColumn(name="CategoryDataSourceId", nullable = false)
    @NotNull
    private CategoryDataSource categoryDataSource;

    @ManyToOne(targetEntity = SeriesType.class)
    @JoinColumn(name="SeriesTypeId", nullable = false)
    @NotNull
    private SeriesType seriesType;

    @Column(name="EnableSeries", nullable = false, columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    @NotNull
    private Boolean enableSeries;
}
