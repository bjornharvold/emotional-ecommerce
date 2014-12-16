package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ItemVideo.class)
public class ItemVideoDataOnDemand {

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    ReviewStatusDataOnDemand reviewStatusDataOnDemand;

    @Autowired
    VideoTypeDataOnDemand videoTypeDataOnDemand;

    public void setItem(ItemVideo obj, int index)
    {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }

    public void setReviewStatus(ItemVideo obj, int index) {
        ReviewStatus reviewStatus = reviewStatusDataOnDemand.getSpecificReviewStatus(index);
        obj.setReviewStatus(reviewStatus);
    }

    public void setVideoType(ItemVideo obj, int index) {
        VideoType videoType = videoTypeDataOnDemand.getRandomVideoType();
        obj.setVideoType(videoType);
    }

}
