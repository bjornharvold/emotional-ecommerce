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
@Table(name = "answer", uniqueConstraints = {@UniqueConstraint(columnNames={"QuestionId", "AnswerKey"})})
@AttributeOverride(name = "id", column = @Column(name = "AnswerId", columnDefinition = "int(11)"))
@RooSerializable
public class Answer extends AbstractEntity {

    @ManyToOne(targetEntity = Question.class)
    @JoinColumn(name = "QuestionId", nullable = false)
    @ForeignKey(name = "FK_answer_question", inverseName = "FK_question_answer")
    private Question question;

    @Column(name = "AnswerKey", nullable = false)
    @NotNull
    private Integer answerKey;

    @Column(name = "Answer", length = 100)
    private String answer;

    @Column(name = "Image", length = 100)
    private String image;

    @Column(name = "Z")
    private Integer q;

    @Column(name = "A")
    private Integer a;

    @Column(name = "B")
    private Integer b;

    @Column(name = "C")
    private Integer c;

    @Column(name = "D")
    private Integer d;

    @Column(name = "E")
    private Integer e;

    @Column(name = "F")
    private Integer f;

    @Column(name = "G")
    private Integer g;

    @Column(name = "H")
    private Integer h;

    @Column(name = "I")
    private Integer i;

    @Column(name = "J")
    private Integer j;

    @Column(name = "K")
    private Integer k;

    @Column(name = "L")
    private Integer l;
}
