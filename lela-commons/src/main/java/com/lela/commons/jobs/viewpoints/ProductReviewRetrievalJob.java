package com.lela.commons.jobs.viewpoints;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lela.commons.jobs.java.JavaExecutionContext;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.java.JavaJob;
import com.lela.commons.jobs.java.JavaJobDetail;
import com.lela.commons.jobs.viewpoints.domain.AttributeRating;
import com.lela.commons.jobs.viewpoints.domain.Product;
import com.lela.commons.jobs.viewpoints.domain.Review;
import com.lela.commons.jobs.viewpoints.domain.ReviewArray;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductReviewService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.ProductReview;
import com.lela.domain.document.ProductReviewDetail;

public class ProductReviewRetrievalJob extends JavaJobDetail implements JavaJob {

	private static final long serialVersionUID = -1785239249412350521L;

	private static final Logger LOG = LoggerFactory.getLogger(ProductReviewRetrievalJob.class);
	
	@Value("${viewpoints.consumer.key}")
	private String consumerKey;
	
	@Value("${viewpoints.url}")
	private String viewpointsUrl;
	
	private final String extraneousString = "{?limit,offset,include,order}";
	
	private final RestTemplate viewpointsRestTemplate;
	private final ItemService itemService;
	private final CategoryService categoryService;
	private final ProductReviewService productReviewService;
	
	@Autowired
	public ProductReviewRetrievalJob(final RestTemplate viewpointsRestTemplate, 
			final ItemService itemService,
			final CategoryService categoryService,
			final ProductReviewService productReviewService
			){
		this.viewpointsRestTemplate = viewpointsRestTemplate;
		this.itemService = itemService;
		this.categoryService = categoryService;
		this.productReviewService = productReviewService;
	}
	
	@Override
	public void execute(JavaExecutionContext context) {
		 reportInfo("Product Review Retrieval Job started...", context);

		 long found = 0;
		 long didNotFind = 0;
		 long itemCount = 0;
		 long categoryCount = 0;
		 long reviewsFound= 0;
		 double totalReviewWC = 0;
		 String authPartOfUrl = "oauth_consumer_key=" + consumerKey;
		 List<Category> categoryList = categoryService.findCategories();
		 if (categoryList != null){
			 for (Category category : categoryList) {
				 List<Item> itemList = itemService.findItemsByCategoryUrlName(category.getRlnm());
				 if (itemList != null){
					 for (Item item : itemList) {
						 if (item.getVlbl()){
							context.message("Getting upcs for available item with url:" + item.getRlnm());
							List<ProductReview> productReviewList = new ArrayList<ProductReview>();
							Set<String> upcSet = item.getUPCsForItem();
							if (upcSet != null){
								context.message("Found " + upcSet.size() + " upcs for item.");
								for (String upc : upcSet) { 
									 upc = preProcessUPC(upc);
									 String url = viewpointsUrl + String.format("/resources/products/%s.json?by=upc&", upc) + authPartOfUrl;
									 LOG.debug("Getting Product UPC using url: "  + url);
									 try {
										 Product p = viewpointsRestTemplate.getForObject(url, Product.class);
										 context.message("Found Product for URL: "  + url);
										 //Map Product to Item and place reviews on item
										 if (!StringUtils.isEmpty(p.getReviews())){
											 url = viewpointsUrl + p.getReviews() + "?" + authPartOfUrl;
											 url = StringUtils.replace(url, extraneousString, "");
											 context.message("Review url: " + url); 
											 LOG.debug("Review URL: " + url);
											 
											 ProductReview productReview = new ProductReview();
											 productReview.setTmrlnm(item.getRlnm());
											 if (p.getCommunity_urls() != null){
												 productReview.setWrl(p.getCommunity_urls().getWrite_review());
												 productReview.setPrl(p.getCommunity_urls().getProduct_page());
											 }
											 productReview.setRtg(p.getAverage_rating());
											 if (p.getAttribute_ratings() != null){
												 for (AttributeRating ar : p.getAttribute_ratings()) {
													 productReview.setAttributeRating(ar.getName(), ar.getAverage_rating());
												 }
											 }
											 productReview.setTvrvcnt(p.getActive_reviews_count()); 
											 try {
												 ReviewArray reviewArray = viewpointsRestTemplate.getForObject(url, ReviewArray.class);
												 
												 if (reviewArray != null){
													 for (Review review : reviewArray.getReviews()) { 
														 String content = this.removeMarkup(review.getContent());
														 LOG.debug(content);
														 ProductReviewDetail prd = new ProductReviewDetail();
														 if (review.getCommunity_urls() != null){
															 prd.setRlnm(review.getCommunity_urls().getReview());
														 }
														 prd.setCntnt(content);
														 prd.setCrtdt(review.getCreated_at());
														 prd.setPbdt(review.getPublished_at());
														 prd.setRtng(review.getRating());
														 prd.setSndbt(review.getSound_bite());
														 prd.setSrnm(review.getUsername());
														 prd.setUpddt(review.getUpdated_at());

														 productReview.addProductReviewDetail(prd);
														 
														 totalReviewWC += (review == null?0:(content==null?0:content.length()));
														 reviewsFound++;
													}
												 }
											 } catch (HttpClientErrorException hcee){
												 context.message("Could not find Reviews for UPC: " + upc + ", message:" +  hcee.getMessage());
											 }
											 productReviewList.add(productReview);	
										 }
										 LOG.debug("Found Product for UPC: "  + upc);
										 found++;
									 } catch (HttpClientErrorException hcee){
										 reportInfo("Could not find UPC: " + upc + ", message:" +  hcee.getMessage(), context);
										 didNotFind++;
									 }
								}
							}
							productReviewService.saveProductReviewsForAnItem(item.getRlnm(), productReviewList);
							itemCount++;
						 }
					}
				 } 
				 categoryCount++;
			}
		 }  
		 reportInfo(String.format("Processed %s UPCs with %s found and %s not found  amongst %s items in %s categories.There were %s reviews found.", found + didNotFind, found, didNotFind, itemCount, categoryCount, reviewsFound ), context);
		 reportInfo(String.format("Total Review character count is %s and average review length is %s", totalReviewWC, (totalReviewWC/reviewsFound)), context);
		 reportInfo("Job finished", context);
	}
	
    @Override
    public JavaJob getJob() {
        return this;
    }
    
    private String removeMarkup(String s) {
    	String text = s;
		if (!StringUtils.isEmpty(s)){
				text = Jsoup.parse(s).text();
		}
    	return text;
    }
    /**
     * Make the UPC valid.
     * @param upc
     * @return
     */
    private String preProcessUPC(String upc) {
    	String s = upc;
    	//UPCs should be 12 digits. So lop off the leading 0 for 13 digit UPCs, if any
    	if (s.startsWith("0") && s.length() == 13){
    		s = s.substring(1, 13);
    	}
    	return s;
    }
    
    
    private void reportInfo(String s, ExecutionContext context){
    	LOG.info(s);
    	context.message(s);
    }
    
    
}
