package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "series_list_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "SeriesListTypeId", columnDefinition = "int(11)"))
public class SeriesListType extends AbstractEntity {

    @Column(name="SeriesListTypeName", nullable = false)
    private String seriesListTypeName;

}
