// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.dbview.ViewService;
import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.ConditionType;
import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemAttribute;
import com.lela.data.domain.entity.ItemColor;
import com.lela.data.domain.entity.ItemIdentifier;
import com.lela.data.domain.entity.ItemRecall;
import com.lela.data.domain.entity.ItemStatus;
import com.lela.data.domain.entity.ItemVideo;
import com.lela.data.domain.entity.MerchantOffer;
import com.lela.data.domain.entity.ProductImageItem;
import com.lela.data.domain.entity.ProductMotivator;
import com.lela.data.domain.entity.ReviewStatus;
import com.lela.data.domain.entity.SiteStatus;
import java.util.Map;
import java.util.Set;

privileged aspect Item_Roo_JavaBean {
    
    public ViewService Item.getViewService() {
        return this.viewService;
    }
    
    public void Item.setViewService(ViewService viewService) {
        this.viewService = viewService;
    }
    
    public Set<ItemAttribute> Item.getItemAttributes() {
        return this.itemAttributes;
    }
    
    public void Item.setItemAttributes(Set<ItemAttribute> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }
    
    public Map<AttributeType, ItemAttribute> Item.getItemAttributeMap() {
        return this.itemAttributeMap;
    }
    
    public void Item.setItemAttributeMap(Map<AttributeType, ItemAttribute> itemAttributeMap) {
        this.itemAttributeMap = itemAttributeMap;
    }
    
    public Map<String, ItemAttribute> Item.getItemAttributeMapByName() {
        return this.itemAttributeMapByName;
    }
    
    public void Item.setItemAttributeMapByName(Map<String, ItemAttribute> itemAttributeMapByName) {
        this.itemAttributeMapByName = itemAttributeMapByName;
    }
    
    public Set<ItemColor> Item.getItemColors() {
        return this.itemColors;
    }
    
    public void Item.setItemColors(Set<ItemColor> itemColors) {
        this.itemColors = itemColors;
    }
    
    public Set<ItemIdentifier> Item.getItemIdentifiers() {
        return this.itemIdentifiers;
    }
    
    public void Item.setItemIdentifiers(Set<ItemIdentifier> itemIdentifiers) {
        this.itemIdentifiers = itemIdentifiers;
    }
    
    public Set<ItemRecall> Item.getItemRecalls() {
        return this.itemRecalls;
    }
    
    public void Item.setItemRecalls(Set<ItemRecall> itemRecalls) {
        this.itemRecalls = itemRecalls;
    }
    
    public Set<ItemVideo> Item.getItemVideos() {
        return this.itemVideos;
    }
    
    public void Item.setItemVideos(Set<ItemVideo> itemVideos) {
        this.itemVideos = itemVideos;
    }
    
    public Set<ProductMotivator> Item.getProductMotivators() {
        return this.productMotivators;
    }
    
    public void Item.setProductMotivators(Set<ProductMotivator> productMotivators) {
        this.productMotivators = productMotivators;
    }
    
    public Set<ProductImageItem> Item.getProductImageItems() {
        return this.productImageItems;
    }
    
    public void Item.setProductImageItems(Set<ProductImageItem> productImageItems) {
        this.productImageItems = productImageItems;
    }
    
    public Set<MerchantOffer> Item.getMerchantOffers() {
        return this.merchantOffers;
    }
    
    public void Item.setMerchantOffers(Set<MerchantOffer> merchantOffers) {
        this.merchantOffers = merchantOffers;
    }
    
    public Category Item.getCategory() {
        return this.category;
    }
    
    public void Item.setCategory(Category category) {
        this.category = category;
    }
    
    public Brand Item.getBrand() {
        return this.brand;
    }
    
    public void Item.setBrand(Brand brand) {
        this.brand = brand;
    }
    
    public ConditionType Item.getConditionType() {
        return this.conditionType;
    }
    
    public void Item.setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
    
    public ReviewStatus Item.getReviewStatus() {
        return this.reviewStatus;
    }
    
    public void Item.setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
    
    public SiteStatus Item.getSiteStatus() {
        return this.siteStatus;
    }
    
    public void Item.setSiteStatus(SiteStatus siteStatus) {
        this.siteStatus = siteStatus;
    }
    
    public ItemStatus Item.getItemStatus() {
        return this.itemStatus;
    }
    
    public void Item.setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }
    
    public Integer Item.getItemId() {
        return this.itemId;
    }
    
    public void Item.setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    public String Item.getValidated() {
        return this.validated;
    }
    
    public void Item.setValidated(String validated) {
        this.validated = validated;
    }
    
    public String Item.getPriority() {
        return this.priority;
    }
    
    public void Item.setPriority(String priority) {
        this.priority = priority;
    }
    
    public String Item.getLelaUrl() {
        return this.lelaUrl;
    }
    
    public void Item.setLelaUrl(String lelaUrl) {
        this.lelaUrl = lelaUrl;
    }
    
    public String Item.getItemType() {
        return this.itemType;
    }
    
    public void Item.setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String Item.getProductOverview() {
        return this.productOverview;
    }
    
    public void Item.setProductOverview(String productOverview) {
        this.productOverview = productOverview;
    }
    
    public String Item.getDesignStyle() {
        return this.designStyle;
    }
    
    public void Item.setDesignStyle(String designStyle) {
        this.designStyle = designStyle;
    }
    
    public String Item.getResearcherInitials() {
        return this.researcherInitials;
    }
    
    public void Item.setResearcherInitials(String researcherInitials) {
        this.researcherInitials = researcherInitials;
    }
    
    public String Item.getResearcherNotes() {
        return this.researcherNotes;
    }
    
    public void Item.setResearcherNotes(String researcherNotes) {
        this.researcherNotes = researcherNotes;
    }
    
    public String Item.getProductModelName() {
        return this.productModelName;
    }
    
    public void Item.setProductModelName(String productModelName) {
        this.productModelName = productModelName;
    }
    
    public String Item.getModelNumber() {
        return this.modelNumber;
    }
    
    public void Item.setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
    
    public String Item.getModelUrl() {
        return this.modelUrl;
    }
    
    public void Item.setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }
    
    public String Item.getListPrice() {
        return this.listPrice;
    }
    
    public void Item.setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }
    
    public String Item.getListPriceRange() {
        return this.listPriceRange;
    }
    
    public void Item.setListPriceRange(String listPriceRange) {
        this.listPriceRange = listPriceRange;
    }
    
    public String Item.getSalePrice() {
        return this.salePrice;
    }
    
    public void Item.setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }
    
    public Boolean Item.getDoNotUse() {
        return this.doNotUse;
    }
    
    public void Item.setDoNotUse(Boolean doNotUse) {
        this.doNotUse = doNotUse;
    }
    
    public Boolean Item.getResearchComplete() {
        return this.researchComplete;
    }
    
    public void Item.setResearchComplete(Boolean researchComplete) {
        this.researchComplete = researchComplete;
    }
    
    public String Item.getObjectId() {
        return this.objectId;
    }
    
    public void Item.setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public String Item.getUrlName() {
        return this.urlName;
    }
    
    public void Item.setUrlName(String urlName) {
        this.urlName = urlName;
    }
    
    public Boolean Item.getBlockFeedUpdates() {
        return this.blockFeedUpdates;
    }
    
    public void Item.setBlockFeedUpdates(Boolean blockFeedUpdates) {
        this.blockFeedUpdates = blockFeedUpdates;
    }
    
    public String Item.getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    
    public void Item.setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    
    public String Item.getLastUpdateSystem() {
        return this.lastUpdateSystem;
    }
    
    public void Item.setLastUpdateSystem(String lastUpdateSystem) {
        this.lastUpdateSystem = lastUpdateSystem;
    }
    
}