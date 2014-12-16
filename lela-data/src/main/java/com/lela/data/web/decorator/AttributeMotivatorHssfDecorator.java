package com.lela.data.web.decorator;

import com.lela.data.domain.entity.AttributeType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.displaytag.decorator.hssf.DecoratesHssf;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/14/12
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeMotivatorHssfDecorator extends AttributeMotivatorDecorator implements DecoratesHssf {
    HSSFSheet sheet;

    @Override
    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public void addHeaderRow(Map<Integer, Collection<AttributeType>> motivators) {
       if(assertRequiredState())
       {
          //sheet.shiftRows(sheet.getFirstRowNum(), sheet.getLastRowNum(), 1);
           sheet.createRow(0).createCell(0).setCellValue("MY VAL");
       }
    }

    private boolean assertRequiredState()
    {
       return this.sheet != null;
    }
}
