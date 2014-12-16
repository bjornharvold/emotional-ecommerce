package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "functional_filter_answer", uniqueConstraints = {@UniqueConstraint(columnNames={"FunctionalFilterId","AnswerId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterAnswerId", columnDefinition = "int(11)"))
public class FunctionalFilterAnswer extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "FunctionalFilterId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_answer_functional_filter")
    private FunctionalFilter functionalFilter;

    @OneToMany(mappedBy="functionalFilterAnswer")
    private Set<FunctionalFilterAnswerValue> functionalFilterAnswerValues;

    @Column(name = "AnswerId")
    @NotNull
    private Integer answer;

    @Column(name = "AnswerKey", length = 80)
    private String answerKey;

    @Column(name = "AnswerValue", length = 255)
    private String answerValue;

    @Column(name = "AnswerOrder")
    private Integer answerOrder;

    @Column(name = "IsDefault", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean defaultt;

    @NotNull
    @Column(name = "FunctionalFilterAnswerLabel", nullable = false)
    private String functionalFilterAnswerLabel;

}
