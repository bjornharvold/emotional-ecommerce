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
@Table(name = "item_video", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId", "VideoUrl"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemVideoId", columnDefinition = "int(11)"))
public class ItemVideo extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_video_item_UniqueItemId")
    private Item item;

    @Column(name = "VideoUrl", length = 255)
    @NotNull
    private String videoUrl;

    @Column(name = "VideoSubscribers")
    private Integer videoSubscribers;

    @Column(name = "VideoViews")
    private Integer videoViews;

    @Column(name = "VideoTitle")
    private String videoTitle;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "VideoTypeId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_video_video_type")
    private VideoType videoType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ReviewStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_video_review_status")
    private ReviewStatus reviewStatus;

}
