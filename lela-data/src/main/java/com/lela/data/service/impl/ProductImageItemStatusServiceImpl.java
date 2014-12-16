package com.lela.data.service.impl;

import com.lela.data.domain.entity.ProductImageItemStatus;
import com.lela.data.enums.ProductImageItemStatuses;
import com.lela.data.service.ProductImageItemStatusService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/6/12
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProductImageItemStatusServiceImpl implements ProductImageItemStatusService {
    public ProductImageItemStatus getProductImageItemStatus(ProductImageItemStatuses status)
    {
        return status.getProductImageItemStatus();
    }
}
