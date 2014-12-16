package com.lela.data.web.decorator;

import com.lela.data.domain.entity.AttributeType;
import org.displaytag.decorator.TableDecorator;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/14/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AttributeMotivatorDecorator extends TableDecorator {

    @Override
    public String startRow() {
        Map<Integer, Collection<AttributeType>> motivators = ( Map<Integer, Collection<AttributeType>>)this.getPageContext().getRequest().getAttribute("motivators");
        if(getListIndex()==0)
          addHeaderRow(motivators);
        return super.startRow();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public abstract void addHeaderRow(Map<Integer, Collection<AttributeType>> motivators);
}
