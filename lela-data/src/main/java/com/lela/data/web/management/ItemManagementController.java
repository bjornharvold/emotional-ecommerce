package com.lela.data.web.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.lela.data.domain.dto.ProductImageUpload;
import com.lela.data.domain.entity.*;
import com.lela.data.enums.ProductImageItemStatuses;
import com.lela.data.service.ItemSearchService;
import com.lela.data.service.ProductImageItemStatusService;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RequestMapping("/manage/items/images")
@Controller
public class ItemManagementController implements ApplicationContextAware {

    private static final String MANUAL = "MANUAL";
    private static final int PAGE_SIZE = 20;
    private static final String DEFAULT_ITEM_SEARCH = "itemSearch";
    private static final String[] acceptedImageTypes = new String[]{".jpg", ".gif", ".png"};

    final static Logger logger = LoggerFactory.getLogger(ItemManagementController.class);

    @Autowired
    @Qualifier("itemImageFileStorage")
    private FileStorage fileStorage;

    @Autowired
    @Resource(name="imageAngleMap")
    private Map imageAngleMap;

    @Autowired
    @Resource(name="manualSourceKeyMap")
    private Map manualSourceKeyMap;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private ProductImageItemStatusService productImageItemStatusService;
    /**
     * This AJAX method will mark which images are preferred to display on the product grid.
     * @param productImageItemId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/imageStatus", headers = "Accept=application/json", produces = "text/html")
    @ResponseBody
    public void setImageItemStatus(@RequestParam(required = true) Long productImageItemId, @RequestParam(required = true) Long imageStatusId, HttpServletResponse response) {

        //get the image and set it as the primary
        ProductImageItem productImageItem = ProductImageItem.findProductImageItem(productImageItemId);
        ProductImageItemStatus imageStatus = ProductImageItemStatus.findProductImageItemStatus(imageStatusId);

        if(productImageItem == null || imageStatus == null)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        else
        {
            productImageItem.setProductImageItemStatus(imageStatus);
            productImageItem.merge();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * This AJAX method will mark which images are preferred to display on the product grid.
     * @param uniqueItemId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/reviewStatus", headers = "Accept=application/json", produces = "text/html")
    @ResponseBody
    public void setItemReviewStatus(@RequestParam(required = true) Long uniqueItemId, @RequestParam(required = true) Long reviewStatusId, HttpServletResponse response) {

        //get the image and set it as the primary
        Item item = Item.findItem(uniqueItemId);
        ReviewStatus reviewStatus = ReviewStatus.findReviewStatus(reviewStatusId);

        if(item == null || reviewStatus == null)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        else
        {
            item.setReviewStatus(reviewStatus);
            item.merge();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    /**
     * This AJAX method will mark which images are preferred to display on the product grid.
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/preferred", headers = "Accept=application/json", produces = "text/html")
    @ResponseBody
    public void setPreferredImage(@RequestParam(required = true) Long id, HttpServletResponse response) {

        //get the image and set it as the primary
        ProductImage preferredProductImage = ProductImage.findProductImage(id);

        if(preferredProductImage == null)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        else
        {
            //TODO set the primary image
            preferredProductImage.setPreferred(preferredProductImage.getPreferred() == false);
            preferredProductImage.merge();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * This AJAX method will change an image to not be used
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/doNotUse", headers = "Accept=application/json", produces = "text/html")
    @ResponseBody
    public void toggleDoNotUseImage(@RequestParam(required = true) Long id, HttpServletResponse response) {

        //get the image and set it as the primary
        ProductImage doNotUseProductImage = ProductImage.findProductImage(id);
        if(doNotUseProductImage == null)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        else
        {
          //TODO implement me
          doNotUseProductImage.setDoNotUse(doNotUseProductImage.getDoNotUse() == false);
          doNotUseProductImage.merge();
          response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/api/upload", headers = "Accept=application/json", produces = "text/html")
    public String upload(@Valid ProductImageUpload productImageUpload, BindingResult bindingResult, Model uiModel, HttpServletRequest request, HttpServletResponse response) throws IOException {


        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uiModel.addAttribute("error_message","data_validation_errors");
            return "ajax-error";
        }

        if(productImageUpload.getFile() == null || productImageUpload.getFile().getBytes().length == 0)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uiModel.addAttribute("error_message","no_image_uploaded");
            return "ajax-error";
        }


        if(!StringUtils.endsWithAny(StringUtils.lowerCase(productImageUpload.getFile().getOriginalFilename()), acceptedImageTypes))
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uiModel.addAttribute("error_message", "invalid_image_type");
            uiModel.addAttribute("error_message_arguments", ArrayUtils.toString(acceptedImageTypes));
            return "ajax-error";
        }

        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(productImageUpload.getFile().getBytes()));
        }
        catch(Exception e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uiModel.addAttribute("error_message","bad_image_format");
            return "ajax-error";
        }

        byte[] file = productImageUpload.getFile().getBytes();
        String fileName = productImageUpload.getFile().getOriginalFilename();
        String extension = StringUtils.substringAfterLast(fileName, ".");
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5 = digest.digest(file);
            String hashValue = Base64.encodeBase64URLSafeString(md5);
            fileName = hashValue + "_" + Math.abs(UUID.randomUUID().getLeastSignificantBits()) + "." + extension;
        }
        catch (NoSuchAlgorithmException e)
        {
            uiModel.addAttribute("error_message","image_hash_problem");
            return "ajax-error";
        }

        Item item = Item.findItem(productImageUpload.getItemId());

        uiModel.asMap().clear();
        //create product image item

        ProductImageItem productImageItem = new ProductImageItem();
        ProductImage productImage = new ProductImage();

        //TODO this must be supplied
        ImageSource imageSource = ImageSource.findBySourceName(MANUAL);
        if(imageSource == null)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uiModel.addAttribute("error_message","missing_image_source");
            return "ajax-error";
        }
        productImage.setImageSource(imageSource);

        //TODO save the image to s3
        String s3Url = fileStorage.storeFile(new FileData(item.getCategory().getCategoryName() + "/" + fileName,
                    file, URLConnection.guessContentTypeFromName(fileName)));

        //TODO what is the url then?
        productImage.setImageUrl(s3Url);
        productImage.setDoNotUse(Boolean.FALSE);
        productImage.setPreferred(Boolean.FALSE);
        productImage.setImageAngle(productImageUpload.getImageAngle());
        productImage.setSourceKey(String.valueOf(Math.abs(UUID.randomUUID().getLeastSignificantBits())));
        productImage.setUploadReason(productImageUpload.getSourceKey());
        productImage.persist();

        productImageItem.setProductImage(productImage);
        productImageItem.setItem(item);
        productImageItem.setDoNotUse(Boolean.FALSE);
        productImageItem.setDuplicate(Boolean.FALSE);
        ProductImageItemStatus imageStatus = getProductImageItemStatusService().getProductImageItemStatus(ProductImageItemStatuses.NEW_IMAGE);
        productImageItem.setProductImageItemStatus(imageStatus);
        productImageItem.persist();


        uiModel.addAttribute("item", item);
        uiModel.addAttribute("productImageItem", productImageItem);
        uiModel.addAttribute("productImageItemStatuses", ProductImageItemStatus.findAllProductImageItemStatuses());
        return "manage/items/image";
    }

    private ProductImageItemStatus getNewImageStatus()
    {
        return ProductImageItemStatuses.NEW_IMAGE.getProductImageItemStatus();
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        // now Spring knows how to handle multipart object and convert them
    }


    @RequestMapping(value = "/category/{id}", produces = "text/html")
    public String listImagesByCategory(@PathVariable("id") Long categoryId, Integer itemId, String productModelName, Long brandId, Long itemStatusId, Long[] reviewStatusId, Boolean displayHiddenImages, Boolean itemsWithNewImages, Model uiModel, HttpServletRequest request) {
        Category category = Category.findCategory(categoryId);
        Brand brand = Brand.findBrand(brandId);
        ItemStatus itemStatus = ItemStatus.findItemStatus(itemStatusId);

        int page = determinePageFromRequest(request);
        String sort = determineSortFromRequest(request);
        String order = determineOrderFromRequest(request);

        itemsWithNewImages = itemsWithNewImages!=null?itemsWithNewImages:false;

        Map<String, String> sortMap = new HashMap<String, String>();
        sortMap.put("[mostRecentProductImageDate]","(select max(b.productImage.createdDate) from ProductImageItem b where b.item.id = o.id)");

        List<Item> items = determineItemSearchService(sort).findItems(category, brand, itemStatus, itemId, productModelName, reviewStatusId, itemsWithNewImages, page, PAGE_SIZE, sort, order);

        uiModel.addAttribute("items",items);

        uiModel.addAttribute("resultSize", Integer.valueOf((int) Item.countItemsByCategory(category, brand, itemStatus, itemId, productModelName, reviewStatusId, itemsWithNewImages)));
        uiModel.addAttribute("imageSources", ImageSource.findAllImageSources());
        uiModel.addAttribute("brands", Brand.findBrandsForACategory(category));
        uiModel.addAttribute("itemStatuses", ItemStatus.findAllItemStatuses());
        uiModel.addAttribute("productImageItemStatuses", ProductImageItemStatus.findAllProductImageItemStatuses());
        uiModel.addAttribute("reviewStatuses", ReviewStatus.findAllReviewStatuses());
        uiModel.addAttribute("categoryId", categoryId);
        uiModel.addAttribute("imageAngleMap", imageAngleMap);
        uiModel.addAttribute("manualSourceKeyMap", manualSourceKeyMap);
        uiModel.addAttribute("displayHiddenImages", displayHiddenImages);
        uiModel.addAttribute("itemsWithNewImages", itemsWithNewImages);

        addDateTimeFormatPatterns(uiModel);
        return "manage/items/images";
    }

    private String determineOrderFromRequest(HttpServletRequest request) {
        String order = "ASC"; //1=ASC, 2=DESC
        String orderName = new ParamEncoder("item").encodeParameterName(TableTagParameters.PARAMETER_ORDER);
        String orderParam = request.getParameter(orderName);
        if( orderParam != null )
        {
            order = orderParam;
            order = determineOrderFromCode(order);
        }
        return order;
    }

    private ItemSearchService determineItemSearchService(String sort)
    {
        String searchServiceId = DEFAULT_ITEM_SEARCH;

        if(sort.contains("["))
        {
            searchServiceId = StringUtils.remove(StringUtils.remove(sort, "["), "]");
        }
        return (ItemSearchService)ctx.getBean(searchServiceId);
    }

    private String determineSortFromRequest(HttpServletRequest request) {
        String sort = "o.itemId";//default sort
        String sortName = new ParamEncoder("item").encodeParameterName(TableTagParameters.PARAMETER_SORT);
        String sortParam = request.getParameter(sortName);

        if( sortParam != null )
          sort = sortParam;
        return sort;
    }

    private int determinePageFromRequest(HttpServletRequest request) {
        int page = 0; //default page
        ParamEncoder paramEncoder = new ParamEncoder("item");
        String paramName = paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_PAGE);
        String pageParam = request.getParameter(paramName);
        if( pageParam != null )
          page = (Integer.parseInt(pageParam) - 1) * PAGE_SIZE;
        return page;
    }

    private String determineOrderFromCode(String order) {
        if(order.equals("1"))
          order="ASC";
        else
          order="DESC";
        return order;
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("item_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("item_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody String handleUncaughtException(Exception ex, WebRequest request, HttpServletResponse response) throws Exception {
        logger.error(ex.getMessage(), ex);
        if (request != null && request.getHeader("X-Requested-With")!=null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            response.setHeader("Content-Type", "application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "Unknown error occurred: " + ex;
        } else {
            throw ex;
        }
    }

    public void setProductImageItemStatusService(ProductImageItemStatusService productImageItemStatusService)
    {
        this.productImageItemStatusService = productImageItemStatusService;
    }

    public ProductImageItemStatusService getProductImageItemStatusService()
    {
        return this.productImageItemStatusService;
    }

    /*
    @RequestMapping(value = "/duplicates/category/{id}", produces = "text/html")
    public String markDuplicatesByCategory(@PathVariable("id") final Long categoryId, Model uiModel, HttpServletRequest request) throws Exception{


        //new Thread(new Runnable() {
        //    public void run() {
        //        Session session = ((Session)Item.entityManager().getDelegate());
        //        session = session.getSessionFactory().openSession();
        //        Transaction tx = session.beginTransaction();

        processCategory(categoryId);
//        }).start();

        return "manage/items/images";
    }


    @Async
    private void processCategory(Long categoryId) {

        int batch = 0;
        List<Exception> exceptions = new ArrayList<Exception>();

        boolean more = true;
        while(more == true)
        {
            more = processCategory(batch, categoryId, exceptions);
            batch++;
        }
    }

    @Transactional
    public boolean processCategory(int batch, Long categoryId, List<Exception> exceptions)
    {
        int maxSize = 4;
        Category category = Category.findCategory(categoryId);
        List<Item> items = Item.findItemsByCategory(category, batch*maxSize, maxSize);
        if(items.size() == 0)
        {
            return false;
        }


        //TODO Filter out "MANUFACTURER" images
        for(Item item:items)
        {
            logger.info("Processing item:" + item.getId());
            //TODO only if images
            logger.info("Item has images:" + item.getProductImageItems().size());
            try
            {
                Map<ImageData, ProductImageItem> imageDatas = new HashMap<ImageData, ProductImageItem>();

                Collection<ProductImageItem> productImageItems = Collections2.filter(item.getProductImageItems(), new Predicate<ProductImageItem>() {
                    public boolean apply(ProductImageItem productImageItem)
                    {
                        return productImageItem.getProductImage().getImageSource().getId()!=4 &&
                                !productImageItem.getProductImage().getDoNotUse();
                    }
                });

                for(ProductImageItem productImageItem: productImageItems)
                {
                    imageDatas.put(new ImageData(new URL(productImageItem.getProductImage().getImageUrl()), productImageItem.getProductImage().getId(), productImageItem.getProductImage().getImageSource().getId(), productImageItem.getId()), productImageItem);
                }

                ImageData[] imageDataA = imageDatas.keySet().toArray(new ImageData[0]);
                ImageComparison imageComparison = new ImageComparison(imageDataA);
                imageComparison.compare();


                Set<Set<ImageData>> duplicates = imageComparison.getDuplicates();
                if(duplicates.size()>0)
                    logger.info("Duplicates found");
                else
                    logger.info("No duplicates found");
                for(Set<ImageData> duplicate:duplicates)
                {
                    for(ImageData imageData: duplicate)
                    {
                        imageDatas.get(imageData).setDuplicate(true);
                        imageDatas.get(imageData).merge();
                    }
                }


            }
            catch(Exception e)
            {
                logger.error("item: " + item.getId() + " error: " + e.getMessage(), e);
                exceptions.add(e);
            }

        }
        Item.flushSession();
        Item.clearSession();
        return true;
    }

    @RequestMapping(value = "/duplicates/item/{id}", produces = "text/html")
    public String markDuplicatesByItem(@PathVariable("id") Long uniqueItemId, Model uiModel, HttpServletRequest request) throws Exception{
        Item item = Item.findItem(uniqueItemId);
        List<Exception> exceptions = new ArrayList<Exception>();
        try
        {
            Map<ImageData, ProductImageItem> imageDatas = new HashMap<ImageData, ProductImageItem>();
            logger.info(item.getProductImageItems().size()+ " images found");
            for(ProductImageItem productImageItem: item.getProductImageItems())
            {
                imageDatas.put(new ImageData(new URL(productImageItem.getProductImage().getImageUrl()), productImageItem.getProductImage().getId(), productImageItem.getProductImage().getImageSource().getId(), productImageItem.getId()), productImageItem);
            }
            ImageData[] imageDataA = imageDatas.keySet().toArray(new ImageData[0]);
            ImageComparison imageComparison = new ImageComparison(imageDataA);
            imageComparison.compare();
            Set<Set<ImageData>> duplicates = imageComparison.getDuplicates();
            if(duplicates.size()>0)
                logger.info("Duplicates found");
            else
                logger.info("No duplicates found");
            for(Set<ImageData> duplicate:duplicates)
            {
                for(ImageData imageData: duplicate)
                {
                    imageDatas.get(imageData).setDuplicate(true);
                    imageDatas.get(imageData).merge();
                }
            }
        }
        catch(Exception e)
        {
            logger.error("item: " + item.getId() + " error: " + e.getMessage(), e);
            exceptions.add(e);
        }

        return "manage/items/images";
    }


*/
}
