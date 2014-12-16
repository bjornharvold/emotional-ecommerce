package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_changed")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemChangedId", columnDefinition = "int(11)"))

public class ItemChanged extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "UniqueItemId")
    private Item item;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirty;

    @Column(name = "IsDirty_development", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirtyInDevelopment;

    @Column(name = "IsDirty_latest", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirtyInLatest;

    @Column(name = "IsDirty_qa", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirtyInQA;

    @Column(name = "IsDirty_www", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirtyInWWW;

    @Column(name = "IsDirty_sandbox", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirtyInSandbox;

}
