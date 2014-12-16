package com.lela.commons.repository;

import java.util.List;

import com.lela.domain.document.ProductReview;

public interface ProductReviewRepositoryCustom {
	    List<ProductReview> findProductReviews(List<String> fields);
}
