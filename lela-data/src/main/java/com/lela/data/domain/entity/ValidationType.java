package com.lela.data.domain.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString(excludeFields = {"dateCreated", "dateModified", "version"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "validation_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ValidationTypeId", columnDefinition = "int(11)"))
public class ValidationType extends AbstractEntity {

    @Column(name = "ValidationType", length = 255)
    private String validationTypeName;

}
