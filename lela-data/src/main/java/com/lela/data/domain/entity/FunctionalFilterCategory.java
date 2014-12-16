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
import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "functional_filter_category")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterCategoryId", columnDefinition = "int(11)"))
public class FunctionalFilterCategory extends AbstractEntity {

    @ManyToOne(targetEntity = FunctionalFilter.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FunctionalFilterId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_category_functional_filter")
    FunctionalFilter functionalFilter;

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_category_category")
    private Category category;

    @Column(name="ObjectId", length = 24)
    private String objectId;

    public static List<FunctionalFilter> findByItemSearchCommand(ItemSearchCommand itemSearchCommand)
    {
        ((Session)entityManager().getDelegate()).createCriteria(FunctionalFilterCategory.class);
        Session session = ((Session)entityManager().getDelegate());
        List<FunctionalFilterCategory> functionalFiltersCategories = session.createCriteria(FunctionalFilterCategory.class).
                add(Restrictions.eq("category.id", itemSearchCommand.getCategoryId()))
                .list();

        List<FunctionalFilter> results = new ArrayList<FunctionalFilter>();
        for(FunctionalFilterCategory functionalFilterCategory:functionalFiltersCategories)
        {
           results.add(functionalFilterCategory.getFunctionalFilter());
        }
        return results;
    }

}
