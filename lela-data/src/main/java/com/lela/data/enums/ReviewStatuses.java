package com.lela.data.enums;

import com.lela.data.domain.entity.ItemStatus;
import com.lela.data.domain.entity.ReviewStatus;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/20/12
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ReviewStatuses {
    NOT_TESTED(1l), FAILED(2l), PASSED(3l), IN_PROGRESS(4l), SMALL_PHOTOS(5l), OUT_OF_STOCK_OR_PRICE_ISSUE(6l);
    Long id;

    private ReviewStatuses(Long id)
    {
        this.id = id;
    }

    public ReviewStatus getReviewStatus()
    {
        return ReviewStatus.findReviewStatus(this.id);
    }

    public Long getId()
    {
        return this.id;
    }
}
