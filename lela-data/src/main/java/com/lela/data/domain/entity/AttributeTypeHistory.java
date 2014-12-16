package com.lela.data.domain.entity;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.Date;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_type_history", uniqueConstraints = {@UniqueConstraint(columnNames={"AttributeTypeId", "ChangeDate"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeTypeHistoryId", columnDefinition = "int(11)"))
public class AttributeTypeHistory extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_history_attribute_type")
    private AttributeType attributeType;

    @Column(name = "AttributeName", nullable=false)
    private String attributeName;

    @Column(name = "AttributeSourceNameOld", nullable=false)
    private String attributeSourceNameOld;

    @Column(name = "AttributeSourceNameNew")
    private String attributeSourceNameNew;

    @Column(name = "ChangeDate")
    private Date changeDate;
}
