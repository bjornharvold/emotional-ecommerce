package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "review_source")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ReviewSourceId", columnDefinition = "int(11)"))
public class ReviewSource extends AbstractEntity {

    @NotNull
    @ManyToOne(targetEntity = ReviewProvider.class)
    @JoinColumn(name = "ReviewProviderId", nullable = false)
    @ForeignKey(name = "FK_review_source_review_provider")
    ReviewProvider reviewProvider;

    @NotNull
    @Column(name="ReviewProviderSourceId", nullable = false)
    Integer reviewProviderSourceId;

    @NotNull
    @Column(name="ReviewSourceName", nullable = false)
    String reviewSourceName;

    @Column(name="CountryCode")
    String countryCode;

    @Column(name="LanguageCode")
    String languageCode;

    @Column(name="SourceWWW")
    String sourceWWW;

    @Column(name="SourceLogo")
    String sourceLogo;

    @Column(name="SourceLogoWidth")
    Integer sourceLogoWidth;

    @Column(name="SourceLogoHeight")
    Integer sourceLogoHeight;

    @NotNull
    @Column(name="SourcePictureRequired", nullable=false, columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    Boolean sourcePictureRequired;

    @NotNull
    @Column(name="SourceURLRequired", nullable=false, columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    Boolean sourceURLRequired;

    @Column(name="SourceRank")
    Integer sourceRank;
}
