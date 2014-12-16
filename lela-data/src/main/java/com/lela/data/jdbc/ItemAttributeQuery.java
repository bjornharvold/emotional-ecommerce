package com.lela.data.jdbc;

import com.lela.data.domain.dto.ItemAttributeDto;
import com.lela.data.domain.entity.ItemAttribute;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.MappingSqlQueryWithParameters;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/10/12
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemAttributeQuery extends MappingSqlQuery<ItemAttributeDto> {

    public ItemAttributeQuery(DataSource ds) {
        super(ds, "SELECT ItemAttributeId, UniqueItemId, AttributeValue, AttributeTypeId from item_attribute WHERE UniqueItemId = ?");
        super.declareParameter(new SqlParameter("UniqueItemId", Types.INTEGER));
        compile();
    }

    public ItemAttributeDto mapRow(ResultSet rs, int rowNumber) throws SQLException {
        ItemAttributeDto itemAttribute = new ItemAttributeDto();
        itemAttribute.setUniqueItemId(Long.valueOf((Integer) rs.getObject("UniqueItemId")));
        itemAttribute.setItemAttributeValue(rs.getString("AttributeValue"));
        itemAttribute.setAttributeItemId(Long.valueOf((Integer)rs.getObject("AttributeTypeId")));
        return itemAttribute;
    }
}
