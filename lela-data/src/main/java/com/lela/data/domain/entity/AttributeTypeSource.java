package com.lela.data.domain.entity;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString(excludeFields = {"dateCreated", "dateModified", "version"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_type_source", uniqueConstraints = {@UniqueConstraint(name = "UK_attribute_type_source_AttributeTypeSourceName", columnNames={"AttributeTypeSourceName"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeTypeSourceId", columnDefinition = "int(11)"))
public class AttributeTypeSource extends AbstractEntity {
    @Column(name = "AttributeTypeSourceName", length = 255)
    @NotNull
    private String attributeTypeSourceName;
}
