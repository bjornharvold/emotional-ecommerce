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
@Table(name = "category_data_source", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId", "DataSourceTypeId", "DataSourceString"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CategoryDataSourceId", columnDefinition = "int(11)"))
public class CategoryDataSource extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_category_data_source_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "DataSourceTypeId", nullable = false)
    @ForeignKey(name = "FK_category_data_source_data_source_type")
    private DataSourceType dataSourceType;

    @Column(name = "DataSourceString", length = 255)
    @NotNull
    private String dataSourceString;

    @NotNull
    @Column(name = "CanCreate", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean canCreate;

    @NotNull
    @Column(name = "HasLelaId", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean hasLelaId;

    @NotNull
    @Column(name = "IsPrimaryFromSource", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean isPrimaryFromSource;

    @NotNull
    @Column(name = "GetAttributesFromSource", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean getAttributesFromSource;

    @NotNull
    @Column(name = "GetImagesFromSource", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean getImagesFromSource;

    @NotNull
    @Column(name = "GetDescriptionsFromSource", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean getDescriptionsFromSource;

    @NotNull
    @Column(name = "UseCategoryDataSourceMap", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean useCategoryDataSourceMap;

    @NotNull
    @Column(name = "AllowCategoryChange", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean allowCategoryChange;

}
