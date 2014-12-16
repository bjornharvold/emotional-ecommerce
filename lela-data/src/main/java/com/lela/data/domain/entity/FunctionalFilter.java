package com.lela.data.domain.entity;

import com.lela.data.domain.dto.ItemSearchCommand;
import org.hibernate.Session;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.criterion.Restrictions;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RooJavaBean
@RooToString(excludeFields = {"dateCreated", "dateModified"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "functional_filter")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterId", columnDefinition = "int(11)"))
public class FunctionalFilter extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "FunctionalFilterTypeId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_functional_filter_type")
    private FunctionalFilterType functionalFilterType;

    @ManyToOne
    @JoinColumn(name = "LocaleId", nullable = false, referencedColumnName = "LocaleId")
    @ForeignKey(name = "FK_functional_filter_locale")
    private Locale locale;

    @OneToMany(mappedBy="functionalFilter")
    private Set<FunctionalFilterAnswer> functionalFilterAnswers;

    @Column(name = "FunctionalFilterName", length = 100)
    private String functionalFilterName;

    @Column(name = "DataKey", length = 100)
    private String dataKey;

    @Column(name = "FunctionalFilterOrder")
    private Integer functionalFilterOrder;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

    @Column(name = "ObjectId", length = 24)
    private String objectId;

    @NotNull
    @Column(name = "FunctionalFilterLabel", nullable = false)
    private String functionalFilterLabel;


}
