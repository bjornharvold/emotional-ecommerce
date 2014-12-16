package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "object_id_sequence")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ObjectIdSequenceId", columnDefinition = "int(11)"))
public class ObjectIdSequence extends AbstractEntity {

    @Column(name = "Val")
    private Integer val;
}
