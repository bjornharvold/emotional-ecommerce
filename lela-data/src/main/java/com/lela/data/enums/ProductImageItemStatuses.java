package com.lela.data.enums;

import com.lela.data.domain.entity.ProductImageItemStatus;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/28/12
 * Time: 7:28 AM
 * To change this template use File | Settings | File Templates.
 */
public enum ProductImageItemStatuses {

    NEW_IMAGE(1l), DUPLICATE_IMAGE(2l), PREFERRED_IMAGE(3l), DO_NOT_USE(4l), USE(5l);

    private Long id;
    private ProductImageItemStatuses(Long id)
    {
        this.id = id;
    }

    public ProductImageItemStatus getProductImageItemStatus()
    {
        return ProductImageItemStatus.findProductImageItemStatus(this.id);
    }

    public Long getId()
    {
        return this.id;
    }
}
