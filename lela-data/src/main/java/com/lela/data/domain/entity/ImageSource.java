package com.lela.data.domain.entity;

import com.google.common.collect.Iterables;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "image_source")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ImageSourceId", columnDefinition = "int(11)"))
public class ImageSource extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "SourceTypeId", nullable = false)
    @ForeignKey(name = "FK_image_source_image_source_type")
    private ImageSourceType imageSourceType;

    @Column(name = "SourceName", length = 255)
    private String sourceName;

    public static ImageSource findBySourceName(String sourceName)
    {
        List<ImageSource> results = entityManager().createQuery("Select o from ImageSource o where o.sourceName = :SourceName", ImageSource.class).setParameter("SourceName", sourceName).getResultList();
        return Iterables.getOnlyElement(results, null);
    }
}
