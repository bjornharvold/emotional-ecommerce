package com.lela.data.enums;

import com.lela.data.domain.entity.ItemStatus;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/28/12
 * Time: 7:04 AM
 * To change this template use File | Settings | File Templates.
 */
public enum ItemStatuses {
    DO_NOT_INGEST(1l), INGEST_PROD_DISPLAY(2l), INGEST_PROD_DO_NOT_DISPLAY(3l), INGEST_FOR_TESTING_ONLY(4l);

    Long id;

    private ItemStatuses(Long id)
    {
        this.id = id;
    }

    public ItemStatus getItemStatus()
    {
        return ItemStatus.findItemStatus(this.id);
    }

    public Long getId()
    {
        return this.id;
    }
}
