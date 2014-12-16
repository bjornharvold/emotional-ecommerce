package com.lela.commons.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lela.domain.document.ProductReview;

public interface ProductReviewService {   
    List<ProductReview> saveProductReviews(List<ProductReview> list);
    ProductReview findProductReviewsByItemUrlName(String urlName);
    ProductReview saveProductReview(ProductReview productReview); 
    Long findProductReviewCount();
    Page<ProductReview> findProductReviews(Integer page, Integer maxResults);
    List<ProductReview> findAllProductReviews(Integer chunk);
    List<ProductReview> findProductReviews(List<String> fields);
    List<ProductReview> saveProductReviewsForAnItem(String itemUrlName, List<ProductReview> productReviewList) ;
    void removeProductReview(String productReviewItemUrlName) ;
}
