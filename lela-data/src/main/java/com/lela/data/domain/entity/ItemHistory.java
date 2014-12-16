package com.lela.data.domain.entity;

import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.enums.ItemHistoryReportType;
import com.lela.data.enums.ItemStatuses;
import com.lela.data.web.commands.ItemHistoryReportingCommand;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.criterion.*;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "item_history", uniqueConstraints = {@UniqueConstraint(name = "UK_item_history", columnNames = {"UniqueItemId", "ItemHistorySequence"})})
@RooSerializable
@RooToString(excludeFields = {"conditionType", "reviewStatus", "siteStatus", "itemStatus", "itemId", "validated", "priority", "lelaUrl", "itemType", "productOverview", "designStyle", "researcherInitials", "researcherNotes", "modelNumber", "modelUrl", "listPrice", "listPriceRange", "salePrice", "doNotUse", "researchComplete", "objectId", "urlName", "blockFeedUpdates"})
@AttributeOverride(name = "id", column = @Column(name = "ItemHistoryId", columnDefinition = "int(11)"))
public class ItemHistory extends AbstractEntity {

    @Column(name="ItemHistorySequence")
    private Integer itemHistorySequence;

    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_history_item")
    @Fetch(FetchMode.SELECT)
    private Item item;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_item_history_category")
    private Category category;

    @ManyToOne(targetEntity = Brand.class)
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_item_history_brand")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ConditionTypeId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_history_condition_type_ConditionTypeId")
    private ConditionType conditionType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_hisory_review_status")
    private ReviewStatus reviewStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SiteStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_history_site_status")
    private SiteStatus siteStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_history_item_status")
    private ItemStatus itemStatus;

    @Column(name = "ItemId", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Integer itemId;

    @Column(name = "Validated", length = 255)
    private String validated;

    @Column(name = "Priority", length = 255)
    private String priority;

    @Column(name = "LelaUrl", length = 255)
    private String lelaUrl;

    @Column(name = "ItemType", length = 255)
    private String itemType;

    @Column(name = "ProductOverview", length = 12000)
    private String productOverview;

    @Column(name = "DesignStyle", length = 255)
    private String designStyle;

    @Column(name = "ResearcherInitials", length = 50)
    private String researcherInitials;

    @Column(name = "ResearcherNotes", length = 4096)
    private String researcherNotes;

    @Column(name = "ProductModelName", length = 255)
    private String productModelName;

    @Column(name = "ModelNumber", length = 255)
    private String modelNumber;

    @Column(name = "ModelUrl", length = 512)
    private String modelUrl;

    @Column(name = "ListPrice", length = 255)
    private String listPrice;

    @Column(name = "ListPriceRange", length = 255)
    private String listPriceRange;

    @Column(name = "SalePrice", length = 255)
    private String salePrice;

    @Column(name = "DoNotUse", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean doNotUse;

    @Column(name = "ResearchComplete", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean researchComplete;

    @Column(name = "ObjectID", length = 24)
    private String objectId;

    @Column(name = "URLName")
    private String urlName;

    @Column(name = "BlockFeedUpdates", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean blockFeedUpdates;

    @Column(name = "LastUpdateUser")
    @NotNull
    private String lastUpdateUser;

    @Column(name = "LastUpdateSystem")
    @NotNull
    private String lastUpdateSystem;


    public static List<ItemHistory> findChangedItems(ItemHistoryReportingCommand itemHistoryReportingCommand)
    {
        Session session = ((Session) entityManager().getDelegate());
        //session.enableFetchProfile("item-with-offers-images");
        //StatelessSession session = dSession.getSessionFactory().openStatelessSession();
        session.setDefaultReadOnly(true);


        Criteria query = session.createCriteria(ItemHistory.class, "o");
        query = query.setReadOnly(true);
        query.setProjection(Projections.rowCount());
        buildItemHistorySearchQuery(itemHistoryReportingCommand, query);
        long totalItems = ((Long) query.list().get(0)).longValue();
        System.out.println(totalItems);
        int batch = 100;

        List<ItemHistory> itemHistories = new ArrayList<ItemHistory>();

        query = session.createCriteria(ItemHistory.class, "o");
        buildItemHistorySearchQuery(itemHistoryReportingCommand, query);

        int page = 0;
        while(itemHistories.size() < totalItems)
        {
            long begin = System.currentTimeMillis();

            query.setMaxResults(batch);
            query.setFirstResult(batch * page);
            itemHistories.addAll( query.list());
            //for(ItemHistory itemHistory:itemHistories)
            //{
                //session.evict(itemHistory);
                //session.evict(itemHistory.getItem());
            //}
            page ++;
            long end = System.currentTimeMillis();
            long total = end - begin;
            session.flush();
            System.out.println("Total time: " + total);
        }

        return itemHistories;
    }

    private static Criteria buildItemHistorySearchQuery(ItemHistoryReportingCommand itemHistoryReportingCommand, Criteria query) {
        //if (itemHistoryReportingCommand.getCategoryId() != null) {
        //    query = query.add(Restrictions.eq("category.id", itemHistoryReportingCommand.getCategoryId()));
        //}

        if (itemHistoryReportingCommand.getFrom() != null) {
            query = query.add(Restrictions.ge("dateModified", itemHistoryReportingCommand.getFrom()));
        }

        if (itemHistoryReportingCommand.getTo() != null) {
            query = query.add(Restrictions.le("dateModified", itemHistoryReportingCommand.getTo()));
        }

        query.createAlias("o.item", "i");
        if(itemHistoryReportingCommand.getReportType().equals(ItemHistoryReportType.CUSTOM))
        {
          query = query.add(Restrictions.eq("o.itemStatus.id", itemHistoryReportingCommand.getFromItemStatus().getId()));

          query = query.add(Restrictions.eq("i.itemStatus.id", itemHistoryReportingCommand.getToItemStatus().getId()));
          //DetachedCriteria dc = DetachedCriteria.forClass(Item.class, "i")
          //      .add(Property.forName("o.item.id").eqProperty("i.id"))
          //      .add(Restrictions.eq("i.itemStatus.id", itemHistoryReportingCommand.getToItemStatus().getId()))
          //      .setProjection(Projections.property("i.id"));

          //query = query.add(Subqueries.exists(dc));
        }
        else if (itemHistoryReportingCommand.getReportType().equals(ItemHistoryReportType.ADDED_TO_PRODUCTION))
        {
            query = query.add(Restrictions.ne("o.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()));

            query = query.add(Restrictions.eq("i.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()));
            //DetachedCriteria dc = DetachedCriteria.forClass(Item.class, "i")
            //        .add(Property.forName("o.item.id").eqProperty("i.id"))
            //        .add(Restrictions.eq("i.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()))
            //        .setProjection(Projections.property("i.id"));

            //query = query.add(Subqueries.exists(dc));
        }
        else if (itemHistoryReportingCommand.getReportType().equals(ItemHistoryReportType.DROPPED_FROM_PRODUCTION))
        {
            query = query.add(Restrictions.eq("o.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()));

            query = query.add(Restrictions.ne("i.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()));
            //DetachedCriteria dc = DetachedCriteria.forClass(Item.class, "i")
            //        .add(Property.forName("o.item.id").eqProperty("i.id"))
            //        .add(Restrictions.ne("i.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()))
            //        .setProjection(Projections.property("i.id"));

            //query = query.add(Subqueries.exists(dc));
        }



        return query;
    }
}
