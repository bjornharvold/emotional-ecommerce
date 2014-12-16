package com.lela.data.domain.dbview;

import com.lela.data.domain.dto.AttributeMotivator;
import com.lela.data.domain.dto.BaseMotivator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 7:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ViewService {

    public List<BaseMotivator> findAllBaseMotivators();
    public List<BaseMotivator> findBaseMotivatorsByCategoryAndItem(Long categoryId, Integer itemId);

    public List<AttributeMotivator> findAllAttributeMotivators();
    public List<AttributeMotivator> findAttributeMotivatorsByAttributeType(Long attributeTypeId);


}
