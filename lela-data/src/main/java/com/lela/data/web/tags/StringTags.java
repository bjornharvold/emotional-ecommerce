package com.lela.data.web.tags;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/14/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringTags {

    public static String splitCamelCase(String value)
    {
        String[] splitString = StringUtils.splitByCharacterTypeCamelCase(value);
        return StringUtils.join(splitString, " ");
    }
}
