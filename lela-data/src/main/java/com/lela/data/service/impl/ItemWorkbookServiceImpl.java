package com.lela.data.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.lela.data.domain.entity.*;
import com.lela.data.excel.ItemWorkbook;
import com.lela.data.service.ItemWorkbookService;
import com.lela.data.web.commands.ItemReportingCommand;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/7/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("itemWorkbookService")
public class ItemWorkbookServiceImpl implements ItemWorkbookService, ApplicationContextAware{

    ApplicationContext ctx = null;

    @Async
    public void run(ItemWorkbook itemWorkbook)
    {
        doAsync(itemWorkbook);
    }


    private void doAsync(ItemWorkbook itemWorkbook)
    {
        runItemWorkbookInTransaction(itemWorkbook);
    }

    @Transactional
    private void runItemWorkbookInTransaction(ItemWorkbook itemWorkbook)
    {
        List<FunctionalFilter> filters = FunctionalFilterCategory.findByItemSearchCommand(itemWorkbook.getItemReportingCommand().getItemSearchCommand());
        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findByItemSearchCommand(itemWorkbook.getItemReportingCommand().getItemSearchCommand(), itemWorkbook.getItemReportingCommand().getItemWorkbookCommand());

        Category category = Category.findCategory(itemWorkbook.getItemReportingCommand().getItemSearchCommand().getCategoryId());

        List<AttributeTypeMotivator> attributeTypeMotivators = AttributeTypeMotivator.findAllAttributeTypeMotivatorByCategory(category);
        ArrayListMultimap<String, AttributeType> motivators = ArrayListMultimap.create();
        for(AttributeTypeMotivator attributeTypeMotivator:attributeTypeMotivators)
        {
            motivators.put(ItemReportingCommand.productMotivators[attributeTypeMotivator.getMotivator()-1], attributeTypeMotivator.getAttributeType());
        }
        List<ProductDetailSection> productDetailSections = ProductDetailSection.findByCategory(category);

        //new ItemWorkbook(itemReportingCommand.getItemWorkbookCommand(), itemReportingCommand.getItemSearchCommand(), filters, attributeTypeCategories, motivators).generate();
        itemWorkbook.setItemReportingCommand(itemWorkbook.getItemReportingCommand());
        itemWorkbook.setAttributeTypeCategories(attributeTypeCategories);
        itemWorkbook.setMotivators(motivators);
        itemWorkbook.setFunctionalFilters(filters);
        itemWorkbook.setProductDetailSection(productDetailSections);

        Workbook workbook = itemWorkbook.generate();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
