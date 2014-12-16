package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@AttributeOverride(name = "id", column = @Column(name = "CleanseRuleId", columnDefinition = "int(11)"))
@RooJpaActiveRecord(table = "cleanse_rule")
public class CleanseRule extends AbstractEntity{

    @Column(name = "CleanseRule")
    private String cleanseRuleValue;
}
