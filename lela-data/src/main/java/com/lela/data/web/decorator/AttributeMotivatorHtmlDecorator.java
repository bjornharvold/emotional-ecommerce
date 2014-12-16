package com.lela.data.web.decorator;

import com.lela.data.domain.entity.AttributeType;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/14/12
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeMotivatorHtmlDecorator extends AttributeMotivatorDecorator {

    public void addHeaderRow(Map<Integer, Collection<AttributeType>> motivators) {
        try {
            this.getPageContext().getOut().write("<tr><th colspan='4'>Item Information</th>");

            for (Integer motivator : motivators.keySet()) {
                this.getPageContext().getOut().write(String.format("<th colspan='%1s'>%2s</th>", motivators.get(motivator).size(), motivator));
            }
            this.getPageContext().getOut().write("</tr>");
        }catch(IOException ioe)
        {
        //won't happen
        ioe.printStackTrace();
        }
    }
}
