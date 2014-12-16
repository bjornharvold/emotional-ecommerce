package com.lela.data.web.management;

import com.lela.data.domain.dto.ProductImageUpload;
import com.lela.data.domain.entity.*;
import com.lela.data.enums.ProductImageItemStatuses;
import com.lela.data.service.ProductImageItemStatusService;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.ItemController;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import junit.framework.Assert;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.terracotta.license.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemManagementControllerIntegrationTest extends AbstractControllerTest {
    @Autowired
    ItemManagementController itemController;

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    ProductImageDataOnDemand productImageDataOnDemand;

    @Autowired
    ImageSourceTypeDataOnDemand imageSourceTypeDataOnDemand;

    @Autowired
    ReviewStatusDataOnDemand reviewStatusDataOnDemand;

    @Autowired
    ProductImageItemStatusDataOnDemand productImageItemStatusDataOnDemand;

    @Test
    @Transactional
    public void testListImagesByCategory()
    {
        Category category = categoryDataOnDemand.getRandomCategory();
        Model model = this.getModel();
        itemController.listImagesByCategory(category.getId(), null, null, null, null, new Long[]{}, true, false, model, new MockHttpServletRequest());
    }

    @Test
    @Transactional
    public void testListImagesByCategorySort()
    {
        String paramName = new ParamEncoder("item").encodeParameterName(TableTagParameters.PARAMETER_ORDER);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(paramName, "2");
        Category category = categoryDataOnDemand.getRandomCategory();
        Model model = this.getModel();
        itemController.listImagesByCategory(category.getId(), null, null, null, null, new Long[]{},  true, false, model, request);

        request.setParameter(paramName, "1");
        itemController.listImagesByCategory(category.getId(), null, null, null, null, new Long[]{}, true, false, model, request);
    }

    @Test
    @Transactional
    public void testListImagesByCategoryAndBrand()
    {
        Item item = itemDataOnDemand.getRandomItem();
        Model model = this.getModel();
        itemController.listImagesByCategory(item.getCategory().getId(), null, null, item.getBrand().getId(), null, new Long[]{},  true, false, model, new MockHttpServletRequest());
    }

    @Test
    @Transactional
    public void testListImagesByCategoryAndItemStatus()
    {
        Item item = itemDataOnDemand.getRandomItem();
        Model model = this.getModel();
        itemController.listImagesByCategory(item.getCategory().getId(), null, null, null, item.getItemStatus().getId(), new Long[]{},  true, false, model, new MockHttpServletRequest());
    }

    @Test
    @Transactional
    public void testListImagesByCategorySortByProductImageModified()
    {
        String paramName = new ParamEncoder("item").encodeParameterName(TableTagParameters.PARAMETER_SORT);
        Item item = itemDataOnDemand.getRandomItem();
        Model model = this.getModel();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(paramName, "[itemSearchDateModified]");
        itemController.listImagesByCategory(item.getCategory().getId(), null, null, null, item.getItemStatus().getId(), new Long[]{},  true, false, model, request);
    }

    @Test
    @Transactional
    public void testListImagesByCategorySortByProductImageCreated()
    {
        String paramName = new ParamEncoder("item").encodeParameterName(TableTagParameters.PARAMETER_SORT);
        Item item = itemDataOnDemand.getRandomItem();
        Model model = this.getModel();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(paramName, "[itemSearchDateCreated]");
        itemController.listImagesByCategory(item.getCategory().getId(), null, null, null, item.getItemStatus().getId(), new Long[]{},  true, false, model, request);
    }


    @Test
    @Transactional
    public void testSetDoNotUseImage()
    {
        ProductImage productImage = productImageDataOnDemand.getRandomProductImage();
        MockHttpServletResponse response = this.getHttpServletResponse();
        itemController.toggleDoNotUseImage(productImage.getId(), response);
        assertEquals("Response did not return success.", HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void testSetDoNotUseImageError()
    {
        MockHttpServletResponse response = this.getHttpServletResponse();
        itemController.toggleDoNotUseImage(-1l, response);
        assertEquals("Response did not return error.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    @Transactional
    public void testSetPreferredImage()
    {
        ProductImage productImage = productImageDataOnDemand.getRandomProductImage();
        MockHttpServletResponse response = this.getHttpServletResponse();
        itemController.setPreferredImage(productImage.getId(), response);
        assertEquals("Response did not return success.", HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    @Transactional
    public void testChangeReviewStatus()
    {
        Item item = itemDataOnDemand.getRandomItem();
        ReviewStatus reviewStatus = reviewStatusDataOnDemand.getRandomReviewStatus();

        MockHttpServletResponse response = this.getHttpServletResponse();
        itemController.setItemReviewStatus(item.getId(), reviewStatus.getId(), response);
        assertEquals("Response did not return success.", HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    @Transactional
    public void testUploadImage() throws IOException
    {
        itemController.setProductImageItemStatusService(new ProductImageItemStatusService(){
            @Override
            public ProductImageItemStatus getProductImageItemStatus(ProductImageItemStatuses status) {
                return productImageItemStatusDataOnDemand.getRandomProductImageItemStatus();
            }
        });

        itemController.setFileStorage(new FileStorage() {
            @Override
            public String absoluteUrl(String fileName) {
                return "";
            }
            @Override
            public String storeFile(FileData file) {
                return "";
            }
            @Override
            public String[] listAllBuckets() {
                return new String[0];
            }
            @Override
            public List<String> listAllObjectsInABucket(String bucketName) {
            	return null; //TODO
            }
            @Override
            public void removeObjectFromBucket(String bucketName, String objectKey) {
            	//TODO Implement as needed
            }
			@Override
			public String getObjectKeyForURL(String url) {
				// TODO Implement as needed
				return null;
			}
		    @Override
		    public String getBucketName() {
		    	//TODO: Implement as needed
		    	return null;
		    }
		    
		    @Override
		    public boolean isUseBucketAsDomain() {
		    	//TODO: Implement as needed
		    	return false;
		    }
					
        });
        ClassPathResource resource = new ClassPathResource("testdata/image/blogimage.png");
        byte[] fileData = IOUtils.readBytes(resource.getInputStream());
        MockHttpServletResponse response = this.getHttpServletResponse();
        ProductImageUpload productImageUpload = new ProductImageUpload();
        MockMultipartFile multipartFile = new MockMultipartFile("blogimage.png", "blogimage.png", "image/png", fileData);

        productImageUpload.setFile(multipartFile);
        productImageUpload.setItemId(itemDataOnDemand.getSpecificItem(0).getId());

        Model model = super.getModel();
        String result = itemController.upload(productImageUpload, super.getBindingResult(), model, super.getHttpServletRequest(), super.getHttpServletResponse());
        assertEquals("Response should contain the error message: ", "missing_image_source", model.asMap().get("error_message"));
        assertEquals("Response did not correct view.", "ajax-error", result);

        ImageSource imageSource = new ImageSource();
        imageSource.setImageSourceType(imageSourceTypeDataOnDemand.getRandomImageSourceType());
        imageSource.setSourceName("MANUAL");
        imageSource.persist();

        model = super.getModel();
        result = itemController.upload(productImageUpload, super.getBindingResult(), model, super.getHttpServletRequest(), super.getHttpServletResponse());
        assertNull("Response contains an error message: " + model.asMap().get("error_message"), model.asMap().get("error_message"));
        assertEquals("Response did not correct view.", "manage/items/image", result);

        model = super.getModel();
        multipartFile = new MockMultipartFile("blogimage.png", "blogimage.png", "image/png", new byte[]{});
        productImageUpload.setFile(multipartFile);
        result = itemController.upload(productImageUpload, super.getBindingResult(), model, super.getHttpServletRequest(), super.getHttpServletResponse());
        assertEquals("Response should contain the error message. ", "no_image_uploaded", model.asMap().get("error_message"));
        assertEquals("Response did not correct view.", "ajax-error", result);

        model = super.getModel();
        multipartFile = new MockMultipartFile("blogimage.tif", "blogimage.tif", "image/tif", fileData);
        productImageUpload.setFile(multipartFile);
        result = itemController.upload(productImageUpload, super.getBindingResult(), model, super.getHttpServletRequest(), super.getHttpServletResponse());
        assertEquals("Response should contain the error message: ", "invalid_image_type", model.asMap().get("error_message"));
        assertEquals("Response did not correct view.", "ajax-error", result);

        model = super.getModel();
        resource = new ClassPathResource("testdata/image/bad_cmyk.jpg");
        fileData = IOUtils.readBytes(resource.getInputStream());
        multipartFile = new MockMultipartFile("bad_cmyk.jpg", "bad_cmyk.jpg", "image/png", fileData);
        productImageUpload.setFile(multipartFile);
        result = itemController.upload(productImageUpload, super.getBindingResult(), model, super.getHttpServletRequest(), super.getHttpServletResponse());
        assertEquals("Response should contain the error message: ", "bad_image_format", model.asMap().get("error_message"));
        assertEquals("Response did not correct view.", "ajax-error", result);

        model = super.getModel();
        imageSource.remove();
        resource = new ClassPathResource("testdata/image/blogimage.png");
        fileData = IOUtils.readBytes(resource.getInputStream());
        multipartFile = new MockMultipartFile("blogimage.png", "blogimage.png", "image/png", fileData);
        productImageUpload.setFile(multipartFile);
    }


}
