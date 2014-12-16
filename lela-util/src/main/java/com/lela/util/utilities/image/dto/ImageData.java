package com.lela.util.utilities.image.dto;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/17/12
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageData {

    private URL url;
    private Long productImageId;
    private Long sourceId;
    private Long productImageItemId;
    private boolean duplicate;
    private Integer height;
    private Integer width;
    private Long productImageItemStatusId;

    public ImageData(URL url, Long productImageId, Long sourceId, Long productImageItemId, Integer size, Long productImageItemStatusId) {
        this.url = url;
        this.productImageId = productImageId;
        this.sourceId = sourceId;
        this.productImageItemId = productImageItemId;
        this.productImageItemStatusId = productImageItemStatusId;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public URL getUrl() {
        return url;
    }

    public Long getProductImageId() {
        return productImageId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getProductImageItemId() {
        return productImageItemId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Long getProductImageItemStatusId() {
        return productImageItemStatusId;
    }

    public void setProductImageItemStatusId(Long productImageItemStatusId) {
        this.productImageItemStatusId = productImageItemStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageData imageData = (ImageData) o;

        if (productImageId != null ? !productImageId.equals(imageData.productImageId) : imageData.productImageId != null)
            return false;
        if (productImageItemId != null ? !productImageItemId.equals(imageData.productImageItemId) : imageData.productImageItemId != null)
            return false;
        if (sourceId != null ? !sourceId.equals(imageData.sourceId) : imageData.sourceId != null) return false;
        if (url != null ? !url.equals(imageData.url) : imageData.url != null) return false;
        //if (height != null ? !height.equals(imageData.height) : imageData.height != null) return false;
        //if (width != null ? !width.equals(imageData.height) : imageData.height != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (productImageId != null ? productImageId.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (productImageItemId != null ? productImageItemId.hashCode() : 0);
        //result = 31 * result + (height != null ? height.hashCode() : 0);
        //result = 31 * result + (width != null ? width.hashCode() : 0);
        return result;
    }


}
