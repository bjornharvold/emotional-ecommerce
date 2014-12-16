package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = CategoryDataSourceSeriesType.class)
public class CategoryDataSourceSeriesTypeDataOnDemand {

    @Autowired
    CategoryDataSourceDataOnDemand categoryDataSourceDataOnDemand;

    @Autowired
    SeriesTypeDataOnDemand seriesTypeDataOnDemand;

    public void setCategoryDataSource(CategoryDataSourceSeriesType obj, int index) {
        CategoryDataSource categoryDataSource = categoryDataSourceDataOnDemand.getSpecificCategoryDataSource(index);
        obj.setCategoryDataSource(categoryDataSource);
    }

    public void setSeriesType(CategoryDataSourceSeriesType obj, int index) {
        SeriesType seriesType = seriesTypeDataOnDemand.getSpecificSeriesType(index);
        obj.setSeriesType(seriesType);
    }

}
