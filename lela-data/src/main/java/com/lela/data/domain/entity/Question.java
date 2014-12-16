package com.lela.data.domain.entity;

import java.util.HashSet;
import java.util.Set;
import javax.*;
import javax.persistence.*;
import javax.validation.constraints.Max;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "question")
@AttributeOverride(name = "id", column = @Column(name = "QuestionId", columnDefinition = "int(11)"))
@RooSerializable
public class Question extends AbstractEntity {

    @Column(name = "QuestionType", length = 100)
    private String questionType;

    @Column(name = "QuestionGroup")
    private Integer questionGroup;

    @Column(name = "Question", length = 100)
    private String questionText;

    @Column(name = "Localization", length = 20)
    private String localization;

    @Column(name = "isActive", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean active;
}
