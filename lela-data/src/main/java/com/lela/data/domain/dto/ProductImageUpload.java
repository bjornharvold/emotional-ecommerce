package com.lela.data.domain.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 7/13/12
 * Time: 2:02 PM
 */
public class ProductImageUpload {
    @NotNull
    private Long itemId;

    private MultipartFile file;

    @NotNull
    private String imageAngle;

    @NotNull
    private String sourceKey;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getImageAngle() {
        return imageAngle;
    }

    public void setImageAngle(String imageAngle) {
        this.imageAngle = imageAngle;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

}
