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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "series_attribute_map")
@AttributeOverride(name = "id", column = @Column(name = "SeriesAttributeMapId", columnDefinition = "int(11)"))
@RooSerializable
public class SeriesAttributeMap extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="CategoryId")
    @ForeignKey(name = "FK_series_attribute_map_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name="SeriesTypeId")
    @ForeignKey(name = "FK_series_attribute_map_series_type")
    private SeriesType seriesType;

    @ManyToOne
    @JoinColumn(name="AttributeTypeId")
    @ForeignKey(name = "FK_series_attribute_map_attribute_type")
    private AttributeType attributeType;
}
