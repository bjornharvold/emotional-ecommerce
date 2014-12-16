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
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@AttributeOverride(name = "id", column = @Column(name = "ActionTypeId", columnDefinition = "int(11)"))
@Table(name = "action_type", uniqueConstraints = {@UniqueConstraint(name = "UK_action_type_ActionTypeName", columnNames={"ActionTypeName"})})
@RooSerializable
public class ActionType extends AbstractEntity{

    @NotNull
    @Column(name = "ActionTypeName")
    private String actionTypeName;
}
