package com.lela.data.excel;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.lela.commons.excel.AbstractExcelWorkbook;

import com.lela.commons.service.EmailService;
import com.lela.data.domain.dto.ItemAttributeDto;
import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.domain.entity.*;
import com.lela.data.enums.*;
import com.lela.data.excel.command.ItemWorkbookCommand;
import com.lela.data.jdbc.ItemAttributeQuery;
import com.lela.data.web.commands.ItemReportingCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.transaction.annotation.Transactional;
import org.terracotta.ehcachedx.monitor.util.IOUtils;

import javax.annotation.Nullable;
import javax.persistence.NoResultException;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/2/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemWorkbook extends AbstractExcelWorkbook {

    String id;

    final static Logger logger = LoggerFactory.getLogger(ItemWorkbook.class);

    ItemReportingCommand itemReportingCommand;

    List<FunctionalFilter> functionalFilters;

    List<AttributeTypeCategory> attributeTypeCategories;

    Multimap<String, AttributeType> motivators;

    List<ProductDetailSection> productDetailSections;

    List<AttributeType> attributes;

    @Autowired
    ItemAttributeQuery itemAttributeQuery;

    private static String SHEET_NAME = "Items";
    private static final String successMessage = "Your report was generated and is attached.";
    private static final String failureMessage = "There was a problem with your report, the error message is attached.";


    private static final String TBD = "TBD";

    private List<String> header = new ArrayList<String>();
    private Set<MultiColumnTitle> topHeader = new HashSet<MultiColumnTitle>();
    private Set<MultiColumnTitle> superHeader = new HashSet<MultiColumnTitle>();
    private Set<MultiColumnTitle> superSubHeader = new HashSet<MultiColumnTitle>();
    private Set<MultiColumnTitle> sectionalHeader = new HashSet<MultiColumnTitle>();

    private static final String[] merchantOfferTitles = new String[]{"ID", "UPC", "Merchant Name", "Item Name", "Color", "Condition", "Price", "Sale Price", "URL", "Available", "Review Status", "Use This Offer", "Do Not Use", "Date Created", "Date Modified"};
    private static final String[] itemRecallTitles = new String[]{"Recall Date", "Recall Event", "Recall Incident Injuries", "Recall Severity", "Recall Units", "Recall URL"};

    int rownum;
    int colnum;

    private long totalItems;
    private long completed;
    private boolean complete = false;

    Map<String, CellStyle> styles = null;

    private static final IndexedColors[] colors = new IndexedColors[]{IndexedColors.LAVENDER, IndexedColors.LIGHT_BLUE, IndexedColors.LIGHT_GREEN, IndexedColors.LIGHT_YELLOW, IndexedColors.PINK};
    private static final IndexedColors[] childrenColors = new IndexedColors[]{IndexedColors.LAVENDER, IndexedColors.LIGHT_BLUE, IndexedColors.LIGHT_GREEN, IndexedColors.LIGHT_ORANGE, IndexedColors.PINK};

    private static final Class[] acceptableTypes = new Class[]{String.class, Long.class, Integer.class, Boolean.class };

    private static final String[] coreAttributes = new String[]{"ProductModelName", "Category", "Brand"};

    private Exception exception;

    @Autowired
    private EmailService email;

    private String errorEmail;

    byte[] output = null;

    private static final Map<Character, String> motivatorLabels = ImmutableMap.<Character, String>builder()
            .put('A', "A")
            .put('B', "B")
            .put('C', "C")
            .put('D', "D")
            .put('E', "E")
            .put('F', "F")
            .put('G', "G")
            .put('H', "Traditional")
            .put('I', "Modern")
            .put('J', "Practical")
            .put('K', "Sporty")
            .put('L', "Luxury").build();

    public ItemWorkbook(String errorEmail){
        this.id = String.valueOf(UUID.randomUUID());
        this.errorEmail = errorEmail;
    }

    @Override
    protected String getSheetName() {
        return SHEET_NAME;
    }

    @Override
    protected int[] getColumnWidths() {
        return new int[]{};
    }

    @Override
    protected String[] getTitles() {
        return header.toArray(new String[]{});
    }

    protected MultiColumnTitle[] getSuperTitles() {
        return superHeader.toArray(new MultiColumnTitle[]{});
    }

    protected MultiColumnTitle[] getSuperSubTitles() {
        return superSubHeader.toArray(new MultiColumnTitle[]{});
    }

    protected MultiColumnTitle[] getSectionTitles() {
        return sectionalHeader.toArray(new MultiColumnTitle[]{});
    }

    protected MultiColumnTitle[] getTopTitles() {
        return topHeader.toArray(new MultiColumnTitle[]{});
    }

    @Override
    @Transactional(readOnly = true)
    protected void generateContent(Workbook wb, Sheet sheet, Map<String, CellStyle> styles) {
        try
        {
            //TODO move me
            attributes = AttributeType.findByLelaResearched();
            attributes = Lists.newArrayList(Collections2.filter(attributes, new Predicate<AttributeType>() {
                public boolean apply(AttributeType attributeType)
                {
                    String propertyName = WordUtils.uncapitalize(attributeType.getLelaAttributeName());
                    return !ArrayUtils.contains(coreAttributes, attributeType.getLelaAttributeName()) && ClassUtils.hasProperty(Item.class, propertyName);
                }
            }));

            this.styles = styles;
            int batchSize = 25;
            int batch = 0;
            completed = 0;

            rownum = determineRownum();
            this.totalItems = Item.findCountItemByItemSearchCommand(itemReportingCommand.getItemSearchCommand());
            logger.debug("There are " + totalItems + " total items.");
            while(true)
            {
                boolean more = processItems(sheet, batch, batchSize);

                if(batch == 0)
                    populateTitleStreaming(sheet);

                if(!more)
                    break;

                batch ++;
                Item.flushSession();
                Item.clearSession();


                try
                {
                    logger.debug("Flushing...");
                    ((SXSSFSheet)sheet).flushRows();
                    logger.debug("Flushed...");
                }
                catch(IOException ioe)
                {
                    logger.error("Error flushing streaming report", ioe);
                }
                this.completed += batchSize;
                double percentComplete = ((double)this.completed/(double)this.totalItems)*100.0d;
                logger.debug("Completed: " + this.completed + " of " + this.totalItems);
                logger.debug("% complete: " + percentComplete);
            }

            sheet.setZoom(5, 6);
            this.complete = true;
        }
        catch(RuntimeException e)
        {
            logger.error("Couldn't proccess workbook.", e);
            this.exception = e;
            throw e;
        }
        try
        {
          finishWorkbook();
        }
        catch (IOException e)
        {
            logger.error("Couldn't email workbook to the user.", e);
            throw new RuntimeException(e);
        }
    }


    private boolean processItems(Sheet sheet, int batch, int batchSize )
    {
        Row row;
        Cell cell;
        List<Item> items = Item.findItemByItemSearchCommand(itemReportingCommand.getItemSearchCommand(), batch*batchSize, batchSize);


        logger.debug("Retrieved " + items.size() + " items from " + batch*batchSize + " fetching " + batchSize);

        if(items.size() == 0)
            return false;

        // looping through all items
        for (int i = 0; i < items.size(); i++, rownum++) {
            logger.debug("Processing " +((batch)*batchSize + i));
            row = sheet.createRow(rownum);
            row.setRowStyle(styles.get(NORMAL_STYLE_NOWRAP));
            colnum = 0;

            logger.debug("Adding Core Attributes");
            Item item = addCoreItemAttributes(row, items.get(i));



            if(itemReportingCommand.getItemWorkbookCommand().isShowBrandAttributes())
            {
                addBrandAttributes(row, item);
            }

            Map<Long, ItemAttributeDto> itemAttributeDtoMap = findItemAttributes(item);

            if(itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.ALL))
            {
                addMrfPartNoIdentifier(row, item);
                addCnetIdentifier(row, item);
                addAsinIdentifier(row, item);
                for(String motivator:ItemReportingCommand.productMotivators)
                {
                    addProductMotivator(row, item, motivator);
                }
                //show all the design style motivators
                addDesignStyleMotivators(row, item, ItemReportingCommand.designStyleMotivators, "DESIGN STYLE MOTIVATORS");
            }
            if(itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.CNET))
            {
                addCnetIdentifier(row, item);

            }
            if(itemReportingCommand.getItemWorkbookCommand().isShowMerchantOffers())
            {
                addAsinIdentifier(row, item);
            }


            if(itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.LELA))
            {
              //find common attributes
              //TODO make sure these are not displayed twice
              //TODO don't display attributes that are part of the "core" attributes
              BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);

              for(AttributeType attribute: attributes)
              {
                  String propertyName = WordUtils.uncapitalize(attribute.getLelaAttributeName());
                  try
                  {
                        Object value = wrapper.getPropertyValue(propertyName);
                        if(ArrayUtils.contains(acceptableTypes, wrapper.getPropertyType(propertyName)))
                          addColumn(row, String.valueOf(value), propertyName );
                  }
                  catch(Exception e)
                  {
                     //We couldn't get the property off the item table
                     logger.error(e.getMessage(), e);
                  }
              }
            }
            if(itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.ALL) ||
                    itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.CNET) ||
                    itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.LELA))
            {
                logger.debug("Adding Item Attributes");
                for(AttributeTypeCategory attributeTypeCategory: attributeTypeCategories)
                {
                    if(attributeTypeCategory.getAttributeType().getAttributeTypeSource().getId() == AttributeTypeSources.LELA_SPREADSHEET.getId()
                            && attributeTypeCategory.getAttributeType().getValidationType().getId() != ValidationTypes.NONE.getId()
                            && attributeTypeCategory.getAttributeType().getValidationType().getId() != ValidationTypes.DERIVED_ATTRIBUTE.getId())
                    {
                        //Don't display these attributes, they'd be displayed with LELA attributes already
                        //addColumn(row, "TBD - Get Lela Researched value from Item table", attributeTypeCategory.getAttributeType().getLelaAttributeName());
                    }
                    else
                    {
                      ItemAttributeDto itemAttribute = itemAttributeDtoMap.get(attributeTypeCategory.getAttributeType().getId());
                      addColumn(row, itemAttribute == null ? "" : itemAttribute.getItemAttributeValue(), attributeTypeCategory.getAttributeType().getLelaAttributeName());
                      //addItemAttribute(itemAttribute, attributeTypeCategory.getAttributeType(), attributeTypeCategory.getAttributeType().getLelaAttributeName(), row);
                    }
                }
            }
            if (itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.MOTIVATORS)
                    || itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.COMPOSITE))
            {
                logger.debug("Adding Item Attributes for Motivators");
                int beginColumn = colnum;
                String[] motivators = itemReportingCommand.getItemWorkbookCommand().getShowMotivator();
                if(motivators.length == 0)//default to All
                {
                    motivators = ItemReportingCommand.productMotivators;
                }
                for(final String motivator:motivators)
                {
                    int colorIndex = 0;
                    addProductMotivators(sheet, row, item, itemAttributeDtoMap, motivator, colors[colorIndex % (colors.length-1)], "MOTIVATORS");
                    colorIndex ++;
                }

                motivators = itemReportingCommand.getItemWorkbookCommand().getShowMotivator();
                if(motivators.length == 0)//default to All
                {
                    motivators = ItemReportingCommand.designStyleMotivators;
                }
                addDesignStyleMotivators(row, item, motivators, "MOTIVATORS");
                int endColumn = colnum - 1;
                this.pushSectionHeader("MOTIVATORS", beginColumn, endColumn, IndexedColors.WHITE, "MOTIVATORS");
            }
            if (itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.FUNCTIONAL_FILTERS)
                    || itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.COMPOSITE))
            {
                logger.debug("Adding Functional Filter Attributes");
                int colorIndex = 0;
                int beginColumn = colnum;
                for(FunctionalFilter functionalFilter:this.functionalFilters)
                {
                    addFunctionalFilters(sheet, row, item, itemAttributeDtoMap, functionalFilter, colors[colorIndex % (colors.length-1)], "FUNCTIONAL FILTERS");
                    colorIndex ++;
                }
                int endColumn = colnum - 1;
                this.pushSectionHeader("FUNCTIONAL FILTERS", beginColumn, endColumn, IndexedColors.WHITE, "FUNCTIONAL FILTERS");
            }
            if (itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.PRODUCT_DETAILS)
                    || itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.COMPOSITE))
            {
                logger.debug("Adding Product Detail Attributes");
                int colorIndex = 0;
                int beginColumn = colnum;

                for(ProductDetailSection section:productDetailSections)
                {

                    addProductDetails(row, itemAttributeDtoMap, section, colors[colorIndex % (colors.length-1)], childrenColors[colorIndex % (childrenColors.length-1)], "PRODUCT DETAILS");
                    colorIndex++;
                }
                int endColumn = colnum - 1;
                this.pushSectionHeader("PRODUCT DETAILS", beginColumn, endColumn, IndexedColors.WHITE, "PRODUCT DETAILS");
            }


            logger.debug("Adding Merchant Offers");
            if(itemReportingCommand.getItemWorkbookCommand().isShowMerchantOffers())
            {

                addMerchantOffer(sheet, item);
            }

            logger.debug("Adding Item Recalls");
            if(itemReportingCommand.getItemWorkbookCommand().isShowItemRecalls())
            {
                addItemRecalls(sheet, item);
            }
            //item.removeFromSession();
        }

        return true;
    }

    private void addMrfPartNoIdentifier(Row row, Item item) {
        IdentifierType mfrPartNoIdentifierType = IdentifierTypes.MRF_PART_NO.getIdentifierType();
        if(mfrPartNoIdentifierType!=null)
          addColumn(row, Joiner.on(", ").join(getItemIdentifiers(item).get(mfrPartNoIdentifierType)), mfrPartNoIdentifierType.getIdentifierTypeName());
    }

    private void addAsinIdentifier(Row row, Item item) {
        IdentifierType asinIdentifierType = IdentifierTypes.ASIN.getIdentifierType();
        if(asinIdentifierType!=null)
          addColumn(row, Joiner.on(", ").join(getItemIdentifiers(item).get(asinIdentifierType)), asinIdentifierType.getIdentifierTypeName());
    }

    private void addCnetIdentifier(Row row, Item item) {
        IdentifierType cnetIdentifierType = IdentifierTypes.CNET.getIdentifierType();
        if(cnetIdentifierType!=null)
          addColumn(row, Joiner.on(", ").join(getItemIdentifiers(item).get(cnetIdentifierType)), cnetIdentifierType.getIdentifierTypeName());
    }

    private Multimap<IdentifierType, String> getItemIdentifiers(Item item)
    {
        Multimap<IdentifierType, String> itemIdentifiers = ArrayListMultimap.create();


        if(item.getItemIdentifiers()!=null && item.getItemIdentifiers().size()>0)
        {

            for(ItemIdentifier itemIdentifier:item.getItemIdentifiers())
            {
                itemIdentifiers.put(itemIdentifier.getIdentifierType(), itemIdentifier.getIdentifierValue());
            }
            /*
            colnum = 1;
            rownum ++;
            Row headerRow = sheet.createRow(rownum);
            for(IdentifierType identifierType:itemIdentifiers.keySet())
            {
                addColumn(headerRow, identifierType.getIdentifierTypeName());
            }

            colnum = 1;
            rownum++;
            Row row = sheet.createRow(rownum);

            for(IdentifierType identifierType:itemIdentifiers.keySet())
            {
                String identifiers = StringUtils.join(itemIdentifiers.get(identifierType), ", ");
                addColumn(row, identifiers);
            }
          */
        }
        return itemIdentifiers;
    }


    private void addProductDetails(Row row, Map<Long, ItemAttributeDto> itemAttributeDtoMap, ProductDetailSection section, IndexedColors color, IndexedColors childrenColumns, String sectionName) {
        int beginProductDetailSection = colnum;

        for(ProductDetailGroup productDetailGroup: section.getProductDetailGroups())
        {


            int beginProductDetailGroup = colnum;
            for(ProductDetailGroupAttribute productDetailGroupAttribute:productDetailGroup.getProductDetailGroupAttributes())
            {
                int beginProductDetailAttributes = colnum;
                for(ProductDetailAttributeType productDetailAttributeType:productDetailGroupAttribute.getProductDetailAttributeTypes())
                {
                    ItemAttributeDto itemAttribute = itemAttributeDtoMap.get(productDetailAttributeType.getAttributeType().getId());
                    addItemAttribute(itemAttribute, productDetailAttributeType.getAttributeType(), productDetailAttributeType.getAttributeType().getLelaAttributeName() + "(ATTR)", row, itemAttributeDtoMap);
                }
                for(ProductDetailPart productDetailPart:productDetailGroupAttribute.getProductDetailParts())
                {
                    for(ProductDetailPartValue value:productDetailPart.getProductDetailPartValue())
                    {
                        ItemAttributeDto itemAttribute = itemAttributeDtoMap.get(value.getAttributeType().getId());
                        addItemAttribute(itemAttribute, value.getAttributeType(), value.getAttributeType().getLelaAttributeName() + " (PART) + " + " - " + value.getAttributeValue(), row, itemAttributeDtoMap);
                    }
                }
                if(colnum == beginProductDetailAttributes)
                {
                    //there were no item attributes at all;
                    //add a placeholder
                    addColumn(row, "", "PLACEHOLDER (DETAIL)");
                }
                int endProductDetailAttributes = colnum - 1;
                this.pushSuperSubHeader(productDetailGroupAttribute.getAttrLabel(), beginProductDetailAttributes, endProductDetailAttributes, color, sectionName);

            }
            if(colnum == beginProductDetailGroup)
            {
               addColumn(row, "", "PLACEHOLDER (GROUP)");
            }
            int endProductDetailGroup = colnum - 1;

            this.pushSuperHeader(productDetailGroup.getGroupName(), beginProductDetailGroup, endProductDetailGroup, color, sectionName);

        }
        if(colnum == beginProductDetailSection)
        {
            addColumn(row, "PLACEHOLDER (SECTION)", "");
        }
        int endProductDetailSection = colnum - 1;
        this.pushTopHeader(section.getSectionName(), beginProductDetailSection, endProductDetailSection, color, sectionName);
    }

    private int determineRownum()
    {
        if(itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.MOTIVATORS))
            return 2;
        else if( itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.FUNCTIONAL_FILTERS))
            return 3;
        else if (itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.PRODUCT_DETAILS))
            return 4;
        else if (itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.ALL))
            return 2;
        else if ( itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.COMPOSITE))
            return 5;
        else return 1;

    }

    private Map<Long, ItemAttributeDto> findItemAttributes(Item item) {
        List<ItemAttributeDto> itemAttributes = itemAttributeQuery.execute(item.getId());
        Map<Long, ItemAttributeDto> itemAttributeDtoMap = new HashMap<Long, ItemAttributeDto>(itemAttributes.size());
        for(ItemAttributeDto itemAttribute:itemAttributes)
        {
            itemAttributeDtoMap.put(itemAttribute.getAttributeItemId(), itemAttribute);
        }
        return itemAttributeDtoMap;
    }

    private void addFunctionalFilters(Sheet sheet, Row row, Item item, Map<Long, ItemAttributeDto> itemAttributeDtoMap, FunctionalFilter functionalFilter, IndexedColors color, String section)
    {
        if(functionalFilter.getFunctionalFilterAnswers().size()>0)
        {
            int beginFunctionalFilter = colnum;
            for(FunctionalFilterAnswer answer: functionalFilter.getFunctionalFilterAnswers())
            {
                int beginFunctionalFilterAnswer = colnum;
                for(FunctionalFilterAnswerValue value: answer.getFunctionalFilterAnswerValues())
                {
                    ItemAttributeDto itemAttribute = itemAttributeDtoMap.get(value.getAttributeType().getId());
                    //TODO if there is no item attribute check to see if there is an attribute formula
                    //if there is an attribute formula calculate the value and display it with a comment...
                    addItemAttribute(itemAttribute, value.getAttributeType(), value.getAttributeType().getLelaAttributeName() + ": true if " + (value.getAnswerValue().equals("true")?"equals":value.getAnswerValue()) + " " + value.getAttributeValue()  , row, itemAttributeDtoMap);
                }
                int endFunctionalFilterAnswer = colnum - 1;
                //TODO here's where the super sub header is pushed
                if(answer.getFunctionalFilterAnswerValues().size()>0)
                {
                    this.pushSuperSubHeader(answer.getAnswerKey(), beginFunctionalFilterAnswer, endFunctionalFilterAnswer, color, section);
                }
                //There were no answer values
                else
                {
                    addColumn(row, "NAV", "NAV");
                    this.pushSuperSubHeader(answer.getAnswerKey(), beginFunctionalFilterAnswer, colnum - 1, color, section);
                }
            }
            int endFunctionalFilter = colnum - 1;

            if(endFunctionalFilter - beginFunctionalFilter > 0)
            {
                //sheet.groupColumn(beginFunctionalFilter, endFunctionalFilter);
                this.pushSuperHeader(functionalFilter.getFunctionalFilterName(), beginFunctionalFilter, endFunctionalFilter, color, section);
            }
            else
            {
                addColumn(row, "N/A", "N/A");
                this.pushSuperSubHeader("N/A", beginFunctionalFilter, colnum - 1, color, section);
                this.pushSuperHeader(functionalFilter.getFunctionalFilterName(), beginFunctionalFilter, colnum - 1, color, section);
            }
        }
    }

    private void addItemAttribute(ItemAttributeDto itemAttribute, AttributeType attributeType, String title, Row row, Map<Long, ItemAttributeDto> itemAttributeDtos)
    {
            //check if attr is derived
            AttributeFormula attributeFormula = null;
            try
            {
                 attributeFormula = AttributeFormula.findAttributeFormulaByAttributeType(attributeType);
            }
            catch(RuntimeException e)
            {

            }
            addDerivedItemAttribute(attributeType, title, row, itemAttributeDtos, attributeFormula);
            if(attributeFormula != null)
                title = "(derived)" + title;
            if (attributeType.getValidationType().equals(ValidationTypes.DERIVED_ATTRIBUTE))
            {
                if( itemAttribute !=null)
                    addColumn(row, itemAttribute.getItemAttributeValue(), title);
                else
                    addColumn(row, "value was null", title);
            }
            else if (itemAttribute == null)
            {
                addColumn(row, "value was null", title);
            }
            else
            {
                addColumn(row, itemAttribute.getItemAttributeValue(), title);
            }
    }

    private void addDerivedItemAttribute(AttributeType attributeType, String title, Row row, Map<Long, ItemAttributeDto> itemAttributeDtos, AttributeFormula attributeFormula) {
            if(attributeFormula != null)
            {
                addColumn(row, attributeFormula.getWhereClause(), "(where clause) for " + title );

                for(AttributeFormulaAttributeType attributeFormulaAttributeType:attributeFormula.getAttributeFormulaAttributeTypes())
                {
                    ItemAttributeDto itemAttributeDto = itemAttributeDtos.get(attributeFormulaAttributeType.getAttributeType().getId());
                    if(itemAttributeDto != null)
                      addColumn(row, itemAttributeDto.getItemAttributeValue(), "(source) for " + title + " from " + attributeFormulaAttributeType.getAttributeType().getLelaAttributeName());
                    else
                      addColumn(row, "null", "(source) for " + title + " from " + attributeFormulaAttributeType.getAttributeType().getLelaAttributeName());
                }

            }
    }


    private void addProductMotivators(Sheet sheet, Row row, Item item, Map<Long, ItemAttributeDto> itemAttributeDtoMap, final String motivator, IndexedColors color, String section) {
        Collection<AttributeType> attributeTypes = motivators.get(motivator);

        int beginMotivatorColumn = colnum;
        addProductMotivator(row, item, motivator);
        for(AttributeType attributeType:attributeTypes)
        {
            ItemAttributeDto itemAttribute = itemAttributeDtoMap.get(attributeType.getId());
            addColumn(row, itemAttribute==null?"null":itemAttribute.getItemAttributeValue(), motivator + " - " + attributeType.getLelaAttributeName());
        }
        int endMotivatorColumn = colnum - 1;
        //sheet.groupColumn(beginMotivatorColumn, endMotivatorColumn);
        String title = motivatorLabels.get(motivator.length()>0?motivator.charAt(0):"unknown");
        if(title == null)
            title = "Undefined Motivator";
        this.pushSuperSubHeader(title, beginMotivatorColumn, endMotivatorColumn, color, section);
    }

    private void addDesignStyleMotivators(Row row, Item item, String[] motivators, String section)
    {
        DesignStyleMotivator designStyleMotivator = null;

        try
        {
            designStyleMotivator = DesignStyleMotivator.findDesignStyleMotivatorsByItem(item);
        }
        catch(EmptyResultDataAccessException e)
        {
            logger.error(e.getMessage(), e);
            designStyleMotivator = new DesignStyleMotivator();
            designStyleMotivator.setH(1);
            designStyleMotivator.setI(1);
            designStyleMotivator.setJ(1);
            designStyleMotivator.setK(1);
            designStyleMotivator.setL(1);
        }


        int beginColumn = colnum;
        if(ArrayUtils.contains(motivators, "H"))
          addColumn(row, ObjectUtils.defaultIfNull(designStyleMotivator.getH(), 1), motivatorLabels.get('H'));

        if(ArrayUtils.contains(motivators, "I"))
          addColumn(row, ObjectUtils.defaultIfNull(designStyleMotivator.getI(), 1), motivatorLabels.get('I'));

        if(ArrayUtils.contains(motivators, "J"))
          addColumn(row, ObjectUtils.defaultIfNull(designStyleMotivator.getJ(), 1), motivatorLabels.get('J'));

        if(ArrayUtils.contains(motivators, "K"))
          addColumn(row, ObjectUtils.defaultIfNull(designStyleMotivator.getK(), 1), motivatorLabels.get('K'));

        if(ArrayUtils.contains(motivators, "L"))
          addColumn(row, ObjectUtils.defaultIfNull(designStyleMotivator.getL(), 1), motivatorLabels.get('L'));
        int endColumn = colnum-1;

        this.pushSuperSubHeader("Design Style", beginColumn, endColumn, IndexedColors.LIGHT_TURQUOISE, section);
    }

    private void addProductMotivator(Row row, Item item, final String motivator) {
        ProductMotivator productMotivator = null;
        if(item.getProductMotivators()!=null && item.getProductMotivators().size()>0)
        {
            try
            {
                productMotivator = Iterables.find(item.getProductMotivators(),
                        new Predicate<ProductMotivator>() {
                            @Override
                            public boolean apply(@Nullable ProductMotivator o) {
                                return o.getMotivator().getMotivatorLabel() == motivator.charAt(0);
                            }
                        });
            }
            catch(NoSuchElementException notFound)
            {
                //it's ok if it's not found
            }

        }
        addColumn(row, productMotivator==null?"":productMotivator.getMotivatorScore(), "Motivator Score : " + motivator);
    }

    private void addMerchantOffer(Sheet sheet, Item item) {
        Row row;
        if(item.getMerchantOffers()!=null && item.getMerchantOffers().size()>0)
        {
            rownum ++;
            colnum = 1;
            row = sheet.createRow(rownum);
            addHeaderRow(row, merchantOfferTitles);

            for (MerchantOffer offer: item.getMerchantOffers())
            {
                rownum++;
                colnum = 1;//indent
                row = sheet.createRow(rownum);
                row.setRowStyle(styles.get(NORMAL_CENTERED));

                addColumn(row, offer.getId());

                addColumn(row, offer.getUpc());

                addColumn(row, offer.getMerchantName());

                addColumn(row, offer.getOfferItemName());

                addColumn(row, offer.getColor());

                addColumn(row, offer.getConditionType().getConditionTypeName());

                addColumn(row, offer.getOfferPrice());

                addColumn(row, offer.getOfferSalePrice());

                addColumn(row, offer.getOfferUrl());

                addColumn(row, offer.getAvailable());

                addColumn(row, offer.getReviewStatus().getReviewStatusName());

                addColumn(row, offer.getUseThisOffer());

                addColumn(row, offer.getDoNotUse());

                addColumn(row, offer.getDateCreated());

                addColumn(row, offer.getDateModified());
            }
            sheet.groupRow(rownum - item.getMerchantOffers().size(), rownum);
            //sheet.setRowGroupCollapsed(rownum - item.getMerchantOffers().size(), true);
        }
    }

    private void addItemRecalls(Sheet sheet, Item item) {
        Row row;Cell cell;
        if(item.getItemRecalls() != null && item.getItemRecalls().size()>0)
        {
            rownum ++;
            colnum = 1;
            row = sheet.createRow(rownum);
            addHeaderRow(row, itemRecallTitles);

            for (ItemRecall itemRecall: item.getItemRecalls())
            {
                rownum++;
                colnum = 1;

                row = sheet.createRow(rownum);
                cell = row.createCell(0);
                row.setRowStyle(styles.get(NORMAL_CENTERED));

                addColumn(row, itemRecall.getRecallDate());

                addColumn(row, itemRecall.getRecallHazard());

                addColumn(row, itemRecall.getRecallEvent());

                addColumn(row, itemRecall.getRecallIncidentsInjuries());

                addColumn(row, itemRecall.getRecallSeverity());

                addColumn(row, itemRecall.getRecallUnits());

                addColumn(row, itemRecall.getRecallUrl());
            }
            sheet.groupRow(rownum - item.getItemRecalls().size(), rownum);
            //sheet.setRowGroupCollapsed(rownum - item.getItemRecalls().size(), true);
        }
    }

    private Item addCoreItemAttributes(Row row, Item item) {
        //id
        addColumn(row, item.getId(), "Unique ID");

        //id
        addColumn(row, item.getCategory().getCategoryName(), "Category Name");

        //itemid
        addColumn(row, item.getItemId(), "Item ID");

        //productModelName
        addColumn(row, item.getProductModelName(), "Product Model Name");

        //brand name
        addColumn(row, item.getBrand().getBrandName(), "Brand");

        //item is on latest
        //TODO not good to hard code here:
        addColumn(row, item.getItemStatus().getId()== ItemStatuses.INGEST_PROD_DISPLAY.getId() || item.getItemStatus().getId()== ItemStatuses.INGEST_FOR_TESTING_ONLY.getId(), "Item is on Latest");

        //item is on www
        //TODO not good to hard code here:
        addColumn(row, item.getItemStatus().getId()==ItemStatuses.INGEST_PROD_DISPLAY.getId(), "Item is on WWW");

        //item review status
        addColumn(row, item.getReviewStatus().getReviewStatusName(), "Item Review Status");

        //item has offers with valid merchant offers (merchant_offer.ReviewStatusId = 3)
        addColumn(row, MerchantOffer.countValidMerchantOfferByItem(item)>0?"true":"false", "Has Merchant Offers");

        //item has valid images
        //preferred, used
        Predicate validImagePredicate = new Predicate<ProductImageItem>() {
            public boolean apply(ProductImageItem productImageItem)
            {
                return productImageItem.getProductImageItemStatus().getId() == ProductImageItemStatuses.PREFERRED_IMAGE.getId()
                        || productImageItem.getProductImageItemStatus().getId() == ProductImageItemStatuses.USE.getId();
            }
        };

        addColumn(row, ProductImageItem.countValidProductImageItemsByItem(item)>0? "true" : "false", "Has Valid Images");

        //item researched by lela
        addColumn(row, this.isLelaResearched(item), "Researched by Lela");

        //item has product motivators
        addColumn(row, item.getProductMotivators() != null && item.getProductMotivators().size()>0, "Product Motivators");

        //item's motivators are "UNIQUE" or "BRAND AVERAGED"
        addColumn(row, this.isLelaResearched(item) && (item.getProductMotivators() == null || item.getProductMotivators().size() == 0)?"Brand Averaged":"Unique", "Motivator Type");


        IdentifierType upcIdentifierType = IdentifierTypes.UPC.getIdentifierType();
        if(upcIdentifierType!=null)
          addColumn(row, Joiner.on(", ").join(getItemIdentifiers(item).get(upcIdentifierType)), upcIdentifierType.getIdentifierTypeName());

        return item;
    }

    private void addBrandAttributes(Row row, Item item) {
        //facebook url
        addColumn(row, item.getBrand().getFacebookUrl(), "Facebook URL");

        addColumn(row, item.getBrand().getFacebookAccess(), "Facebook Access");

        addColumn(row, item.getBrand().getFacebookLikes(), "Facebook Likes");

        addColumn(row, item.getBrand().getTwitterUrl(), "Twitter URL");

        addColumn(row, item.getBrand().getTwitterFollowers(), "Twitter Followers");

        addColumn(row, item.getBrand().getTwitterName(), "Twitter Name");

        addColumn(row, item.getBrand().getUrlname(), "Brand URL Name");

        addColumn(row, item.getBrand().getManufacturer()!=null?item.getBrand().getManufacturer().getManufacturerName():item.getBrand().getManufacturer(), "Manufacturer Name");

    }

    private boolean isLelaResearched(Item item)
    {
        return item.getResearchComplete();
    }

    private void addHeaderRow(Row row, String[] titles) {
        Cell cell;
        row.setHeightInPoints(12.75f);
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(colnum);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(styles.get(HEADER_STYLE));
            colnum ++;
        }
    }

    private void addColumn(Row row, Object value, String title) {

        pushHeader(title);
        addColumn(row, value);
    }

    private void addColumn(CellStyle style, Row row, Object value, String title) {

        pushHeader(title);
        addColumn(style, row, value);
    }

    private void addColumn(Row row, Object value) {
        addColumn(styles.get(NORMAL_STYLE_NOWRAP), row, value);
    }

    private void addColumn(CellStyle style, Row row, Object value) {
        Cell cell;
        cell = row.createCell(colnum);
        cell.setCellValue(StringUtils.defaultString(String.valueOf(value)));
        cell.setCellStyle(style);

        colnum ++;
    }

    private void pushHeader(String title)
    {
        if (rownum == determineRownum())
        {
            header.add(title);
        }
    }

    private void pushSuperHeader(String title, int from, int to, IndexedColors color, String section)
    {
        superHeader.add(new MultiColumnTitle(title, from, to, color, section));
    }

    private void pushSuperSubHeader(String title, int from, int to, IndexedColors color, String section)
    {
        superSubHeader.add(new MultiColumnTitle(title, from, to, color, section));
    }

    private void pushTopHeader(String title, int from, int to, IndexedColors color, String section)
    {
        topHeader.add(new MultiColumnTitle(title, from, to, color, section));
    }

    private void pushSectionHeader(String title, int from, int to, IndexedColors color, String section)
    {
        sectionalHeader.add(new MultiColumnTitle(title, from, to, color, section));
    }

    public void setAttributeTypeCategories(List<AttributeTypeCategory> attributeTypeCategories) {
        this.attributeTypeCategories = attributeTypeCategories;
    }

    @Override
    protected void populateTitle(Map<String, CellStyle> styles, Sheet sheet) {}

    protected void populateTitleStreaming(Sheet sheet) {

        String[] titles = getTitles();

        int firstRow = determineRownum();

        Row headerRow = sheet.createRow(firstRow - 1);

        headerRow.setHeightInPoints(12.75f);

        CellStyle style = this.getHeaderStyle(IndexedColors.LIGHT_ORANGE);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(style);
            //sheet.autoSizeColumn(i);
        }

        //always the index 0 row
        MultiColumnTitle[] superTitles = getSuperTitles();
        addGroupedHeaderRow(sheet, firstRow - 3, superTitles);

        //Always the index 1 row
        MultiColumnTitle[] superSubTitles = getSuperSubTitles();
        addGroupedHeaderRow(sheet, firstRow - 2, superSubTitles);

        MultiColumnTitle[] topTitles = getTopTitles();
        addGroupedHeaderRow(sheet, firstRow - 4, topTitles);

        if( itemReportingCommand.getItemWorkbookCommand().getShowAttribute().equals(ItemReportingAttribute.COMPOSITE))
        {
            MultiColumnTitle[] sectionTitles = getSectionTitles();
            addGroupedHeaderRow(sheet, firstRow - 5, sectionTitles);
        }

        sheet.createFreezePane(0, firstRow);
    }

    private void addGroupedHeaderRow(Sheet sheet, int rownum, MultiColumnTitle[] topTitles) {
        if(topTitles.length > 0)
        {
            CellStyle style = this.getHeaderStyle(IndexedColors.LIGHT_ORANGE);
            Row subHeaderRow = sheet.createRow(rownum);
            subHeaderRow.setHeightInPoints(13.0f);

            for(int i = 0; i <= getTitles().length; i++)
            {
                Cell cell = subHeaderRow.createCell(i);
                style = this.getHeaderStyle(IndexedColors.WHITE);
                cell.setCellStyle(style);
            }


            for(MultiColumnTitle title:topTitles)
            {
                for(int i = title.getFrom(); i <= title.getTo(); i++)
                {
                    Cell cell = subHeaderRow.getCell(i);
                    style = this.getHeaderStyle(title.getColor());
                    cell.setCellStyle(style);
                }

                Cell cell = subHeaderRow.createCell(title.getFrom());//.getCell(title.getFrom());
                cell.setCellValue(title.getTitle());

                if(title.getTo() - title.getFrom() > 0)
                {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, title.getFrom(), title.getTo()));
                }
                //CellStyle style = this.getHeaderStyle(title.getColor());
                cell.setCellStyle(style);
            }
        }
    }

    private class MultiColumnTitle
    {
        private int from;
        private int to;
        private String title;
        private IndexedColors color;
        private String section;

        public MultiColumnTitle(String title, int from, int to, IndexedColors color, String section)
        {
            this.title = title;
            this.from = from;
            this.to = to;
            this.color = color;
            this.section = section;
        }

        public String getTitle()
        {
            return this.title;
        }

        public int getFrom()
        {
            return this.from;
        }

        public int getTo()
        {
            return this.to;
        }

        public IndexedColors getColor()
        {
            return color;
        }

        public String getSection()
        {
            return section;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MultiColumnTitle that = (MultiColumnTitle) o;

            if (from != that.from) return false;
            if (to != that.to) return false;
            if (!title.equals(that.title)) return false;
            if (!section.equals(that.section)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = from;
            result = 31 * result + to;
            result = 31 * result + title.hashCode();
            result = 31 * result + section.hashCode();
            return result;
        }
    }


    protected Workbook newWorkbook()
    {
        SXSSFWorkbook wb = new SXSSFWorkbook(-1);
        wb.setCompressTempFiles(true);
        return wb;
    }

    public CellStyle getHeaderStyle(IndexedColors color)
    {
        CellStyle style = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    public void finishWorkbook() throws IOException
    {
        File temp = null;
        String filename = "";
        if(exception == null)
        {
            filename = itemReportingCommand.getFilename() + ".xlsx";
            temp = File.createTempFile(itemReportingCommand.getFilename(), ".xlsx");
            this.write(new FileOutputStream(temp));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(new FileInputStream(temp), out);
            output = out.toByteArray();
        }
        else
        {
            filename = "error.txt";
            temp = File.createTempFile("error", ".txt");
            exception.printStackTrace(new PrintStream(new FileOutputStream(temp)));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(new FileInputStream(temp), out);
            output = out.toByteArray();
        }

        if (StringUtils.isNotBlank(itemReportingCommand.getRecipientEmailAddress()) && this.itemReportingCommand.getItemWorkbookCommand().isEmailWhenComplete())
        {
            email.sendEmailWithAttachment(itemReportingCommand.getRecipientEmailAddress(), successMessage, temp, filename);
        }
    }

    public void setItemReportingCommand(ItemReportingCommand itemReportingCommand) {
        this.itemReportingCommand = itemReportingCommand;
    }

    public void setMotivators(Multimap<String, AttributeType> motivators) {
        this.motivators = motivators;
    }

    public void setFunctionalFilters(List<FunctionalFilter> functionalFilters) {
        this.functionalFilters = functionalFilters;
    }

    public void setProductDetailSection(List<ProductDetailSection> productDetailSections) {
        this.productDetailSections = productDetailSections;
    }

    public String getId() {
        return id;
    }

    public String getFilename()
    {
       return itemReportingCommand.getFilename();
    }

    public double getCompletePercentage()
    {
        double perComplete = 0.0;
        if(this.totalItems > 0)
          perComplete = ((double)this.completed/(double)this.totalItems)*100.0d;
        if(perComplete > 100.0)
          perComplete = 100.0;
        if(complete)
          perComplete = 100.0;
        return perComplete;
    }

    private void write(OutputStream outputStream) throws IOException
    {
        wb.write(outputStream);
    }

    public void writeCached(OutputStream outputStream) throws IOException
    {
        outputStream.write(output);
    }

    public ItemReportingCommand getItemReportingCommand()
    {
        return itemReportingCommand;
    }

    public Exception getException()
    {
        return exception;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public long getCompleted() {
        return completed;
    }

    public boolean isComplete() {
        return complete;
    }
}
