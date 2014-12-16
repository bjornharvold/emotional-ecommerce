package com.lela.data.enums;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/14/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ItemReportingAttribute {

    NONE("None"),
    ALL("All"),
    LELA("Lela Researched"),
    CNET("CNET"),
    FUNCTIONAL_FILTERS("Functional Filter Attributes"),
    PRODUCT_DETAILS("Product Details"),
    COMPOSITE("Composite"),
    MOTIVATORS("Motivators");//Functional Filters, Product Details and Motivators

    String label;

    private ItemReportingAttribute(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return this.label;
    }
}
