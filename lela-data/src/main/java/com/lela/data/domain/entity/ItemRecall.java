package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "item_recall", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId","RecallDate"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemRecallId", columnDefinition = "int(11)"))
public class ItemRecall extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_recall_item_UniqueItemId")
    private Item item;

    @Column(name = "RecallDate")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date recallDate;

    @Column(name = "RecallEvent", length = 255)
    private String recallEvent;

    @Column(name = "RecallUrl", length = 255)
    private String recallUrl;

    @Column(name = "RecallUnits", length = 255)
    private String recallUnits;

    @Column(name = "RecallHazard", length = 1024)
    private String recallHazard;

    @Column(name = "RecallIncidentsInjuries", length = 1024)
    private String recallIncidentsInjuries;

    @Column(name = "RecallSeverity", length = 512)
    private String recallSeverity;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
