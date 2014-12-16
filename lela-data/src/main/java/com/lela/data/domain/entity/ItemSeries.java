package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_series")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemSeriesId", columnDefinition = "int(11)"))
public class ItemSeries extends AbstractEntity{

    @NotNull
    @ManyToOne
    @JoinColumn( name = "SeriesId")
    @ForeignKey(name = "FK_item_series_series")
    private Series series;

    @NotNull
    @ManyToOne
    @JoinColumn( name = "UniqueItemId")
    @ForeignKey(name="FK_item_series_item")
    private Item item;

    @Column(name = "IsPrimary", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean primaryFlag;
}
