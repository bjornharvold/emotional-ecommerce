package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "category_data_source_map")
@AttributeOverride(name = "id", column = @Column(name = "CategoryDataSourceMapId", columnDefinition = "int(11)"))
@RooSerializable
public class CategoryDataSourceMap extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="DataSourceTypeId")
    @ForeignKey(name = "FK_category_data_source_map_data_source")
    private DataSourceType dataSource;

    @Column(name = "DataSourceString")
    private String dataSoureceString;

    @Column(name = "AttributeName")
    private String attributeName;

    @Column(name = "AttributeValue")
    private String attributeValue;

    @ManyToOne
    @JoinColumn(name="CategoryId")
    @ForeignKey(name = "FK_category_data_source_map_category")
    private Category category;
}
