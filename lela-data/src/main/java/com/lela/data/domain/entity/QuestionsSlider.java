package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "questions_slider")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "QuestionsSliderId", columnDefinition = "int(11)"))
public class QuestionsSlider extends AbstractEntity {

    @Column(name = "Active", length = 50)
    private String active;

    @Column(name = "GroupName")
    private Integer groupName;

    @Column(name = "QuestionSerial")
    private Long questionSerial;

    @Column(name = "Instructions", length = 67)
    private String instructions;

    @Column(name = "Scale", length = 91)
    private String scale;

    @Column(name = "LeftSide", length = 73)
    private String leftSide;

    @Column(name = "RightSide", length = 72)
    private String rightSide;

}
