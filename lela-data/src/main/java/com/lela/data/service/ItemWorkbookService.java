package com.lela.data.service;

import com.lela.data.excel.ItemWorkbook;
import org.springframework.scheduling.annotation.Async;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/7/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemWorkbookService {

    public void run(ItemWorkbook itemWorkbook);
}
