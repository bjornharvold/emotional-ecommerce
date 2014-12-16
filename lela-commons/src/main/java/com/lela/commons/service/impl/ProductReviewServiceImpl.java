package com.lela.commons.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lela.commons.LelaException;
import com.lela.commons.repository.ProductReviewRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.ProductReviewService;
import com.lela.domain.document.ProductReview;
import com.lela.util.utilities.number.NumberUtils;

@Service("productReviewService") 
public class ProductReviewServiceImpl extends AbstractCacheableService implements ProductReviewService {  
	
	private final ProductReviewRepository productReviewRepository;
	
	@Autowired
	protected ProductReviewServiceImpl(CacheService cacheService, ProductReviewRepository productReviewRepository) {
		super(cacheService);
		this.productReviewRepository = productReviewRepository;
	}
	

	@Override
	public Page<ProductReview> findProductReviews(Integer page,Integer maxResults) { 
		return productReviewRepository.findAll(new PageRequest(page, maxResults));
	}

	@Override
	public List<ProductReview> findAllProductReviews(Integer chunk) {
        List<ProductReview> result = null;
        Long count = findProductReviewCount();

        if (count != null && count > 0) {
            result = new ArrayList<ProductReview>(count.intValue());
            Integer iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findProductReviews(i, chunk).getContent());
            }
        }

        return result;
	}

	@Override
	public List<ProductReview> findProductReviews(List<String> fields) {
		return productReviewRepository.findProductReviews(fields);
	}


	@Override
	public ProductReview findProductReviewsByItemUrlName(String urlName) {
		ProductReview productReview = null;
		List<ProductReview> productReviewList = productReviewRepository.findByItemUrlName(urlName);
		if (productReviewList != null && productReviewList.size() > 0){
			if ( productReviewList.size() == 1) {
				productReview = productReviewList.get(0);
			} else {
				throw new LelaException("There are more than one ProductReview for item Url: " + urlName);
			}
		}
		return productReview;
	}


	@Override
	public ProductReview saveProductReview(ProductReview productReview) { 
		return productReviewRepository.save(productReview);
	}

	/**
	 * Saves the supplied productReviews for an item preserving uniqueness of ProductReview
	 */
	@Override
	public List<ProductReview> saveProductReviewsForAnItem(String itemUrlName, List<ProductReview> productReviewList) {
		//First make sure that dups dont make it
		Set<ProductReview> newSet = new HashSet<ProductReview>();
		for (ProductReview productReview : productReviewList) {
			newSet.add(productReview);
		}

		//Find out all product reviews for an item 
		List<ProductReview> existingProductReviewList = productReviewRepository.findByItemUrlName(itemUrlName);
		Set<ProductReview> existingSet = new HashSet<ProductReview>();
		if (existingProductReviewList != null){
			for (ProductReview pr : existingProductReviewList) {
				existingSet.add(pr);
			}
		} 
		existingSet.addAll(newSet);

		return (List<ProductReview>)productReviewRepository.save(new ArrayList<ProductReview>(existingSet));
	}


	@Override
	public Long findProductReviewCount() {
		return productReviewRepository.count();
	}


	@Override
	public List<ProductReview> saveProductReviews(List<ProductReview> list) {
		return  (List<ProductReview>) productReviewRepository.save(list);
	}
	
	@Override
	public void removeProductReview(String productReviewItemUrlName) {
		ProductReview p = findProductReviewsByItemUrlName(productReviewItemUrlName);
		productReviewRepository.delete(p);
	}
	

}
