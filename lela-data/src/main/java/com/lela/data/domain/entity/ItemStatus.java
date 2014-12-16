package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_status")
@Table(name = "item_status", uniqueConstraints = {@UniqueConstraint(columnNames={"ItemStatusName"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemStatusId", columnDefinition = "int(11)"))
public class ItemStatus extends AbstractEntity{

    @Column(name = "ItemStatusName")
    public String itemStatusName;

}
