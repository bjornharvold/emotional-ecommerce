package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ImageSource.class)
public class ImageSourceDataOnDemand {
    @Autowired
    ImageSourceTypeDataOnDemand imageSourceTypeDataOnDemand;

    public void setImageSourceType(ImageSource obj, int index)
    {
        ImageSourceType imageSourceType = imageSourceTypeDataOnDemand.getRandomImageSourceType();
        obj.setImageSourceType(imageSourceType);
    }
}
