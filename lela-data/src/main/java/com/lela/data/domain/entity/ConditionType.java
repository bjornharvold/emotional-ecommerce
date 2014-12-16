package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "condition_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ConditionTypeId", columnDefinition = "int(11)"))
public class ConditionType extends AbstractEntity {

    @Column(name = "ConditionTypeName")
    String conditionTypeName;

    @Column(name = "UseInLela", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean useInLela;

}
