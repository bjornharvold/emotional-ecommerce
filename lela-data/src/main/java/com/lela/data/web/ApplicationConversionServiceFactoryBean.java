package com.lela.data.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lela.commons.service.CategoryService;
import com.lela.data.domain.document.ItemAttributes;
import com.lela.data.domain.entity.*;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {


	private String dateFormat;

    private CategoryService categoryService;

	@Override
	protected void installFormatters(FormatterRegistry registry) {
        super.installFormatters(registry);
		// Register application converters and formatters
        registry.addConverter(getItemAttributesToStringConverter());
        registry.addConverter(getCategoryCategoryGroupToStringConverter());
        registry.addConverter(getItemToStringConverter());
        registry.addConverter(getManufacturerToStringConverter());
        registry.addConverter(getBrandToStringConverter());
        registry.addConverter(getConditionTypeToStringConverter());
        registry.addConverter(getMerchantSourceToStringConverter());
        registry.addConverter(getMerchantSourceTypeToStringConverter());
        registry.addConverter(getReviewStatusToStringConverter());
        registry.addConverter(getMerchantOfferToStringConverter());
        registry.addConverter(getNetworkToStringConverter());
        registry.addConverter(getItemColorToStringConverter());
        registry.addConverter(getImageSourceTypeToStringConverter());
        registry.addConverter(getImageSourceToStringConverter());
        registry.addConverter(getItemIdentifierToStringConverter());
        registry.addConverter(getIdentifierTypeToStringConverter());
        registry.addConverter(getCategoryCategoryGroupToStringConverter());
        registry.addConverter(getProductDetailGroupToStringConverter());
        registry.addConverter(getProductDetailGroupAttributeToStringConverter());
        registry.addConverter(getProductDetailSectionToStringConverter());
        registry.addConverter(getPrimaryColorToStringConverter());
        registry.addConverter(getAttributeTypeMotivatorToStringConverter());
        registry.addConverter(getInputValidationListToStringConverter());
        registry.addConverter(getAttributeTypeCategoryToStringConverter());
        registry.addConverter(getMotivatorToStringConverter());
        registry.addConverter(getQuestionToStringConverter());
        registry.addConverter(getCategoryGroupToStringConverter());
        registry.addConverter(getNavbarToStringConverter());
        registry.addConverter(getAccessoryValueGroupToStringConverter());
        registry.addConverter(getAccessoryGroupToStringConverter());
        registry.addConverter(getSubdepartmentToStringConverter());
        registry.addConverter(getDepartmentToStringConverter());
        registry.addConverter(getObjectIdToStringConverter());
        registry.addConverter(getStringToObjectIdConverter());
        registry.addConverter(getStringToDateConverter());
        registry.addConverter(getDateToStringConverter());
        registry.addConverter(this.getCustomStringToCategoryConverter());
        registry.addConverter(this.getCategoryToStringConverter());
        
        

	}

    public Converter<ImageSource, String> getImageSourceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ImageSource, java.lang.String>() {
            public String convert(ImageSource imageSource) {
                return imageSource.getSourceName();
            }
        };
    }

    public Converter<ImageSourceType, String> getImageSourceTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ImageSourceType, java.lang.String>() {
            public String convert(ImageSourceType imageSourceType) {
                return imageSourceType.getSourceTypeName();
            }
        };
    }

    public Converter<ItemColor, String> getItemColorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ItemColor, java.lang.String>() {
            public String convert(ItemColor itemColor) {
                return itemColor.getColor().getColorName();
            }
        };
    }

    public Converter<Network, String> getNetworkToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Network, java.lang.String>() {
            public String convert(Network network) {
                return network.getNetworkName();
            }
        };
    }

    public Converter<MerchantSource, String> getMerchantSourceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.MerchantSource, java.lang.String>() {
            public String convert(MerchantSource merchantSource) {
                return merchantSource.getSourceName();
            }
        };
    }

    public Converter<Merchant, String> getMerchantToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Merchant, java.lang.String>() {
            public String convert(Merchant merchant) {
                return merchant.getMerchantName();
            }
        };
    }

    public Converter<MerchantSourceType, String> getMerchantSourceTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.MerchantSourceType, java.lang.String>() {
            public String convert(MerchantSourceType merchantSourceType) {
                return merchantSourceType.getSourceTypeName();
            }
        };
    }

    public Converter<MerchantOffer, String> getMerchantOfferToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.MerchantOffer, java.lang.String>() {
            public String convert(MerchantOffer merchantOffer) {
                return merchantOffer.getOfferItemName();
            }
        };
    }

    public Converter<ReviewStatus, String> getReviewStatusToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ReviewStatus, java.lang.String>() {
            public String convert(ReviewStatus reviewStatus) {
                return reviewStatus.getReviewStatusName();
            }
        };
    }

    public Converter<ConditionType, String> getConditionTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ConditionType, java.lang.String>() {
            public String convert(ConditionType conditionType) {
                return conditionType.getConditionTypeName();
            }
        };
    }

    public Converter<Brand, String> getBrandToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Brand, java.lang.String>() {
            public String convert(Brand brand) {
                return brand.getBrandName();
            }
        };
    }

    public Converter<Manufacturer, String> getManufacturerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Manufacturer, java.lang.String>() {
            public String convert(Manufacturer manufacturer) {
                return manufacturer.getManufacturerName();
            }
        };
    }
    public Converter<InputType, String> getInputTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.InputType, java.lang.String>() {
            public String convert(InputType inputType) {
                return inputType.getInputTypeName();
            }
        };
    }

    public Converter<MultiValuedType, String> getMultiValuedTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.MultiValuedType, java.lang.String>() {
            public String convert(MultiValuedType multiValuedType) {
                return multiValuedType.getMultiValuedTypeName();
            }
        };
    }

    public Converter<AttributeType, String> getAttributeTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.AttributeType, java.lang.String>() {
            public String convert(AttributeType attributeType) {
                return attributeType.getAttributeName();
            }
        };
    }

    public Converter<Item, String> getItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Item, java.lang.String>() {
            public String convert(Item item) {
                return item.getLelaUrl();
            }
        };
    }
    public Converter<Color, String> getColorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Color, java.lang.String>() {
            public String convert(Color color) {
                return color.getColorName();
            }
        };
    }

    public Converter<DataSourceType, String> getDataSourceTypeToStringConverter() {
        return new Converter<DataSourceType, String>() {
            public String convert(DataSourceType dataSourceType) {
                return dataSourceType.getDataSourceTypeName();
            }
        };
    }

    public Converter<ItemAttribute, String> getItemAttributeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ItemAttribute, java.lang.String>() {
            public String convert(ItemAttribute itemAttribute) {
                return itemAttribute.getAttributeType().getAttributeName() + "=" + itemAttribute.getAttributeValue();
            }
        };
    }

    public Converter<Category, String> getCategoryToStringConverter() {
        return new Converter<Category, java.lang.String>() {
            public String convert(Category category) {
                return category.getCategoryName();
            }
        };
    }


    public Converter<String,com.lela.domain.document.Category> getCustomStringToCategoryConverter() {
        return new Converter<String, com.lela.domain.document.Category>() {
            public com.lela.domain.document.Category convert(String string) {
            	com.lela.domain.document.Category result = null;
                if (StringUtils.isNotBlank(string)) {
                    result = categoryService.findCategoryById(new ObjectId(string));
                }
                return result;
            }
        };
    }


    public Converter<AttributeTypeSource, String>getAttributeTypeSourceToStringConverter()
    {
        return new Converter<AttributeTypeSource, String>() {
            public String convert(AttributeTypeSource attributeTypeSource)
            {
                return attributeTypeSource.getAttributeTypeSourceName();
            }
        };
    }

    public Converter<ValidationType, String>getValidationTypeToStringConverter()
    {
        return new Converter<ValidationType, String>() {
            public String convert(ValidationType validationType)
            {
                return validationType.getValidationTypeName();
            }
        };
    }

    public Converter<ItemAttributes, String> getItemAttributesToStringConverter()
    {
        return new Converter<ItemAttributes, String>() {
            public String convert(ItemAttributes itemAttributes)
            {
              return itemAttributes.toString();
            }
        };
    }


    public Converter<CategoryCategoryGroup, String> getCategoryCategoryGroupToStringConverter()
    {
        return new Converter<CategoryCategoryGroup, String>() {
            public String convert(CategoryCategoryGroup categoryCategoryGroup)
            {
                return categoryCategoryGroup.getCategory().getCategoryName() + " - " + categoryCategoryGroup.getCategoryGroup().getCategoryGroupName() + "-" + categoryCategoryGroup.getCategoryGroup().getCategoryGroupStatus();
            }
        };
    }

    public Converter<FunctionalFilterAnswer, String> getFunctionalFilterAnswerToStringConverter()
    {
        return new Converter<FunctionalFilterAnswer, String>() {
            public String convert(FunctionalFilterAnswer functionalFilterAnswer)
            {
                return "key:" + functionalFilterAnswer.getAnswerKey() + " - value: " + functionalFilterAnswer.getAnswerValue();
            }
        };
    }
    public Converter<FunctionalFilterType, String> getFunctionalFilterTypeToStringConverter()
    {
        return new Converter<FunctionalFilterType, String>() {
            public String convert(FunctionalFilterType functionalFilterType)
            {
                return functionalFilterType.getFunctionalFilterTypeName();
            }
        };
    }

    public Converter<FunctionalFilter, String> getFunctionalFilterToStringConverter()
    {
        return new Converter<FunctionalFilter, String>() {
            public String convert(FunctionalFilter functionalFilter)
            {
                return functionalFilter.getFunctionalFilterName();
            }
        };
    }

    public Converter<Locale, String> getLocaleToStringConverter()
    {
        return new Converter<Locale, String>() {
            public String convert(Locale locale)
            {
                return locale.getLocaleName();
            }
        };
    }

    public Converter<CleanseRule, String> getCleanseRuleToStringConverter()
    {
        return new Converter<CleanseRule, String>() {
            public String convert(CleanseRule cleanseRule)
            {
                return cleanseRule.getCleanseRuleValue();
            }
        };
    }

    public Converter<ItemIdentifier, String> getItemIdentifierToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ItemIdentifier, java.lang.String>() {
            public String convert(ItemIdentifier itemIdentifier) {
                return itemIdentifier.getIdentifierValue();
            }
        };
    }

    public Converter<IdentifierType, String> getIdentifierTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.IdentifierType, java.lang.String>() {
            public String convert(IdentifierType identifierType) {
                return identifierType.getIdentifierTypeName() + ( identifierType.getMultiValued()?": (multivalued)":"" );
            }
        };
    }

    public Converter<ProductDetailGroup, String> getProductDetailGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ProductDetailGroup, java.lang.String>() {
            public String convert(ProductDetailGroup productDetailGroup) {
                return productDetailGroup.getGroupName() + "(" + productDetailGroup.getGroupOrder() + ")";
            }
        };
    }

    public Converter<ProductDetailGroupAttribute, String> getProductDetailGroupAttributeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ProductDetailGroupAttribute, java.lang.String>() {
            public String convert(ProductDetailGroupAttribute productDetailGroupAttribute) {
                return productDetailGroupAttribute.getAttrName();
            }
        };
    }

    public Converter<ProductDetailSection, String> getProductDetailSectionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.ProductDetailSection, java.lang.String>() {
            public String convert(ProductDetailSection productDetailSection) {
                return productDetailSection.getSectionName();
            }
        };
    }

    public Converter<PrimaryColor, String> getPrimaryColorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.PrimaryColor, java.lang.String>() {
            public String convert(PrimaryColor primaryColor) {
                return primaryColor.getPrimaryColorName();
            }
        };
    }

    public Converter<AttributeTypeMotivator, String> getAttributeTypeMotivatorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.AttributeTypeMotivator, java.lang.String>() {
            public String convert(AttributeTypeMotivator attributeTypeMotivator) {
                return attributeTypeMotivator.getAttributeType().getAttributeName() + " - " + attributeTypeMotivator.getMotivator();
            }
        };
    }

    public Converter<InputValidationList, String> getInputValidationListToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.InputValidationList, java.lang.String>() {
            public String convert(InputValidationList inputValidationList) {
                return inputValidationList.getListName();
            }
        };
    }

    public Converter<AttributeTypeCategory, String> getAttributeTypeCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.AttributeTypeCategory, java.lang.String>() {
            public String convert(AttributeTypeCategory attributeTypeCategory) {
                return attributeTypeCategory.getCategory().getCategoryName() + " " + attributeTypeCategory.getAttributeType().getAttributeName();
            }
        };
    }

    public Converter<Motivator, String> getMotivatorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Motivator, java.lang.String>() {
            public String convert(Motivator motivator) {
                return motivator.getCategory().getCategoryName() + " - " + motivator.getMotivatorLabel();
            }
        };
    }

    public Converter<Question, String> getQuestionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Question, java.lang.String>() {
            public String convert(Question question) {
                return question.getQuestionText();
            }
        };
    }

    public Converter<CategoryGroup, String> getCategoryGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.CategoryGroup, java.lang.String>() {
            public String convert(CategoryGroup categoryGroup) {
                return categoryGroup.getCategoryGroupName();
            }
        };
    }

    public Converter<Navbar, String> getNavbarToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Navbar, java.lang.String>() {
            public String convert(Navbar navbar) {
                return navbar.getRlnm();
            }
        };
    }

    public Converter<AccessoryValueGroup, String> getAccessoryValueGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.AccessoryValueGroup, java.lang.String>() {
            public String convert(AccessoryValueGroup accessoryValueGroup) {
                return accessoryValueGroup.getAccessoryGroup().getAccessoryGroupName() + " - " + accessoryValueGroup.getAttributeType().getAttributeName() + " - " + accessoryValueGroup.getAttributeValue();
            }
        };
    }

    public Converter<AccessoryGroup, String> getAccessoryGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.AccessoryGroup, java.lang.String>() {
            public String convert(AccessoryGroup accessoryGroup) {
                return accessoryGroup.getAccessoryGroupName();
            }
        };
    }

    public Converter<Subdepartment, String> getSubdepartmentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Subdepartment, java.lang.String>() {
            public String convert(Subdepartment subdepartment) {
                return subdepartment.getSubdepartmentName();
            }
        };
    }

    public Converter<Department, String> getDepartmentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.lela.data.domain.entity.Department, java.lang.String>() {
            public String convert(Department department) {
                return department.getDepartmentName();
            }
        };
    }
    public Converter<ObjectId, String> getObjectIdToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ObjectId, java.lang.String>() {
            public String convert(ObjectId objectId) {
            	String s = null;
            	if (objectId != null){
            		s = objectId.toString();
            	}
                return s;
            }
        };
    }
    public Converter<String, ObjectId> getStringToObjectIdConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ObjectId>() {
            public ObjectId convert(java.lang.String string) {
            	ObjectId oid = null;
            	if (StringUtils.isNotBlank(string)){
            		oid = new ObjectId(string);
            	}
                return oid;
            }
        };
    }

    public Converter<String, Date> getStringToDateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, java.util.Date>() {
            public Date convert(java.lang.String string) {
                Date result = null;
                if (StringUtils.isNotBlank(string)) {
                	DateTimeFormatter df = DateTimeFormat.forPattern(dateFormat);
                    result = df.parseDateTime(string).toDate();
                }
                return result;
            }
        };
    }

    public Converter<Date, String> getDateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Date, java.lang.String>() {
            public java.lang.String convert(Date date) {
                String result = null;
                if (date != null) {
                	SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                    result = df.format(date);
                }
                return result;
            }
        };
    }


	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

}
