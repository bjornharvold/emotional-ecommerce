package com.lela.commons.jobs.interceptor;

import com.lela.commons.service.CategoryService;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.comparator.FunctionalFilterComparator;
import com.lela.commons.comparator.FunctionalFilterOptionComparator;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.IngestJobInterceptorSupport;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.FunctionalFilterOption;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.util.utilities.number.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Chris Tallent
 * Date: 4/30/12
 * Time: 10:48 PM
 */
public class AbstractFunctionalFiltersIngestJobInterceptor extends IngestJobInterceptorSupport {
    /**
     * Logger
     */
    protected final static Logger log = LoggerFactory.getLogger(AllFunctionalFiltersIngestJobInterceptor.class);
    protected final ItemService itemService;
    protected final CategoryService categoryService;
    protected final FunctionalFilterService functionalFilterService;

    public AbstractFunctionalFiltersIngestJobInterceptor(ItemService itemService,
                                                         CategoryService categoryService,
                                                         FunctionalFilterService functionalFilterService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.functionalFilterService = functionalFilterService;
    }

    protected void processDepartments(Set<String> categoryGroupUrlNames, ExecutionContext context) {
        // Re-calculate options for the distinct list of processed categories
        for (String categoryGroupUrlName : categoryGroupUrlNames) {
            try {
                if (log.isInfoEnabled()) {
                    log.info(context.message("Re-calculating Filter options for department: " + categoryGroupUrlName));
                }

                // Grab all categories for the specified department
                List<FunctionalFilter> functionalFilters = functionalFilterService.findFunctionalFiltersByUrlName(categoryGroupUrlName);
                List<Category> categories = categoryService.findCategoriesByCategoryGroupUrlName(categoryGroupUrlName);
                List<Item> allItemsForDepartment = new ArrayList<Item>();

                if (categories != null && !categories.isEmpty()) {
                    for (Category category : categories) {
                        // retrieve all items for the category
                        List<Item> items = itemService.findItemsByCategoryUrlName(category.getRlnm());

                        if (items != null && !items.isEmpty()) {
                            allItemsForDepartment.addAll(items);
                        }
                    }
                }

                // Query all of the items for the functional filter
                if (!allItemsForDepartment.isEmpty()) {

                    // process
                    preProcessFunctionalFilters(context, functionalFilters, allItemsForDepartment);

                    // save
                    functionalFilterService.saveFunctionalFilters(functionalFilters);
                } else {
                    if (log.isInfoEnabled()) {
                        log.info(context.message("Skipping " + categoryGroupUrlName + ", no items were found in the database"));
                    }
                }
            } catch (Exception e) {
                // Make sure that an error on one filter doesn't cause others to break
                context.setErrorResult();
                log.error(context.message("Error processing functional filter for category: " + categoryGroupUrlName, e));
            }
        }
    }

    protected void processCategories(Set<String> categoryUrlNames, ExecutionContext context) {
        // Re-calculate options for the distinct list of processed categories
        for (String categoryUrlName : categoryUrlNames) {
            try {
                if (log.isInfoEnabled()) {
                    log.info(context.message("Re-calculating Filter options category: " + categoryUrlName));
                }

                // Query all of the items for the functional filter
                List<Item> items = itemService.findItemsByCategoryUrlName(categoryUrlName);
                if (items != null && !items.isEmpty()) {

                    List<FunctionalFilter> functionalFilters = functionalFilterService.findFunctionalFiltersByUrlName(categoryUrlName);

                    // process
                    preProcessFunctionalFilters(context, functionalFilters, items);

                    // save
                    functionalFilterService.saveFunctionalFilters(functionalFilters);
                } else {
                    if (log.isInfoEnabled()) {
                        log.info(context.message("Skipping " + categoryUrlName + ", no items were found in the database"));
                    }
                }
            } catch (Exception e) {
                // Make sure that an error on one filter doesn't cause others to break
                context.setErrorResult();
                log.error(context.message("Error processing functional filter for category: " + categoryUrlName, e));
            }
        }
    }

    /**
     * This methods creates functional filters for the specific category.
     * <p/>
     * E.g. A template tuner question of type "range" filters items on price. It assumes all items
     * have a list price associated with them so it can create the range of prices it needs.
     *
     * @param items   relevantItems
     * @param filters filters
     */
    protected void preProcessFunctionalFilters(ExecutionContext context, List<FunctionalFilter> filters, List<Item> items) {
        if ((items != null) && !items.isEmpty()) {
            for (FunctionalFilter filter : filters) {

                // loop through tuner questions and fill in runtime data
                switch (filter.getTp()) {
                    case DYNAMIC_RANGE:

                        // we have a saved value here we want to use
                        handlePreProcessDynamicRangeFilter(context, filter, items);

                        break;
                    case BRAND:

                        // here we have to populate brands so the user can filter on brands
                        handlePreProcessBrandFilter(filter, items);

                        break;
                    case STORE:

                        // here we have to populate stores so the user can filter on stores
                        handlePreProcessStoreFilter(filter, items);

                        break;
                    default:
                        if (log.isInfoEnabled()) {
                            log.info(context.message("The ingest interceptor doesn't have to process this type of filter: " + filter.getTp()));
                        }
                }

                // sort the answers
                Collections.sort(filter.getPtns(), new FunctionalFilterOptionComparator());
            }

            // sort the questions
            Collections.sort(filters, new FunctionalFilterComparator());
        }
    }

    /**
     * This is a type of filter that keeps a key to denote where to go look for numeric values
     * from which to retrieve a high and a low
     *
     * @param filter filter
     * @param items  relevantItems
     */
    private void handlePreProcessDynamicRangeFilter(ExecutionContext context, FunctionalFilter filter, List<Item> items) {
        String key = filter.getDtky();
        FunctionalFilterOption high = null;
        FunctionalFilterOption low = null;

        // first we find high and low for the range
        for (FunctionalFilterOption option : filter.getPtns()) {
            if (StringUtils.equals(option.getKy(), ApplicationConstants.DYNAMIC_RANGE_HIGH)) {
                high = option;
            } else if (StringUtils.equals(option.getKy(), ApplicationConstants.DYNAMIC_RANGE_LOW)) {
                low = option;
            }
        }

        if ((high != null) && (low != null)) {

            // calculate the current max and min scores
            // if the preset is null - we loops through the items to find the high and low values
            Double highestValue = Double.MIN_VALUE;
            Double lowestValue = Double.MAX_VALUE;

            for (Item item : items) {

                // looping through all items to find the highest high and lowest low
                if (item.getAttributes().containsKey(key)) {
                    Double itemValue = NumberUtils.safeDouble(item.getAttributes().get(key));

                    if (itemValue != null) {
                        if (highestValue.compareTo(itemValue) < 0) {
                            highestValue = itemValue;
                        }

                        if (lowestValue.compareTo(itemValue) > 0) {
                            lowestValue = itemValue;
                        }
                    } else {
                        log.warn(context.message(String.format("Item LowestPrice is null for: %s", item.getNm())));
                    }

                } else {
                    if (log.isWarnEnabled()) {
                        log.warn(context.message(String.format("Item: %s is missing key: %s", item.getNm(), key)));
                    }
                }
            }

            // we want to round the ranges to an integer. little bit higher for high and vice versa for low
            high.setVl(new Double(Math.ceil(highestValue)).intValue());
            low.setVl(new Double(Math.floor(lowestValue)).intValue());

            // sort filter options
            Collections.sort(filter.getPtns(), new FunctionalFilterOptionComparator());
        }
    }

    /**
     * The brand filter needs to be populated with the brands from all the items available
     *
     * @param filter filter
     * @param items  items
     */
    private void handlePreProcessBrandFilter(FunctionalFilter filter, List<Item> items) {

        // first we need to find all the unique brands for this list of items
        Map<String, Owner> owners = null;

        if (items != null) {
            owners = new HashMap<String, Owner>();

            for (Item item : items) {
                owners.put(item.getWnr().getRlnm(), item.getWnr());
            }
        }

        // now we have a unique list of Owners and we can start populating the filter
        if (owners != null && !owners.isEmpty()) {

            // Clear out the list of options
            filter.setPtns(new ArrayList<FunctionalFilterOption>());

            List<Owner> list = new ArrayList<Owner>(owners.values());

            for (Owner owner : list) {
                FunctionalFilterOption ffo = new FunctionalFilterOption();
                ffo.setKy(owner.getRlnm());
                ffo.setVl(owner.getNm());
                ffo.setSlctd(false);

                // add it to the filter options
                filter.getPtns().add(ffo);
            }
        }
    }

    /**
     * The store filter needs to be populated with the stores from all the items available
     *
     * @param filter filter
     * @param items  items
     */
    private void handlePreProcessStoreFilter(FunctionalFilter filter, List<Item> items) {
        // first we need to find all the unique stores for this list of items
        Map<String, AvailableInStore> stores = null;

        if (items != null) {
            stores = new HashMap<String, AvailableInStore>();

            for (Item item : items) {
                if (item.getStrs() != null && !item.getStrs().isEmpty()) {
                    for (AvailableInStore store : item.getStrs()) {
                        stores.put(store.getRlnm(), store);
                    }
                }
            }
        }

        // now we have a unique list of Owners and we can start populating the filter
        if (stores != null && !stores.isEmpty()) {

            // Clear out the list of options
            filter.setPtns(new ArrayList<FunctionalFilterOption>());

            // first sort the stores so we can add the order to the filter option
            List<AvailableInStore> list = new ArrayList<AvailableInStore>(stores.values());

            for (AvailableInStore store : list) {
                FunctionalFilterOption ffo = new FunctionalFilterOption();
                ffo.setKy(store.getRlnm());
                ffo.setVl(store.getNm());
                ffo.setSlctd(false);

                // add it to the filter options
                filter.getPtns().add(ffo);
            }
        }
    }
}
