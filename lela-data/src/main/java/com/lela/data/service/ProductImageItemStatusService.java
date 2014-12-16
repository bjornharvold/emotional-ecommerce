package com.lela.data.service;

import com.lela.data.domain.entity.ProductImageItemStatus;
import com.lela.data.enums.ProductImageItemStatuses;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/6/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductImageItemStatusService {

    public ProductImageItemStatus getProductImageItemStatus(ProductImageItemStatuses status);

}
