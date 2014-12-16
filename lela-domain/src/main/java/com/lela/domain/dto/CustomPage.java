/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/11/11
 * Time: 12:10 PM
 * Responsibility: Created this custom page to better support the JSPs
 *
 * @param <T> <T>
 */
public class CustomPage<T> extends PageImpl<T> {
    public CustomPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    /**
     * 
     * @param content that is on this page
     * @param startingPage - page index starts at 0
     * @param itemsPerPage - number of content items per page
     * @param totalNumberOfItems - number of total items in List<T>.
     */
    public CustomPage(List<T> content, int startingPage, int itemsPerPage, long totalNumberOfItems) {
        super(content,  new PageRequest(startingPage, itemsPerPage), totalNumberOfItems);
    }
    
    public CustomPage(List<T> content) {
        super(content);
    }

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param page page

    public CustomPage(Page<T> page) {
        super(page.getContent(), new PageRequest(page.getNumber(), page.getSize()), page.getTotalElements());
    }
    */

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public boolean getHasContent() {
        return hasContent();
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public boolean getHasNextPage() {
        return hasNextPage();
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public boolean getHasPreviousPage() {
        return hasPreviousPage();
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public boolean getIsFirstPage() {
        return isFirstPage();
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public boolean getIsLastPage() {
        return isLastPage();
    }

}
