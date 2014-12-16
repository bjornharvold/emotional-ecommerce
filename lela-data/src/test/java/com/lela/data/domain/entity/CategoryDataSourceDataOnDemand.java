package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = CategoryDataSource.class)
public class CategoryDataSourceDataOnDemand {
    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    DataSourceTypeDataOnDemand dataSourceDataOnDemand;

    public void setCategory(CategoryDataSource obj, int index) {
        Category category = categoryDataOnDemand.getRandomCategory();
        obj.setCategory(category);
    }

    public void setDataSourceType(CategoryDataSource obj, int index) {
        DataSourceType dataSourceType = dataSourceDataOnDemand.getRandomDataSourceType();
        obj.setDataSourceType(dataSourceType);
    }
}
