// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ImageSource;
import com.lela.data.domain.entity.ImageSourceType;

privileged aspect ImageSource_Roo_JavaBean {
    
    public ImageSourceType ImageSource.getImageSourceType() {
        return this.imageSourceType;
    }
    
    public void ImageSource.setImageSourceType(ImageSourceType imageSourceType) {
        this.imageSourceType = imageSourceType;
    }
    
    public String ImageSource.getSourceName() {
        return this.sourceName;
    }
    
    public void ImageSource.setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    
}