/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.Blog;
import com.lela.domain.dto.CategoryBlogs;
import com.lela.domain.dto.OwnerItemsQuery;
import com.lela.domain.dto.blog.BlogEntry;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 */
public interface AdministrationService {

    List<AbstractItem> processOwnersDataFromInputs(OwnerItemsQuery query);

    List<AbstractItem> processOwnersDataFromExcel(OwnerItemsQuery query, MultipartFile excelFile, HSSFWorkbook updateWorkBook);
}
