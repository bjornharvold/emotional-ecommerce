package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
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
@Table(name = "input_type", uniqueConstraints = {@UniqueConstraint(name = "UK_input_type_InputTypeName", columnNames={"InputTypeName"})})
@AttributeOverride(name = "id", column = @Column(name = "InputTypeId", columnDefinition = "int(11)"))
public class InputType extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "MultiValuedTypeId", nullable = false)
    @ForeignKey(name = "FK_input_type_multi_valued_type")
    private MultiValuedType multiValuedType;

    @Column(name = "InputTypeName", length = 255)
    @NotNull
    private String inputTypeName;
}
