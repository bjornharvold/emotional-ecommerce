package com.lela.data.domain.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.lela.data.domain.dbview.ViewService;
import com.lela.data.domain.document.ItemAttributes;
import com.lela.data.domain.dto.BaseMotivator;
import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.enums.ItemStatuses;
import com.lela.data.enums.ProductImageItemStatuses;
import com.lela.data.enums.ReviewStatuses;
import com.lela.domain.ApplicationConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.criterion.*;
import org.hibernate.engine.EntityKey;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

//import org.springframework.data.persistence.document.RelatedDocument;


@RooJavaBean
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "item", uniqueConstraints = {@UniqueConstraint(name = "UK_item_categoryid_itemid", columnNames = {"CategoryId", "ItemId"})})
@RooSerializable
@RooToString(excludeFields = {"itemAttributes", "itemColors", "itemIdentifiers", "itemRecalls", "itemVideos", "productImageItems", "merchantOffers", "conditionType", "reviewStatus", "siteStatus", "itemStatus", "itemId", "validated", "priority", "lelaUrl", "itemType", "productOverview", "designStyle", "researcherInitials", "researcherNotes", "modelNumber", "modelUrl", "listPrice", "listPriceRange", "salePrice", "doNotUse", "researchComplete", "objectId", "urlName", "blockFeedUpdates"})
@AttributeOverride(name = "id", column = @Column(name = "UniqueItemId", columnDefinition = "int(11)"))
//@FetchProfile(name = "item-with-offers-images", fetchOverrides = {
//        @FetchProfile.FetchOverride(entity = Item.class, association = "merchantOffers", mode = FetchMode.JOIN),
//        @FetchProfile.FetchOverride(entity = Item.class, association = "productImageItems", mode = FetchMode.JOIN)
//})
public class Item extends AbstractEntity {

    final static Logger logger = LoggerFactory.getLogger(Item.class);

    @Autowired
    @Transient
    ViewService viewService;
    //@Autowired
    //@Transient
    //ItemAttributesRepository repository;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<ItemAttribute> itemAttributes;

    @Transient
    private Map<AttributeType, ItemAttribute> itemAttributeMap;

    @Transient
    private Map<String, ItemAttribute> itemAttributeMapByName;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<ItemColor> itemColors;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<ItemIdentifier> itemIdentifiers;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<ItemRecall> itemRecalls;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<ItemVideo> itemVideos;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<ProductMotivator> productMotivators;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private Set<ProductImageItem> productImageItems;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<MerchantOffer> merchantOffers;

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_item_category")
    private Category category;

    @ManyToOne(targetEntity = Brand.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_item_brand")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ConditionTypeId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_condition_type_ConditionTypeId")
    private ConditionType conditionType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_review_status")
    private ReviewStatus reviewStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SiteStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_site_status")
    private SiteStatus siteStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemStatusId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_item_item_status")
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

    @Transient
    public Map<Character, ProductMotivator> getProductMotivatorMap()
    {
        Map<Character, ProductMotivator> productMotivatorMap = new HashMap<Character, ProductMotivator>();
        for(ProductMotivator productMotivator:productMotivators)
        {
            productMotivatorMap.put(productMotivator.getMotivator().getMotivatorLabel(), productMotivator);
        }
        return productMotivatorMap;
    }

    @Override
    public void prePersist() {
        determineCurrentUser();

        this.lastUpdateSystem = ApplicationConstants.DATA_APP;
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }



    @Override
    public void preUpdate() {
        determineCurrentUser();

        this.lastUpdateSystem = ApplicationConstants.DATA_APP;
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void determineCurrentUser() {
        this.lastUpdateUser = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @JsonIgnore
    public Date getMostRecentProductImageDateCreated() {
        return entityManager().createQuery("Select max(o.productImage.dateCreated) from ProductImageItem o where o.item.id = :id", Date.class).setParameter("id", this.getId()).getSingleResult();
    }

    @JsonIgnore
    public Date getMostRecentProductImageDateModified() {
        return entityManager().createQuery("Select max(o.productImage.dateModified) from ProductImageItem o where o.item.id = :id", Date.class).setParameter("id", this.getId()).getSingleResult();
    }

    public static List<Item> findItemsByCategory(Category category, int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Item o where o.category = :Category", Item.class).setParameter("Category", category).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<Item> findItemsByCategoryByProductImageDateCreated(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Long[] reviewStatusId, int firstResult, int maxResults, String order) {
        String where = buildWhereClause(category, brand, itemStatus, itemId, productModelName, reviewStatusId, false);
        Query query = entityManager().createQuery(String.format("SELECT o FROM Item o left join o.productImageItems p where %s group by o order by max(p.productImage.dateCreated) %s", where, order), Item.class);
        addParameters(category, brand, itemStatus, itemId, productModelName, false, query);
        return query.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<Item> findItemsByCategoryByProductImageDateModified(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Long[] reviewStatusId, int firstResult, int maxResults, String order) {
        String where = buildWhereClause(category, brand, itemStatus, itemId, productModelName, reviewStatusId, false);
        Query query = entityManager().createQuery(String.format("SELECT o FROM Item o left join o.productImageItems p where %s group by o order by max(p.productImage.dateModified) %s", where, order), Item.class);
        addParameters(category, brand, itemStatus, itemId, productModelName, false, query);
        return query.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }


    public static List<Item> findItemsByCategory(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Long[] reviewStatusId, Boolean hasNewImages, int firstResult, int maxResults, String sort, String order) {
        String where = buildWhereClause(category, brand, itemStatus, itemId, productModelName, reviewStatusId, hasNewImages);

        if (StringUtils.isNotBlank(sort))
            sort = " order by " + sort;

        Query query = entityManager().createQuery(String.format("SELECT o FROM Item o where %s %s %s", where, sort, order), Item.class).setParameter("Category", category);

        addParameters(category, brand, itemStatus, itemId, productModelName, hasNewImages, query);
        return query.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<Item> findAllItemsByCategory(Category category) {
        return entityManager().createQuery("SELECT o FROM Item o where o.category = :Category", Item.class).setParameter("Category", category).getResultList();
    }

    public static long countItemsByCategory(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Long[] reviewStatusId, Boolean hasNewImages) {
        String where = buildWhereClause(category, brand, itemStatus, itemId, productModelName, reviewStatusId, hasNewImages);
        Query query = entityManager().createQuery(String.format("SELECT COUNT(o) FROM Item o where %s", where), Long.class);
        addParameters(category, brand, itemStatus, itemId, productModelName, hasNewImages, query);
        return (Long) query.getSingleResult();
    }

    public static List<Item> findItemsByLelaUrl(String lelaUrl) {
        if (lelaUrl == null || lelaUrl.length() == 0)
            throw new IllegalArgumentException("The lelaUrl argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.lelaUrl = :lelaUrl", Item.class);
        q.setParameter("lelaUrl", lelaUrl);
        return q.getResultList();
    }

    @Transient
    public void removeFromSession() {
        Session session = ((Session) entityManager().getDelegate());


        //session.evict(merchantOffers);
        //session.evict(itemAttributes);
        for (ItemAttribute itemAttribute : itemAttributes) {
            session.evict(itemAttribute);
        }
        //session.evict(itemRecalls);
        //session.evict(productMotivators);
        for (ProductMotivator productMotivator : productMotivators) {
            session.evict(productMotivator);
        }
        session.evict(this);
        this.itemAttributeMap = null;
        this.itemAttributeMapByName = null;
        //session.flush();
        printStatistics();
    }


    private void printStatistics() {
        Session session = ((Session) entityManager().getDelegate());
        logger.debug(session.getStatistics().toString());


        Iterator entityKeys = session.getStatistics().getEntityKeys().iterator();
        Multimap<String, EntityKey> entityKeyCounts = ArrayListMultimap.create();
        while (entityKeys.hasNext()) {
            EntityKey entityKey = (EntityKey) entityKeys.next();
            entityKeyCounts.put(entityKey.getEntityName(), entityKey);
        }
        for (String entityName : entityKeyCounts.keySet()) {
            logger.debug(entityName + ":" + entityKeyCounts.get(entityName).size());
        }

        for (String roleName : session.getSessionFactory().getStatistics().getCollectionRoleNames()) {
            CollectionStatistics collectionStatistics = session.getSessionFactory().getStatistics().getCollectionStatistics(roleName);
            if (collectionStatistics.getLoadCount() > 0)
                logger.debug("roleName:" + roleName + ":" + collectionStatistics.toString());
        }

        for (String entityName : session.getSessionFactory().getStatistics().getEntityNames()) {
            EntityStatistics entityStatistics = session.getSessionFactory().getStatistics().getEntityStatistics(entityName);
            if (entityStatistics.getLoadCount() > 0)
                logger.debug("entityName:" + entityName + ":" + entityStatistics.toString());
        }
    }

    public static Long findCountItemByItemSearchCommand(ItemSearchCommand itemSearchCommand) {
        Session session = ((Session) entityManager().getDelegate());
        //StatelessSession session = dSession.getSessionFactory().openStatelessSession();
        session.setDefaultReadOnly(true);

        Criteria query = session.createCriteria(Item.class, "o");
        query.setProjection(Projections.rowCount());
        query = query.setReadOnly(true);

        query = buildItemSearchQuery(itemSearchCommand, query);


        return ((Long) query.list().get(0)).longValue();

    }

    private static Criteria buildItemSearchQuery(ItemSearchCommand itemSearchCommand, Criteria query) {
        if (itemSearchCommand.getCategoryId() != null) {
            query = query.add(Restrictions.eq("category.id", itemSearchCommand.getCategoryId()));
        }

        if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "PRODUCTION")
                || ArrayUtils.contains(itemSearchCommand.getItemStatus(), "TESTING")
                || ArrayUtils.contains(itemSearchCommand.getItemStatus(), "LATEST")) {
            Disjunction disjunction = Restrictions.disjunction();
            if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "PRODUCTION")) {
                disjunction.add(Restrictions.eq("o.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()));
            }
            if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "TESTING")) {
                disjunction.add(Restrictions.eq("o.itemStatus.id", ItemStatuses.INGEST_FOR_TESTING_ONLY.getId()));
            }
            if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "LATEST")) {
                disjunction.add(Restrictions.eq("o.itemStatus.id", ItemStatuses.INGEST_PROD_DISPLAY.getId()));
                disjunction.add(Restrictions.eq("o.itemStatus.id", ItemStatuses.INGEST_FOR_TESTING_ONLY.getId()));
            }
            query.add(disjunction);
        }

        if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "MISSING_IMAGES")) {
            DetachedCriteria dc = DetachedCriteria.forClass(ProductImageItem.class, "mo")
                    .add(Property.forName("mo.item.id").eqProperty("o.id"))
                    .setProjection(Projections.property("mo.id"));

            query = query.add(Subqueries.notExists(dc));

        }
        if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "MISSING_MOTIVATORS")) {
            DetachedCriteria dc = DetachedCriteria.forClass(ProductMotivator.class, "mo")
                    .add(Property.forName("mo.item.id").eqProperty("o.id"))
                    .setProjection(Projections.property("mo.id"));

            query = query.add(Subqueries.notExists(dc));
        }
        if (ArrayUtils.contains(itemSearchCommand.getItemStatus(), "MISSING_OFFERS")) {
            DetachedCriteria dc = DetachedCriteria.forClass(MerchantOffer.class, "mo")
                    .add(Property.forName("mo.item.id").eqProperty("o.id"))
                    .setProjection(Projections.property("mo.id"));

            query = query.add(Subqueries.notExists(dc));
        }
        return query;
    }

    public static List<Item> findItemByItemSearchCommand(ItemSearchCommand itemSearchCommand, int offset, int maxSize) {

        Session session = ((Session) entityManager().getDelegate());
        //StatelessSession session = dSession.getSessionFactory().openStatelessSession();
        session.setDefaultReadOnly(true);

        Criteria query = session.createCriteria(Item.class, "o");
        query = query.setReadOnly(true);

        query = buildItemSearchQuery(itemSearchCommand, query);
        //query = query.setFetchMode("attributes", org.hibernate.FetchMode.);


        query.setMaxResults(maxSize);
        query.setFirstResult(offset);
        List<Item> items = query.list();
        return items;
    }

    @Transient
    @JsonIgnore
    public Map<AttributeType, ItemAttribute> getItemAttributesMap() {


        //ItemAttributes itemAttributesMap = this.repository.findByItemId(this.getId());

        //mongoTemplate.findOne(new org.springframework.data.mongodb.core.query.Query(where("itemId").is(this.getId())), ItemAttributes.class);

        /*if(itemAttributesMap == null ||
                itemAttributesMap.getItemAttributeMap() == null ||
                itemAttributesMap.getItemAttributeMap().size()==0)
        {
            itemAttributesMap = copyItemAttributes();
        }
        */

        if (this.itemAttributeMap == null) {
            Set<ItemAttribute> itemAttrs = this.getItemAttributes();
            Map<AttributeType, ItemAttribute> itemAttributes = new HashMap<AttributeType, ItemAttribute>(itemAttrs.size());
            for (ItemAttribute itemAttribute : itemAttrs) {
                itemAttributes.put(itemAttribute.getAttributeType(), itemAttribute);
            }
            this.itemAttributeMap = itemAttributes;
            logger.debug("Initialized ItemAttributeMap");
        }
        return this.itemAttributeMap;//itemAttributesMap;
    }

    @Transient
    @JsonIgnore
    public Map<String, ItemAttribute> getItemAttributesMapByName() {

        if (this.itemAttributeMapByName == null) {
            Map<String, ItemAttribute> itemAttributes = new HashMap<String, ItemAttribute>();
            for (ItemAttribute itemAttribute : this.getItemAttributes()) {
                itemAttributes.put(itemAttribute.getAttributeType().getLelaAttributeName(), itemAttribute);
            }
            this.itemAttributeMapByName = itemAttributes;
        }
        return this.itemAttributeMapByName;//itemAttributesMap;
    }

    public ItemAttributes copyItemAttributes() {
        ItemAttributes itemAttributesMap = new ItemAttributes(this.getId());
        for (ItemAttribute itemAttribute : this.itemAttributes) {
            com.lela.data.domain.document.ItemAttribute itemAttributeCopy = new com.lela.data.domain.document.ItemAttribute();
            BeanUtils.copyProperties(itemAttribute, itemAttributeCopy);
            itemAttributeCopy.setAttributeName(itemAttribute.getAttributeType().getAttributeName());
            itemAttributesMap.addItemAttribute(itemAttribute.getAttributeType().getAttributeName(), itemAttributeCopy);
        }
        return itemAttributesMap;
    }

    @Transient
    public void setItemAttributesMap(ItemAttributes itemAttributesMap) {
        //this.mongoTemplate.save(itemAttributesMap);
        //this.repository.save(itemAttributesMap);
    }

    public BaseMotivator getBaseMotivator() {
        /*
        List<BaseMotivator> baseMotivators = this.viewService.findBaseMotivatorsByCategoryAndItem(this.getCategory().getId(), this.getItemId());

        if(baseMotivators.size() > 0)
            return baseMotivators.get(0);

        //return an empty BaseMotivator
        BaseMotivator baseMotivator = new BaseMotivator();
        baseMotivator.setCategoryId(this.getCategory().getId());
        baseMotivator.setItemId(this.getItemId());
        baseMotivator.setUniqueItemId(this.getId());
        */
        return new BaseMotivator();
    }

    //public String toString()
    //{
    //    return this.lelaUrl;
    //}

    private static void addParameters(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Boolean hasNewImages, Query query) {

        if (category != null)
            query.setParameter("Category", category);
        if (brand != null)
            query.setParameter("Brand", brand);
        if (itemStatus != null)
            query.setParameter("ItemStatus", itemStatus);
        if (itemId != null)
            query.setParameter("ItemId", itemId);
        if (StringUtils.isNotBlank(productModelName))
            query.setParameter("ProductModelName", "%" + productModelName + "%");
        if (hasNewImages)
            query.setParameter("ProductImageItemStatus", ProductImageItemStatuses.NEW_IMAGE.getId());
    }

    private static String buildWhereClause(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Long[] reviewStatusIds, Boolean hasNewImages) {
        String where = " 1 = 1 ";
        if (category != null) {
            where += " AND o.category = :Category ";
        }
        if (brand != null) {
            where += " AND o.brand = :Brand ";
        }
        if (itemStatus != null) {
            where += " AND o.itemStatus = :ItemStatus ";
        }
        if (itemId != null) {
            where += " AND o.itemId = :ItemId ";
        }
        if (StringUtils.isNotBlank(productModelName)) {
            where += " AND o.productModelName like (:ProductModelName)";
        }
        if (reviewStatusIds != null && reviewStatusIds.length > 0) {
            where += " AND o.reviewStatus.id in ( ";
            for (int i = 0; i < reviewStatusIds.length; i++) {
                if (i > 0) where += ", ";
                where += reviewStatusIds[i];
            }
            where += " ) ";
        }
        if(hasNewImages)
        {
            where += " AND exists ( from ProductImageItem as pit where pit.item = o and productImageItemStatus.id = :ProductImageItemStatus) ";
        }

        return where;
    }

    @Transient
    @JsonIgnore
    public static Set<MerchantOffer> getValidMerchantOffers(Item item)
    {
        return Sets.filter(item.getMerchantOffers(), new Predicate<MerchantOffer>() {
            @Override
            public boolean apply(@Nullable MerchantOffer merchantOffer) {
                return merchantOffer.getReviewStatus().equals(ReviewStatuses.PASSED.getReviewStatus()) && merchantOffer.getAvailable() && !merchantOffer.getDoNotUse();
            }
        });
    }

    @Transient
    @JsonIgnore
    public static Set<ProductImageItem> getValidProductImages(Item item)
    {
        return Sets.filter(item.getProductImageItems(), new Predicate<ProductImageItem>() {
            @Override
            public boolean apply(@Nullable ProductImageItem productImageItem) {
                return productImageItem.getProductImageItemStatus().equals(ProductImageItemStatuses.PREFERRED_IMAGE.getProductImageItemStatus()) || productImageItem.getProductImageItemStatus().equals(ProductImageItemStatuses.USE.getProductImageItemStatus());
            }
        });
    }

    @Transient
    public static String getProductMotivatorScore( com.lela.data.domain.entity.Item item, java.lang.String label)
    {
        ProductMotivator productMotivator = item.getProductMotivatorMap().get(label.charAt(0));
        if(productMotivator!=null)
        {
            return String.valueOf(productMotivator.getMotivatorScore());
        }
        return "UNKNOWN";

    }

}
