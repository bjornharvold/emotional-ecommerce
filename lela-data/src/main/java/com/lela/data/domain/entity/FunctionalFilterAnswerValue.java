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
@Table(name = "functional_filter_answer_value", uniqueConstraints = {@UniqueConstraint(columnNames={"FunctionalFilterAnswerId", "AttributeTypeId", "AttributeValue"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterAnswerValueId", columnDefinition = "int(11)"))
public class FunctionalFilterAnswerValue extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "FunctionalFilterAnswerId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_answer_value_functional_filter_answer")
    private FunctionalFilterAnswer functionalFilterAnswer;

    @ManyToOne
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_attribute_type")
    private AttributeType attributeType;

    @Column(name = "AttributeValue", length = 255)
    @NotNull
    private String attributeValue;

    @Column(name = "AnswerValue", length = 255)
    @NotNull
    private String answerValue;

    @Column(name = "RequiredValue", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean requiredValue;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
    }
}
