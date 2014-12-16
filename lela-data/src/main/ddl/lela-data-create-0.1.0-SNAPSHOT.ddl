
    create table accessory_group (
        AccessoryGroupId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AccessoryGroupName varchar(255),
        primary key (AccessoryGroupId)
    ) ENGINE=InnoDB;

    create table accessory_value_group (
        AccessoryValueGroupId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeValue varchar(255),
        AccessoryGroupId int(11),
        AttributeTypeId int(11) not null,
        CategoryId int(11) not null,
        primary key (AccessoryValueGroupId),
        unique (CategoryId, AttributeTypeId, AttributeValue)
    ) ENGINE=InnoDB;

    create table action_type (
        ActionTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ActionTypeName varchar(255) unique,
        primary key (ActionTypeId)
    ) ENGINE=InnoDB;

    create table advertiser_name_merchant (
        AdvertiserNameMerchantId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AdvertiserName varchar(255),
        MerchantId int(11),
        primary key (AdvertiserNameMerchantId)
    ) ENGINE=InnoDB;

    create table affiliate_report (
        AffiliateReportId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        FileName varchar(255),
        ReceivedDate datetime,
        primary key (AffiliateReportId)
    ) ENGINE=InnoDB;

    create table affiliate_transaction (
        AffiliateTransactionId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AdvertiserName varchar(255),
        Association varchar(255),
        ClickDate datetime,
        CommissionAmount decimal(19,2),
        Email varchar(255),
        EventDate datetime,
        FirstName varchar(255),
        LastName varchar(255),
        network varchar(255),
        orderId varchar(255),
        OrganizationId integer,
        ProcessDate datetime,
        ProductCategory varchar(255),
        ProductId varchar(255),
        ProductName varchar(255),
        Provider varchar(255),
        ProviderId integer,
        Quantity integer,
        RedirectId varchar(255),
        redirectUrl varchar(512),
        ReferralDate datetime,
        ReferringProductId varchar(255),
        RevenueId bigint,
        SalesAmount decimal(19,2),
        SubId varchar(255),
        TrackingId varchar(255),
        transactionDate datetime,
        UserId varchar(255),
        AffiliateReportId int(11) not null,
        primary key (AffiliateTransactionId)
    ) ENGINE=InnoDB;

    create table answer (
        AnswerId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        A integer,
        Answer varchar(100),
        AnswerKey integer not null,
        B integer,
        C integer,
        D integer,
        E integer,
        F integer,
        G integer,
        H integer,
        I integer,
        Image varchar(100),
        J integer,
        K integer,
        L integer,
        Z integer,
        QuestionId int(11) not null,
        primary key (AnswerId),
        unique (QuestionId, AnswerKey)
    ) ENGINE=InnoDB;

    create table attribute_formula (
        AttributeFormulaId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Description varchar(255),
        FromClause varchar(1024),
        SelectClause varchar(1024),
        SequenceGroup integer,
        WhereClause varchar(1024),
        AttributeTypeId int(11) not null,
        CategoryId int(11) not null,
        primary key (AttributeFormulaId),
        unique (CategoryId, AttributeTypeId)
    ) ENGINE=InnoDB;

    create table attribute_type (
        AttributeTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsAccessory INT(11) NOT NULL DEFAULT 1,
        AttributeName varchar(255) unique,
        AttributeSourceName varchar(255),
        LelaAttributeName varchar(255),
        AttributeTypeSourceId int(11) not null,
        CleanseRuleId int(11) not null,
        ValidationTypeId int(11) not null,
        primary key (AttributeTypeId)
    ) ENGINE=InnoDB;

    create table attribute_type_category (
        AttributeTypeCategoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DefaultValue varchar(255),
        AttributeTypeId int(11) not null,
        CategoryId int(11) not null,
        InputTypeId int(11),
        InputValidationListId int(11),
        primary key (AttributeTypeCategoryId),
        unique (CategoryId, AttributeTypeId)
    ) ENGINE=InnoDB;

    create table attribute_type_history (
        AttributeTypeHistoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeName varchar(255) not null,
        AttributeSourceNameNew varchar(255),
        AttributeSourceNameOld varchar(255) not null,
        ChangeDate datetime,
        AttributeTypeId int(11) not null,
        primary key (AttributeTypeHistoryId),
        unique (AttributeTypeId, ChangeDate)
    ) ENGINE=InnoDB;

    create table attribute_type_motivator (
        AttributeTypeMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        EddDataType varchar(255),
        Motivator integer,
        IsRequired INT(11) NOT NULL DEFAULT 0,
        AttributeTypeId int(11) not null,
        CategoryId int(11) not null,
        primary key (AttributeTypeMotivatorId),
        unique (CategoryId, AttributeTypeId, Motivator)
    ) ENGINE=InnoDB;

    create table attribute_type_normalization (
        AttributeTypeNormalizationId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CategoryId int(11) not null,
        AttributeTypeIdNormalized int(11) not null,
        AttributeTypeIdSource int(11) not null,
        primary key (AttributeTypeNormalizationId),
        unique (CategoryId, AttributeTypeIdSource)
    ) ENGINE=InnoDB;

    create table attribute_type_source (
        AttributeTypeSourceId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeTypeSourceName varchar(255) unique,
        primary key (AttributeTypeSourceId)
    ) ENGINE=InnoDB;

    create table brand (
        BrandId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        BrandName varchar(80),
        FacebookAccess integer,
        FacebookLikes integer,
        FacebookUrl varchar(80),
        IsDirty INT(11) NOT NULL DEFAULT 0,
        LelaUrl varchar(100),
        Manufacturer varchar(80),
        objectId varchar(24),
        PopshopsBrandId integer,
        PopshopsSuccess INT(11) DEFAULT 0,
        TwitterFollowers integer,
        TwitterName varchar(80),
        TwitterUrl varchar(80),
        URLName varchar(100),
        ManufacturerId int(11),
        primary key (BrandId)
    ) ENGINE=InnoDB;

    create table brand_category (
        BrandCategoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        BrandId int(11) not null,
        CategoryId int(11) not null,
        primary key (BrandCategoryId),
        unique (BrandId, CategoryId)
    ) ENGINE=InnoDB;

    create table brand_category_itemtype_motivator (
        BrandCategoryItemtypeMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ItemType varchar(255),
        Motivator integer,
        MotivatorScore integer,
        BrandId int(11),
        CategoryId int(11),
        primary key (BrandCategoryItemtypeMotivatorId),
        unique (CategoryId, BrandId, ItemType, Motivator)
    ) ENGINE=InnoDB;

    create table brand_category_motivator (
        BrandCategoryMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Motivator integer,
        MotivatorScore integer,
        BrandId int(11) not null,
        CategoryId int(11) not null,
        primary key (BrandCategoryMotivatorId),
        unique (CategoryId, BrandId, Motivator)
    ) ENGINE=InnoDB;

    create table brand_country (
        BrandCountryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsAvailable INT(11) NOT NULL DEFAULT 0,
        BrandName varchar(255),
        BrandId int(11) not null,
        CountryId int(11) not null,
        primary key (BrandCountryId),
        unique (BrandName, CountryId)
    ) ENGINE=InnoDB;

    create table brand_history (
        BrandHistoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AsOfDate date,
        FacebookLikes integer,
        TwitterFollowers integer,
        BrandId int(11) not null,
        primary key (BrandHistoryId),
        unique (BrandId, AsOfDate)
    ) ENGINE=InnoDB;

    create table brand_identifier (
        BrandIdentifierId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IdentifierValue varchar(255),
        BrandId int(11) not null,
        IdentifierTypeId int(11) not null,
        primary key (BrandIdentifierId),
        unique (BrandId, IdentifierTypeId, IdentifierValue)
    ) ENGINE=InnoDB;

    create table brand_motivator (
        BrandMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Motivator integer,
        MotivatorScore integer,
        BrandId int(11) not null,
        primary key (BrandMotivatorId),
        unique (BrandId, Motivator)
    ) ENGINE=InnoDB;

    create table brand_name (
        BrandNameId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        BrandName varchar(80),
        MainName integer,
        BrandId int(11) not null,
        LocaleId int(11) not null,
        primary key (BrandNameId),
        unique (BrandId, LocaleId, BrandName)
    ) ENGINE=InnoDB;

    create table category (
        CategoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CatalogKey varchar(50),
        CategoryName varchar(80),
        CategoryNavbar integer,
        CategoryObjectId varchar(50),
        CategoryOrder integer,
        DataSourceString varchar(255),
        IsDirty INT(11) NOT NULL DEFAULT 0,
        LelaUrl varchar(100),
        objectId varchar(24),
        URLName varchar(100),
        IsVisible INT(11) NOT NULL DEFAULT 0,
        DataSourceTypeId int(11),
        primary key (CategoryId)
    ) ENGINE=InnoDB;

    create table category_category_group (
        CategoryCategoryGroupId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CategoryId int(11),
        CategoryGroupId int(11),
        primary key (CategoryCategoryGroupId),
        unique (CategoryId, CategoryGroupId)
    ) ENGINE=InnoDB;

    create table category_data_source (
        CategoryDataSourceId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CanCreate INT(11) NOT NULL DEFAULT 0,
        DataSourceString varchar(255),
        GetAttributesFromSource INT(11) NOT NULL DEFAULT 1,
        GetDescriptionsFromSource INT(11) NOT NULL DEFAULT 1,
        GetImagesFromSource INT(11) NOT NULL DEFAULT 1,
        HasLelaId INT(11) NOT NULL DEFAULT 0,
        IsPrimaryFromSource INT(11) NOT NULL DEFAULT 0,
        UseCategoryDataSourceMap INT(11) NOT NULL DEFAULT 0,
        CategoryId int(11) not null,
        DataSourceTypeId int(11) not null,
        primary key (CategoryDataSourceId),
        unique (CategoryId, DataSourceTypeId, DataSourceString)
    ) ENGINE=InnoDB;

    create table category_data_source_map (
        CategoryDataSourceMapId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeName varchar(255),
        AttributeValue varchar(255),
        DataSourceString varchar(255),
        CategoryId int(11),
        DataSourceTypeId int(11),
        primary key (CategoryDataSourceMapId)
    ) ENGINE=InnoDB;

    create table category_group (
        CategoryGroupId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CategoryGroupName varchar(255),
        CategoryGroupOrder integer,
        CategoryGroupStatus varchar(255),
        CategoryGroupURLName varchar(255),
        IsDirty INT(11) NOT NULL DEFAULT 0,
        NavbarId int(11) not null,
        primary key (CategoryGroupId)
    ) ENGINE=InnoDB;

    create table category_motivator (
        CategoryMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DtRuleDataTag varchar(50),
        DtRuleSet varchar(50),
        DtRuleSheet varchar(50),
        DtRulesQuery varchar(20000),
        CategoryId int(11) not null,
        MotivatorId int(11) not null,
        primary key (CategoryMotivatorId)
    ) ENGINE=InnoDB;

    create table category_subdepartment (
        CategorySubdepartmentId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CategoryOrder integer,
        CategoryId int(11) not null,
        SubdepartmentId int(11) not null,
        primary key (CategorySubdepartmentId)
    ) ENGINE=InnoDB;

    create table cleanse_rule (
        CleanseRuleId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CleanseRule varchar(255),
        primary key (CleanseRuleId)
    ) ENGINE=InnoDB;

    create table color (
        ColorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ColorName varchar(255) unique,
        primary key (ColorId)
    ) ENGINE=InnoDB;

    create table color_primary_color (
        ColorPrimaryColorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CategoryId int(11) not null,
        ColorId int(11) not null,
        PrimaryColorId int(11) not null,
        primary key (ColorPrimaryColorId),
        unique (CategoryId, PrimaryColorId, ColorId)
    ) ENGINE=InnoDB;

    create table condition_type (
        ConditionTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ConditionTypeName varchar(255),
        UseInLela INT(11) NOT NULL DEFAULT 0,
        primary key (ConditionTypeId)
    ) ENGINE=InnoDB;

    create table country (
        CountryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        CountryName varchar(255),
        ISO2 varchar(2),
        LelaActive INT(11) NOT NULL DEFAULT 0,
        primary key (CountryId)
    ) ENGINE=InnoDB;

    create table data_source_type (
        DataSourceTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DataSourceType varchar(255),
        primary key (DataSourceTypeId)
    ) ENGINE=InnoDB;

    create table department (
        DepartmentId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DepartmentName varchar(255) unique,
        DepartmentOrder integer unique,
        DepartmentStatus varchar(255),
        DepartmentURLName varchar(255) unique,
        IsDirty INT(11) NOT NULL,
        primary key (DepartmentId)
    ) ENGINE=InnoDB;

    create table design_style_motivator (
        DesignStyleMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DesignStyle varchar(255),
        H integer,
        I integer,
        J integer,
        K integer,
        L integer,
        CategoryId int(11) not null,
        primary key (DesignStyleMotivatorId),
        unique (CategoryId, DesignStyle)
    ) ENGINE=InnoDB;

    create table functional_filter (
        FunctionalFilterId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DataKey varchar(100),
        IsDirty INT(11) NOT NULL DEFAULT 0,
        FunctionalFilterName varchar(100),
        FunctionalFilterOrder integer,
        ObjectId varchar(24),
        CategoryId int(11) not null,
        FunctionalFilterTypeId int(11) not null,
        LocaleId int(11) not null,
        primary key (FunctionalFilterId)
    ) ENGINE=InnoDB;

    create table functional_filter_answer (
        FunctionalFilterAnswerId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AnswerId integer,
        AnswerKey varchar(80),
        AnswerOrder integer,
        AnswerValue varchar(255),
        IsDefault INT(11) NOT NULL DEFAULT 0,
        FunctionalFilterId int(11) not null,
        primary key (FunctionalFilterAnswerId),
        unique (FunctionalFilterId, AnswerId)
    ) ENGINE=InnoDB;

    create table functional_filter_answer_value (
        FunctionalFilterAnswerValueId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AnswerValue varchar(255),
        AttributeValue varchar(255),
        RequiredValue INT(11) NOT NULL DEFAULT 0,
        AttributeTypeId int(11) not null,
        FunctionalFilterAnswerId int(11) not null,
        primary key (FunctionalFilterAnswerValueId),
        unique (FunctionalFilterAnswerId, AttributeTypeId, AttributeValue)
    ) ENGINE=InnoDB;

    create table functional_filter_type (
        FunctionalFilterTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        FunctionalFilterTypeName varchar(80),
        primary key (FunctionalFilterTypeId)
    ) ENGINE=InnoDB;

    create table identifier_type (
        IdentifierTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IdentifierTypeName varchar(255),
        IsMultiValued INT(11) NOT NULL DEFAULT 1,
        AttributeTypeId int(11),
        primary key (IdentifierTypeId)
    ) ENGINE=InnoDB;

    create table image_source (
        ImageSourceId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SourceName varchar(255),
        SourceTypeId int(11) not null,
        primary key (ImageSourceId)
    ) ENGINE=InnoDB;

    create table image_source_type (
        ImageSourceTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SourceTypeName varchar(255),
        primary key (ImageSourceTypeId)
    ) ENGINE=InnoDB;

    create table input_type (
        InputTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        InputTypeName varchar(255) unique,
        MultiValuedTypeId int(11) not null,
        primary key (InputTypeId)
    ) ENGINE=InnoDB;

    create table input_validation_list (
        InputValidationListId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ListName varchar(255),
        primary key (InputValidationListId)
    ) ENGINE=InnoDB;

    create table input_validation_list_element (
        InputValidationListElementId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        InputValue varchar(255),
        ValidForNew INT(11) NOT NULL DEFAULT 0,
        InputValidationListId int(11) not null,
        primary key (InputValidationListElementId),
        unique (InputValidationListId, InputValue)
    ) ENGINE=InnoDB;

    create table item (
        UniqueItemId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        BlockFeedUpdates INT(11) NOT NULL DEFAULT 0,
        DesignStyle varchar(255),
        DoNotUse INT(11) NOT NULL DEFAULT 0,
        ItemId INT(11) NOT NULL DEFAULT 0,
        ItemType varchar(255),
        LelaUrl varchar(255),
        ListPrice varchar(255),
        ListPriceRange varchar(255),
        ModelNumber varchar(255),
        ModelUrl varchar(512),
        ObjectID varchar(24),
        Priority varchar(255),
        ProductModelName varchar(255),
        ProductOverview varchar(12000),
        ResearchComplete INT(11) NOT NULL DEFAULT 0,
        ResearcherInitials varchar(50),
        ResearcherNotes varchar(4096),
        SalePrice varchar(255),
        URLName varchar(255),
        Validated varchar(255),
        BrandId int(11) not null,
        CategoryId int(11) not null,
        ConditionTypeId INT(11) DEFAULT 1 not null,
        ItemStatusId INT(11) DEFAULT 1 not null,
        ReviewStatusId INT(11) DEFAULT 1 not null,
        SiteStatusId INT(11) DEFAULT 1 not null,
        primary key (UniqueItemId),
        unique (CategoryId, ItemId)
    ) ENGINE=InnoDB;

    create table item_attribute (
        ItemAttributeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeSequence integer,
        AttributeValue varchar(12000),
        AttributeTypeId int(11) not null,
        UniqueItemId int(11) not null,
        primary key (ItemAttributeId),
        unique (UniqueItemId, AttributeTypeId, AttributeSequence)
    ) ENGINE=InnoDB;

    create table item_attribute_history (
        ItemAttributeHistoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeSequence integer,
        NewAttributeValue varchar(10000),
        OldAttributeValue varchar(10000),
        AttributeTypeId int(11),
        UniqueItemId int(11),
        primary key (ItemAttributeHistoryId)
    ) ENGINE=InnoDB;

    create table item_changed (
        ItemChangedId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsDirty INT(11) NOT NULL DEFAULT 0,
        IsDirty_development INT(11) NOT NULL DEFAULT 0,
        IsDirty_latest INT(11) NOT NULL DEFAULT 0,
        IsDirty_qa INT(11) NOT NULL DEFAULT 0,
        IsDirty_www INT(11) NOT NULL DEFAULT 0,
        UniqueItemId int(11),
        primary key (ItemChangedId)
    ) ENGINE=InnoDB;

    create table item_color (
        ItemColorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsAvailable INT(11) NOT NULL DEFAULT 0,
        ColorId int(11) not null,
        UniqueItemId int(11) not null,
        primary key (ItemColorId),
        unique (UniqueItemId, ColorId)
    ) ENGINE=InnoDB;

    create table item_color_merchant (
        ItemColorMerchantId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsAvailable INT(11) NOT NULL DEFAULT 0,
        ItemColorId int(11) not null,
        MerchantId int(11) not null,
        primary key (ItemColorMerchantId)
    ) ENGINE=InnoDB;

    create table item_identifier (
        ItemIdentifierId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IdentifierValue varchar(255),
        IdentifierTypeId int(11) not null,
        UniqueItemId int(11) not null,
        primary key (ItemIdentifierId),
        unique (UniqueItemId, IdentifierTypeId, IdentifierValue)
    ) ENGINE=InnoDB;

    create table item_recall (
        ItemRecallId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        RecallDate date,
        RecallEvent varchar(255),
        RecallHazard varchar(1024),
        RecallIncidentsInjuries varchar(1024),
        RecallSeverity varchar(512),
        RecallUnits varchar(255),
        RecallUrl varchar(255),
        UniqueItemId int(11) not null,
        primary key (ItemRecallId),
        unique (UniqueItemId, RecallDate)
    ) ENGINE=InnoDB;

    create table item_series (
        ItemSeriesId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsPrimary INT(11) NOT NULL DEFAULT 0,
        UniqueItemId int(11),
        SeriesId int(11),
        primary key (ItemSeriesId)
    ) ENGINE=InnoDB;

    create table item_status (
        ItemStatusId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ItemStatusName varchar(255),
        primary key (ItemStatusId),
        unique (ItemStatusName)
    ) ENGINE=InnoDB;

    create table item_video (
        ItemVideoId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        VideoSubscribers integer,
        VideoUrl varchar(255),
        VideoViews integer,
        UniqueItemId int(11) not null,
        primary key (ItemVideoId),
        unique (UniqueItemId, VideoUrl)
    ) ENGINE=InnoDB;

    create table local_store (
        LocalStoreId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Address varchar(100),
        City varchar(50),
        IsDirty INT(11) NOT NULL DEFAULT 0,
        Latitude varchar(200),
        LelaLocalCode varchar(50) unique,
        Longitude varchar(200),
        MerchantId integer,
        MerchantName varchar(50),
        ObjectId varchar(24),
        PhoneNumber varchar(25),
        State varchar(15),
        StoreName varchar(100),
        StoreNumber integer,
        ZipCode varchar(10),
        primary key (LocalStoreId)
    ) ENGINE=InnoDB;

    create table locale (
        LocaleId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        LocaleName varchar(255) unique,
        primary key (LocaleId)
    ) ENGINE=InnoDB;

    create table manufacturer (
        ManufacturerId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ManufacturerName varchar(255) unique,
        primary key (ManufacturerId)
    ) ENGINE=InnoDB;

    create table manufacturer_identifier (
        ManufacturerIdentifierId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IdentifierValue varchar(255) not null,
        IdentifierTypeId int(11) not null,
        ManufacturerId int(11) not null,
        primary key (ManufacturerIdentifierId),
        unique (ManufacturerId, IdentifierTypeId, IdentifierValue)
    ) ENGINE=InnoDB;

    create table manufacturer_name (
        ManufacturerNameId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ManufacturerName varchar(255),
        LocaleId int(11) not null,
        ManufacturerId int(11) not null,
        primary key (ManufacturerNameId),
        unique (ManufacturerId, LocaleId)
    ) ENGINE=InnoDB;

    create table merchant (
        MerchantId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AlwaysShowLocal INT(11) NOT NULL DEFAULT 0,
        IsDirty INT(11) NOT NULL DEFAULT 0,
        ImageQuality integer,
        LelaUrl varchar(255),
        LogoUrl varchar(255),
        MerchantApproved INT(11) NOT NULL DEFAULT 0,
        MerchantName varchar(255),
        MerchantValid INT(11) NOT NULL DEFAULT 0,
        ObjectId varchar(24),
        Url varchar(255),
        NetworkId int(11),
        primary key (MerchantId)
    ) ENGINE=InnoDB;

    create table merchant_deal (
        MerchantDealId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DealName varchar(255),
        DealSpecifics varchar(20),
        EndOn varchar(50),
        StartOn varchar(50),
        Url varchar(255),
        MerchantId int(11),
        primary key (MerchantDealId)
    ) ENGINE=InnoDB;

    create table merchant_offer (
        OfferId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsAvailable INT(11) NOT NULL DEFAULT 0,
        Color varchar(255),
        DoNotUse INT(11) NOT NULL DEFAULT 0,
        LoadDateId integer,
        MerchantName varchar(255),
        ModelNumber varchar(255),
        OfferDate varchar(255),
        OfferItemName varchar(500),
        OfferPrice varchar(18),
        OfferSalePrice varchar(18),
        OfferUrl varchar(512),
        SourceKey varchar(255),
        UPC varchar(25),
        UseThisOffer INT(11) NOT NULL DEFAULT 1,
        BrandId int(11),
        ConditionTypeId int(11) not null,
        UniqueItemId int(11),
        MerchantId int(11),
        SourceId int(11) not null,
        SourceTypeId int(11),
        ReviewStatusId int(11),
        primary key (OfferId)
    ) ENGINE=InnoDB;

    create table merchant_offer_history (
        MerchnatOfferHistoryId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsAvailable bit,
        EndDate datetime,
        OfferId integer,
        OfferItemName varchar(500),
        OfferPrice varchar(18),
        OfferSalePrice varchar(18),
        SourceId varchar(255),
        SourceKey varchar(255),
        SourceTypeId varchar(255),
        StartDate datetime,
        UniqueItemId int(11),
        MerchantId int(11),
        primary key (MerchnatOfferHistoryId)
    ) ENGINE=InnoDB;

    create table merchant_offer_keyword (
        MerchantOfferKeywordId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        OfferKeyword varchar(255),
        ActionTypeId int(11) not null,
        CategoryId int(11) not null,
        primary key (MerchantOfferKeywordId)
    ) ENGINE=InnoDB;

    create table merchant_source (
        MerchantSourceId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SourceId integer,
        SourceName varchar(255),
        SourceTypeId int(11),
        primary key (MerchantSourceId),
        unique (SourceId, SourceTypeId)
    ) ENGINE=InnoDB;

    create table merchant_source_type (
        MerchantSourceTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SourceTypeName varchar(255),
        primary key (MerchantSourceTypeId)
    ) ENGINE=InnoDB;

    create table motivator (
        MotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        MotivatorLabel char(1),
        RulesEngineDt varchar(50),
        RulesEngineEdd varchar(50),
        SubCategoryId integer,
        CategoryId int(11) not null,
        primary key (MotivatorId)
    ) ENGINE=InnoDB;

    create table multivalued_type (
        MultiValuedTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        MultiValuedTypeName varchar(255),
        primary key (MultiValuedTypeId)
    ) ENGINE=InnoDB;

    create table navbar (
        NavbarId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsDirty INT(11) NOT NULL DEFAULT 0,
        ObjectId varchar(24),
        Rlnm varchar(255),
        primary key (NavbarId)
    ) ENGINE=InnoDB;

    create table network (
        NetworkId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Enabled INT(11) NOT NULL DEFAULT 0,
        NetworkName varchar(25),
        PopshopsDesignator varchar(5),
        primary key (NetworkId)
    ) ENGINE=InnoDB;

    create table object_id_sequence (
        ObjectIdSequenceId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Val integer,
        primary key (ObjectIdSequenceId)
    ) ENGINE=InnoDB;

    create table primary_color (
        PrimaryColorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Hex varchar(32),
        PrimaryColorName varchar(255) unique,
        RGBCode varchar(32),
        primary key (PrimaryColorId)
    ) ENGINE=InnoDB;

    create table product_detail_attribute_type (
        ProductDetailAttributeTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeTypeID int(11) not null,
        ProductDetailGroupAttributeId int(11) not null,
        primary key (ProductDetailAttributeTypeId),
        unique (ProductDetailGroupAttributeId, AttributeTypeID)
    ) ENGINE=InnoDB;

    create table product_detail_attribute_value_type (
        ProductDetailAttributeValueTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ProductDetailAttributeValueTypeName varchar(255),
        primary key (ProductDetailAttributeValueTypeId)
    ) ENGINE=InnoDB;

    create table product_detail_group (
        ProductDetailGroupId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        GroupId integer,
        GroupName varchar(58),
        GroupOrder integer,
        SectionId int(11) not null,
        primary key (ProductDetailGroupId),
        unique (SectionId, GroupId)
    ) ENGINE=InnoDB;

    create table product_detail_group_attribute (
        ProductDetailGroupAttributeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttrLabel varchar(54),
        AttrName varchar(54),
        OrderInGroup integer,
        ProductDetailAttributeValueTypeId int(11),
        ProductDetailGroupId int(11),
        primary key (ProductDetailGroupAttributeId)
    ) ENGINE=InnoDB;

    create table product_detail_part (
        ProductDetailPartId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        PartName varchar(255),
        PartSeq integer,
        ProductDetailGroupAttributeId int(11) not null,
        primary key (ProductDetailPartId),
        unique (ProductDetailGroupAttributeId, PartSeq)
    ) ENGINE=InnoDB;

    create table product_detail_part_value (
        ProductDetailPartValueId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeValue varchar(255),
        AttributeTypeId int(11) not null,
        ProductDetailPartId int(11) not null,
        primary key (ProductDetailPartValueId),
        unique (ProductDetailPartValueId, AttributeTypeId, AttributeValue)
    ) ENGINE=InnoDB;

    create table product_detail_section (
        SectionId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsDirty INT(11) NOT NULL DEFAULT 0,
        ObjectId varchar(24),
        SectionName varchar(100),
        SectionOrder integer,
        CategoryId int(11) not null,
        primary key (SectionId)
    ) ENGINE=InnoDB;

    create table product_image (
        ProductImageId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        BValue integer,
        DoNotUse INT(11) NOT NULL DEFAULT 0,
        GValue integer,
        HexValue varchar(255),
        ImageAngle varchar(200),
        ImageDate varchar(255),
        ImageType varchar(200),
        ImageURLLarge varchar(800),
        ImageURLMedium varchar(800),
        ImageURLSmall varchar(800),
        ImageUrl varchar(800),
        Preferred INT(11) NOT NULL DEFAULT 0,
        RValue integer,
        ResizedUrl varchar(512),
        Resolution varchar(200),
        SourceKey varchar(255),
        SourceId int(11),
        primary key (ProductImageId)
    ) ENGINE=InnoDB;

    create table product_image_item (
        ProductImageItemId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        DoNotUse INT(11) NOT NULL DEFAULT 0,
        PrimaryImage INT(11) NOT NULL DEFAULT 0,
        UniqueItemId int(11) not null,
        ImageId int(11) not null,
        primary key (ProductImageItemId),
        unique (ImageId, UniqueItemId)
    ) ENGINE=InnoDB;

    create table product_motivator (
        ProductMotivatorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsDirty INT(11) DEFAULT 0,
        MotivatorScore integer,
        UniqueItemId int(11) not null,
        MotivatorId int(11) not null,
        primary key (ProductMotivatorId),
        unique (UniqueItemId, MotivatorId)
    ) ENGINE=InnoDB;

    create table product_motivator_error (
        ProductMotivatorErrorId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Error varchar(4096),
        StackTrace varchar(4096),
        UniqueItemId int(11) not null,
        MotivatorId int(11) not null,
        primary key (ProductMotivatorErrorId),
        unique (UniqueItemId, MotivatorId)
    ) ENGINE=InnoDB;

    create table question (
        QuestionId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        isActive INT(11) NOT NULL DEFAULT 0,
        Localization varchar(20),
        QuestionGroup integer,
        Question varchar(100),
        QuestionType varchar(100),
        primary key (QuestionId)
    ) ENGINE=InnoDB;

    create table questions_slider (
        QuestionsSliderId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Active varchar(50),
        GroupName integer,
        Instructions varchar(67),
        LeftSide varchar(73),
        QuestionSerial bigint,
        RightSide varchar(72),
        Scale varchar(91),
        primary key (QuestionsSliderId)
    ) ENGINE=InnoDB;

    create table review_status (
        ReviewStatusId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ReviewStatusName varchar(255) unique,
        primary key (ReviewStatusId)
    ) ENGINE=InnoDB;

    create table series (
        SeriesId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SeriesDisplayName varchar(255),
        SeriesName varchar(255),
        UseThisSeries INT(11) NOT NULL DEFAULT 0,
        SeriesTypeId int(11),
        primary key (SeriesId)
    ) ENGINE=InnoDB;

    create table series_attribute_map (
        SeriesAttributeMapId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        AttributeTypeId int(11),
        CategoryId int(11),
        SeriesTypeId int(11),
        primary key (SeriesAttributeMapId)
    ) ENGINE=InnoDB;

    create table series_type (
        SeriesTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SeriesTypeName varchar(255) unique,
        primary key (SeriesTypeId)
    ) ENGINE=InnoDB;

    create table site_status (
        SiteStatusId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SiteStatusName varchar(255),
        primary key (SiteStatusId)
    ) ENGINE=InnoDB;

    create table subdepartment (
        SubdepartmentId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        IsDirty INT(11) NOT NULL DEFAULT 1,
        SubdepartmentName varchar(255),
        SubdepartmentStatus varchar(255),
        SubdepartmentURLName varchar(255),
        primary key (SubdepartmentId)
    ) ENGINE=InnoDB;

    create table subdepartment_department (
        SubdepartmentDepartmentId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        SubdepartmentOrder integer,
        DepartmentId int(11) not null,
        SubdepartmentId int(11) not null,
        primary key (SubdepartmentDepartmentId)
    ) ENGINE=InnoDB;

    create table validation_type (
        ValidationTypeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        ValidationType varchar(255),
        primary key (ValidationTypeId)
    ) ENGINE=InnoDB;

    create table zip_code (
        ZipCodeId int(11) not null auto_increment,
        DateCreated timestamp default '0000-00-00 00:00:00',
        DateModified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        Version int(11) default 0,
        Age decimal(19,2),
        AgeFemale decimal(19,2),
        AgeMale decimal(19,2),
        Asian integer,
        Black integer,
        Cbsa integer,
        City varchar(255),
        IsDirty INT(11) NOT NULL DEFAULT 0,
        Division varchar(20),
        Female integer,
        Hawaiian integer,
        Hispanic integer,
        Household decimal(19,2),
        Income integer,
        Indian integer,
        Lat double precision,
        Lng double precision,
        Male integer,
        ObjectId varchar(24),
        Other integer,
        Population integer,
        PostalCode varchar(11),
        Region varchar(20),
        State varchar(6),
        StateName varchar(20),
        White integer,
        primary key (ZipCodeId)
    ) ENGINE=InnoDB;

    alter table accessory_value_group 
        add index FK_accessory_value_group_category (CategoryId), 
        add constraint FK_accessory_value_group_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table accessory_value_group 
        add index FK_accessory_value_group_accessory_group (AccessoryGroupId), 
        add constraint FK_accessory_value_group_accessory_group 
        foreign key (AccessoryGroupId) 
        references accessory_group (AccessoryGroupId);

    alter table accessory_value_group 
        add index FK_accessory_value_group_attribute_type (AttributeTypeId), 
        add constraint FK_accessory_value_group_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table advertiser_name_merchant 
        add index FK_advertiser_name_merchant_merchant (MerchantId), 
        add constraint FK_advertiser_name_merchant_merchant 
        foreign key (MerchantId) 
        references merchant (MerchantId);

    alter table affiliate_transaction 
        add index FK_affiliate_transaction_affiliate_report (AffiliateReportId), 
        add constraint FK_affiliate_transaction_affiliate_report 
        foreign key (AffiliateReportId) 
        references affiliate_report (AffiliateReportId);

    alter table answer 
        add index FK_answer_question (QuestionId), 
        add constraint FK_answer_question 
        foreign key (QuestionId) 
        references question (QuestionId);

    alter table attribute_formula 
        add index FK_attribute_formula_category (CategoryId), 
        add constraint FK_attribute_formula_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table attribute_formula 
        add index FK_attribute_formula_attribute_type (AttributeTypeId), 
        add constraint FK_attribute_formula_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table attribute_type 
        add index FK_attribute_type_attribute_type_source (AttributeTypeSourceId), 
        add constraint FK_attribute_type_attribute_type_source 
        foreign key (AttributeTypeSourceId) 
        references attribute_type_source (AttributeTypeSourceId);

    alter table attribute_type 
        add index FK_attribute_type_validation_type (ValidationTypeId), 
        add constraint FK_attribute_type_validation_type 
        foreign key (ValidationTypeId) 
        references validation_type (ValidationTypeId);

    alter table attribute_type 
        add index FK_attribute_type_cleanse_rule (CleanseRuleId), 
        add constraint FK_attribute_type_cleanse_rule 
        foreign key (CleanseRuleId) 
        references cleanse_rule (CleanseRuleId);

    alter table attribute_type_category 
        add index FK_attribute_type_category_category (CategoryId), 
        add constraint FK_attribute_type_category_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table attribute_type_category 
        add index FK_attribute_type_input_validation_list (InputValidationListId), 
        add constraint FK_attribute_type_input_validation_list 
        foreign key (InputValidationListId) 
        references input_validation_list (InputValidationListId);

    alter table attribute_type_category 
        add index FK_attribute_type_category_input_type (InputTypeId), 
        add constraint FK_attribute_type_category_input_type 
        foreign key (InputTypeId) 
        references input_type (InputTypeId);

    alter table attribute_type_category 
        add index FK_attribute_type_category_attribute_type (AttributeTypeId), 
        add constraint FK_attribute_type_category_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table attribute_type_history 
        add index FK_attribute_type_history_attribute_type (AttributeTypeId), 
        add constraint FK_attribute_type_history_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table attribute_type_motivator 
        add index FK_attribute_type_motivator_category (CategoryId), 
        add constraint FK_attribute_type_motivator_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table attribute_type_motivator 
        add index FK_attribute_type_motivator_attribute_type (AttributeTypeId), 
        add constraint FK_attribute_type_motivator_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table attribute_type_normalization 
        add index FK_attribute_type_normalization_category (CategoryId), 
        add constraint FK_attribute_type_normalization_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table attribute_type_normalization 
        add index FK_attribute_type_normalization_attribute_type (AttributeTypeIdNormalized), 
        add constraint FK_attribute_type_normalization_attribute_type 
        foreign key (AttributeTypeIdNormalized) 
        references attribute_type (AttributeTypeId);

    alter table attribute_type_normalization 
        add index FK_attribute_type_normalization_attribute_type_source (AttributeTypeIdSource), 
        add constraint FK_attribute_type_normalization_attribute_type_source 
        foreign key (AttributeTypeIdSource) 
        references attribute_type (AttributeTypeId);

    alter table brand 
        add index FK_brand_manufacturer (ManufacturerId), 
        add constraint FK_brand_manufacturer 
        foreign key (ManufacturerId) 
        references manufacturer (ManufacturerId);

    alter table brand_category 
        add index FK_brand_category_category (CategoryId), 
        add constraint FK_brand_category_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table brand_category 
        add index FK_brand_category_brand (BrandId), 
        add constraint FK_brand_category_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_category_itemtype_motivator 
        add index FK_brand_category_itemtype_motivator_category (CategoryId), 
        add constraint FK_brand_category_itemtype_motivator_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table brand_category_itemtype_motivator 
        add index FK_brand_category_itemtype_motivator_brand (BrandId), 
        add constraint FK_brand_category_itemtype_motivator_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_category_motivator 
        add index FK_brand_category_motivator_category (CategoryId), 
        add constraint FK_brand_category_motivator_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table brand_category_motivator 
        add index FK_brand_category_motivator_brand (BrandId), 
        add constraint FK_brand_category_motivator_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_country 
        add index FK_brand_country_brand (BrandId), 
        add constraint FK_brand_country_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_country 
        add index FK_brand_country_country (CountryId), 
        add constraint FK_brand_country_country 
        foreign key (CountryId) 
        references country (CountryId);

    alter table brand_history 
        add index FK_brand_history_brand (BrandId), 
        add constraint FK_brand_history_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_identifier 
        add index FK_brand_identifier_identifier_type (IdentifierTypeId), 
        add constraint FK_brand_identifier_identifier_type 
        foreign key (IdentifierTypeId) 
        references identifier_type (IdentifierTypeId);

    alter table brand_identifier 
        add index FK_brand_identifier_brand (BrandId), 
        add constraint FK_brand_identifier_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_motivator 
        add index FK_brand_motivator_brand (BrandId), 
        add constraint FK_brand_motivator_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_name 
        add index FK_brand_name_brand (BrandId), 
        add constraint FK_brand_name_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table brand_name 
        add index FK_brand_name_locale (LocaleId), 
        add constraint FK_brand_name_locale 
        foreign key (LocaleId) 
        references locale (LocaleId);

    alter table category 
        add index FK_category_data_source_type (DataSourceTypeId), 
        add constraint FK_category_data_source_type 
        foreign key (DataSourceTypeId) 
        references data_source_type (DataSourceTypeId);

    alter table category_category_group 
        add index FK_category_category_group_category (CategoryId), 
        add constraint FK_category_category_group_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table category_category_group 
        add index FK_category_category_group_category_group (CategoryGroupId), 
        add constraint FK_category_category_group_category_group 
        foreign key (CategoryGroupId) 
        references category_group (CategoryGroupId);

    alter table category_data_source 
        add index FK_category_data_source_category (CategoryId), 
        add constraint FK_category_data_source_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table category_data_source 
        add index FK_category_data_source_data_source_type (DataSourceTypeId), 
        add constraint FK_category_data_source_data_source_type 
        foreign key (DataSourceTypeId) 
        references data_source_type (DataSourceTypeId);

    alter table category_data_source_map 
        add index FK_category_data_source_map_category (CategoryId), 
        add constraint FK_category_data_source_map_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table category_data_source_map 
        add index FK_category_data_source_map_data_source (DataSourceTypeId), 
        add constraint FK_category_data_source_map_data_source 
        foreign key (DataSourceTypeId) 
        references data_source_type (DataSourceTypeId);

    alter table category_group 
        add index FK_category_group_navbar (NavbarId), 
        add constraint FK_category_group_navbar 
        foreign key (NavbarId) 
        references navbar (NavbarId);

    alter table category_motivator 
        add index FK_category_motivator_category (CategoryId), 
        add constraint FK_category_motivator_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table category_motivator 
        add index FK_category_motivator_motivator (MotivatorId), 
        add constraint FK_category_motivator_motivator 
        foreign key (MotivatorId) 
        references motivator (MotivatorId);

    alter table category_subdepartment 
        add index FK_category_subdepartment_category (CategoryId), 
        add constraint FK_category_subdepartment_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table category_subdepartment 
        add index FK_category_subdepartment_subdepartment (SubdepartmentId), 
        add constraint FK_category_subdepartment_subdepartment 
        foreign key (SubdepartmentId) 
        references subdepartment (SubdepartmentId);

    alter table color_primary_color 
        add index FK_color_primary_color_category (CategoryId), 
        add constraint FK_color_primary_color_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table color_primary_color 
        add index FK_color_primary_color_primary_color (PrimaryColorId), 
        add constraint FK_color_primary_color_primary_color 
        foreign key (PrimaryColorId) 
        references primary_color (PrimaryColorId);

    alter table color_primary_color 
        add index FK_color_primary_color_color (ColorId), 
        add constraint FK_color_primary_color_color 
        foreign key (ColorId) 
        references color (ColorId);

    alter table design_style_motivator 
        add index FK_design_style_motivator_category (CategoryId), 
        add constraint FK_design_style_motivator_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table functional_filter 
        add index FK_functional_filter_category (CategoryId), 
        add constraint FK_functional_filter_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table functional_filter 
        add index FK_functional_filter_locale (LocaleId), 
        add constraint FK_functional_filter_locale 
        foreign key (LocaleId) 
        references locale (LocaleId);

    alter table functional_filter 
        add index FK_functional_filter_functional_filter_type (FunctionalFilterTypeId), 
        add constraint FK_functional_filter_functional_filter_type 
        foreign key (FunctionalFilterTypeId) 
        references functional_filter_type (FunctionalFilterTypeId);

    alter table functional_filter_answer 
        add index FK_functional_filter_answer_functional_filter (FunctionalFilterId), 
        add constraint FK_functional_filter_answer_functional_filter 
        foreign key (FunctionalFilterId) 
        references functional_filter (FunctionalFilterId);

    alter table functional_filter_answer_value 
        add index FK_functional_filter_answer_value_functional_filter_answer (FunctionalFilterAnswerId), 
        add constraint FK_functional_filter_answer_value_functional_filter_answer 
        foreign key (FunctionalFilterAnswerId) 
        references functional_filter_answer (FunctionalFilterAnswerId);

    alter table functional_filter_answer_value 
        add index FK_functional_filter_attribute_type (AttributeTypeId), 
        add constraint FK_functional_filter_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table identifier_type 
        add index FK_identifier_type_attribute_type (AttributeTypeId), 
        add constraint FK_identifier_type_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table image_source 
        add index FK_image_source_image_source_type (SourceTypeId), 
        add constraint FK_image_source_image_source_type 
        foreign key (SourceTypeId) 
        references image_source_type (ImageSourceTypeId);

    alter table input_type 
        add index FK_input_type_multi_valued_type (MultiValuedTypeId), 
        add constraint FK_input_type_multi_valued_type 
        foreign key (MultiValuedTypeId) 
        references multivalued_type (MultiValuedTypeId);

    alter table input_validation_list_element 
        add index FK_input_validation_list_element_input_validation_list (InputValidationListId), 
        add constraint FK_input_validation_list_element_input_validation_list 
        foreign key (InputValidationListId) 
        references input_validation_list (InputValidationListId);

    alter table item 
        add index FK_item_category (CategoryId), 
        add constraint FK_item_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table item 
        add index FK_item_item_status (ItemStatusId), 
        add constraint FK_item_item_status 
        foreign key (ItemStatusId) 
        references item_status (ItemStatusId);

    alter table item 
        add index FK_item_brand (BrandId), 
        add constraint FK_item_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table item 
        add index FK_item_condition_type_ConditionTypeId (ConditionTypeId), 
        add constraint FK_item_condition_type_ConditionTypeId 
        foreign key (ConditionTypeId) 
        references condition_type (ConditionTypeId);

    alter table item 
        add index FK_item_site_status (SiteStatusId), 
        add constraint FK_item_site_status 
        foreign key (SiteStatusId) 
        references site_status (SiteStatusId);

    alter table item 
        add index FK_item_review_status (ReviewStatusId), 
        add constraint FK_item_review_status 
        foreign key (ReviewStatusId) 
        references review_status (ReviewStatusId);

    alter table item_attribute 
        add index FK_item_attribute_item_UniqueItemId (UniqueItemId), 
        add constraint FK_item_attribute_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_attribute 
        add index FK_item_attribute_attribute_type (AttributeTypeId), 
        add constraint FK_item_attribute_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table item_attribute_history 
        add index FK_item_attribute_history_item (UniqueItemId), 
        add constraint FK_item_attribute_history_item 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_attribute_history 
        add index FK_item_attribute_history_attribute_type (AttributeTypeId), 
        add constraint FK_item_attribute_history_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table item_changed 
        add index FKDA35C188A2A6384 (UniqueItemId), 
        add constraint FKDA35C188A2A6384 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_color 
        add index FK_item_color_item_UniqueItemId (UniqueItemId), 
        add constraint FK_item_color_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_color 
        add index FK_item_color_color (ColorId), 
        add constraint FK_item_color_color 
        foreign key (ColorId) 
        references color (ColorId);

    alter table item_color_merchant 
        add index FK_item_color_merchant_merchant (MerchantId), 
        add constraint FK_item_color_merchant_merchant 
        foreign key (MerchantId) 
        references merchant (MerchantId);

    alter table item_color_merchant 
        add index FK_item_color_merchant_item_color (ItemColorId), 
        add constraint FK_item_color_merchant_item_color 
        foreign key (ItemColorId) 
        references item_color (ItemColorId);

    alter table item_identifier 
        add index FK_item_identifier_item_UniqueItemId (UniqueItemId), 
        add constraint FK_item_identifier_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_identifier 
        add index FK_item_identifier_item_type (IdentifierTypeId), 
        add constraint FK_item_identifier_item_type 
        foreign key (IdentifierTypeId) 
        references identifier_type (IdentifierTypeId);

    alter table item_recall 
        add index FK_item_recall_item_UniqueItemId (UniqueItemId), 
        add constraint FK_item_recall_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_series 
        add index FK_item_series_item (UniqueItemId), 
        add constraint FK_item_series_item 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table item_series 
        add index FK_item_series_series (SeriesId), 
        add constraint FK_item_series_series 
        foreign key (SeriesId) 
        references series (SeriesId);

    alter table item_video 
        add index FK_item_video_item_UniqueItemId (UniqueItemId), 
        add constraint FK_item_video_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table manufacturer_identifier 
        add index FK_manufacturer_identifier_manufacturer (ManufacturerId), 
        add constraint FK_manufacturer_identifier_manufacturer 
        foreign key (ManufacturerId) 
        references manufacturer (ManufacturerId);

    alter table manufacturer_identifier 
        add index FK_manufacturer_identifier_identifier_type (IdentifierTypeId), 
        add constraint FK_manufacturer_identifier_identifier_type 
        foreign key (IdentifierTypeId) 
        references identifier_type (IdentifierTypeId);

    alter table manufacturer_name 
        add index FK_manufacturer_name_manufacturer (ManufacturerId), 
        add constraint FK_manufacturer_name_manufacturer 
        foreign key (ManufacturerId) 
        references manufacturer (ManufacturerId);

    alter table manufacturer_name 
        add index FK_manufacturer_name_locale (LocaleId), 
        add constraint FK_manufacturer_name_locale 
        foreign key (LocaleId) 
        references locale (LocaleId);

    alter table merchant 
        add index FK_merchant_network (NetworkId), 
        add constraint FK_merchant_network 
        foreign key (NetworkId) 
        references network (NetworkId);

    alter table merchant_deal 
        add index FK_merchant_deal_merchant (MerchantId), 
        add constraint FK_merchant_deal_merchant 
        foreign key (MerchantId) 
        references merchant (MerchantId);

    alter table merchant_offer 
        add index FK_merchant_offer_item_UniqueItemId (UniqueItemId), 
        add constraint FK_merchant_offer_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table merchant_offer 
        add index FK_merchant_offer_merchant_source_type (SourceTypeId), 
        add constraint FK_merchant_offer_merchant_source_type 
        foreign key (SourceTypeId) 
        references merchant_source_type (MerchantSourceTypeId);

    alter table merchant_offer 
        add index FK_merchant_offer_merchant (MerchantId), 
        add constraint FK_merchant_offer_merchant 
        foreign key (MerchantId) 
        references merchant (MerchantId);

    alter table merchant_offer 
        add index FK_merchant_offer_brand (BrandId), 
        add constraint FK_merchant_offer_brand 
        foreign key (BrandId) 
        references brand (BrandId);

    alter table merchant_offer 
        add index FK_merchant_offer_condition_type (ConditionTypeId), 
        add constraint FK_merchant_offer_condition_type 
        foreign key (ConditionTypeId) 
        references condition_type (ConditionTypeId);

    alter table merchant_offer 
        add index FK_merchant_offer_merchant_source (SourceId), 
        add constraint FK_merchant_offer_merchant_source 
        foreign key (SourceId) 
        references merchant_source (MerchantSourceId);

    alter table merchant_offer 
        add index FK_merchant_offer_review_status (ReviewStatusId), 
        add constraint FK_merchant_offer_review_status 
        foreign key (ReviewStatusId) 
        references review_status (ReviewStatusId);

    alter table merchant_offer_history 
        add index FK_merchant_offer_history_item (UniqueItemId), 
        add constraint FK_merchant_offer_history_item 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table merchant_offer_history 
        add index FK_merchant_offer_history_merchant (MerchantId), 
        add constraint FK_merchant_offer_history_merchant 
        foreign key (MerchantId) 
        references merchant (MerchantId);

    alter table merchant_offer_keyword 
        add index FK_merchant_offer_keyword_category (CategoryId), 
        add constraint FK_merchant_offer_keyword_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table merchant_offer_keyword 
        add index FK_action_type_category (ActionTypeId), 
        add constraint FK_action_type_category 
        foreign key (ActionTypeId) 
        references action_type (ActionTypeId);

    alter table merchant_source 
        add index FK_merchant_source_merchant_source_type (SourceTypeId), 
        add constraint FK_merchant_source_merchant_source_type 
        foreign key (SourceTypeId) 
        references merchant_source_type (MerchantSourceTypeId);

    alter table motivator 
        add index FK_motivator_category (CategoryId), 
        add constraint FK_motivator_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table product_detail_attribute_type 
        add index FK_product_detail_attribute_type_product_detail_group_attribute (ProductDetailGroupAttributeId), 
        add constraint FK_product_detail_attribute_type_product_detail_group_attribute 
        foreign key (ProductDetailGroupAttributeId) 
        references product_detail_group_attribute (ProductDetailGroupAttributeId);

    alter table product_detail_attribute_type 
        add index FK_product_detail_attribute_type_attribute_type (AttributeTypeID), 
        add constraint FK_product_detail_attribute_type_attribute_type 
        foreign key (AttributeTypeID) 
        references attribute_type (AttributeTypeId);

    alter table product_detail_group 
        add index FK_product_detail_group_product_detail_section (SectionId), 
        add constraint FK_product_detail_group_product_detail_section 
        foreign key (SectionId) 
        references product_detail_section (SectionId);

    alter table product_detail_group_attribute 
        add index FK_detail_group_attribute_detail_group_value_type (ProductDetailAttributeValueTypeId), 
        add constraint FK_detail_group_attribute_detail_group_value_type 
        foreign key (ProductDetailAttributeValueTypeId) 
        references product_detail_attribute_value_type (ProductDetailAttributeValueTypeId);

    alter table product_detail_group_attribute 
        add index FK_detail_group_attribute_detail_group (ProductDetailGroupId), 
        add constraint FK_detail_group_attribute_detail_group 
        foreign key (ProductDetailGroupId) 
        references product_detail_group (ProductDetailGroupId);

    alter table product_detail_part 
        add index FK_product_detail_part_product_detail_group_attribute (ProductDetailGroupAttributeId), 
        add constraint FK_product_detail_part_product_detail_group_attribute 
        foreign key (ProductDetailGroupAttributeId) 
        references product_detail_group_attribute (ProductDetailGroupAttributeId);

    alter table product_detail_part_value 
        add index FK_product_detail_part_value_product_detail_part (ProductDetailPartId), 
        add constraint FK_product_detail_part_value_product_detail_part 
        foreign key (ProductDetailPartId) 
        references product_detail_part (ProductDetailPartId);

    alter table product_detail_part_value 
        add index FK_product_detail_part_value_attribute_type (AttributeTypeId), 
        add constraint FK_product_detail_part_value_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table product_detail_section 
        add index FK_product_detail_section_category (CategoryId), 
        add constraint FK_product_detail_section_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table product_image 
        add index FK_product_image_image_source (SourceId), 
        add constraint FK_product_image_image_source 
        foreign key (SourceId) 
        references image_source (ImageSourceId);

    alter table product_image_item 
        add index FK_product_image_item_item_UniqueItemId (UniqueItemId), 
        add constraint FK_product_image_item_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table product_image_item 
        add index FK_product_image_item_product_image (ImageId), 
        add constraint FK_product_image_item_product_image 
        foreign key (ImageId) 
        references product_image (ProductImageId);

    alter table product_motivator 
        add index FK_product_motivator_item (UniqueItemId), 
        add constraint FK_product_motivator_item 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table product_motivator 
        add index FK_product_motivator_motivator (MotivatorId), 
        add constraint FK_product_motivator_motivator 
        foreign key (MotivatorId) 
        references motivator (MotivatorId);

    alter table product_motivator_error 
        add index FK_product_motivator_error_item_UniqueItemId (UniqueItemId), 
        add constraint FK_product_motivator_error_item_UniqueItemId 
        foreign key (UniqueItemId) 
        references item (UniqueItemId);

    alter table product_motivator_error 
        add index FK_product_motivator_error_motivator_MotivatorId (MotivatorId), 
        add constraint FK_product_motivator_error_motivator_MotivatorId 
        foreign key (MotivatorId) 
        references motivator (MotivatorId);

    alter table series 
        add index FK_series_series_type (SeriesTypeId), 
        add constraint FK_series_series_type 
        foreign key (SeriesTypeId) 
        references series_type (SeriesTypeId);

    alter table series_attribute_map 
        add index FK_series_attribute_map_category (CategoryId), 
        add constraint FK_series_attribute_map_category 
        foreign key (CategoryId) 
        references category (CategoryId);

    alter table series_attribute_map 
        add index FK_series_attribute_map_series_type (SeriesTypeId), 
        add constraint FK_series_attribute_map_series_type 
        foreign key (SeriesTypeId) 
        references series_type (SeriesTypeId);

    alter table series_attribute_map 
        add index FK_series_attribute_map_attribute_type (AttributeTypeId), 
        add constraint FK_series_attribute_map_attribute_type 
        foreign key (AttributeTypeId) 
        references attribute_type (AttributeTypeId);

    alter table subdepartment_department 
        add index FK_subdepartment_department_subdepartment (SubdepartmentId), 
        add constraint FK_subdepartment_department_subdepartment 
        foreign key (SubdepartmentId) 
        references subdepartment (SubdepartmentId);

    alter table subdepartment_department 
        add index FK_subdepartment_department_department (DepartmentId), 
        add constraint FK_subdepartment_department_department 
        foreign key (DepartmentId) 
        references department (DepartmentId);
