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
@Table(name = "input_validation_list_element", uniqueConstraints = {@UniqueConstraint(columnNames={"InputValidationListId", "InputValue"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "InputValidationListElementId", columnDefinition = "int(11)"))
public class InputValidationListElement extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "InputValidationListId", nullable = false)
    @ForeignKey(name = "FK_input_validation_list_element_input_validation_list")
    private InputValidationList inputValidationList;

    @Column(name = "InputValue", length = 255)
    @NotNull
    private String inputValue;

    @Column(name = "ValidForNew", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean validForNew;
}
