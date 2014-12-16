package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString(excludeFields = {"dateCreated", "dateModified", "version"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "data_source_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "DataSourceTypeId", columnDefinition = "int(11)"))
public class DataSourceType extends AbstractEntity {
    @Column(name = "DataSourceType", length = 255)
    @NotNull
    private String dataSourceTypeName;

}
