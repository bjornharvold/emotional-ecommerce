package com.lela.data.domain.dbview.impl;

import com.lela.data.domain.dbview.ViewService;
import com.lela.data.domain.dto.AttributeMotivator;
import com.lela.data.domain.dto.BaseMotivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 7:29 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ViewServiceImpl implements ViewService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<BaseMotivator> findAllBaseMotivators()
    {
        return this.jdbcTemplate.query("select * from vw_basemotivators", new BeanPropertyRowMapper<BaseMotivator>());
    }

    public List<BaseMotivator> findBaseMotivatorsByCategoryAndItem(Long categoryId, Integer itemId)
    {
        Map parameters = new HashMap();
        parameters.put("categoryId", itemId);
        parameters.put("itemId", itemId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate);

        return template.query("select * from vw_basemotivators where categoryId = :categoryId and itemId = :itemId",
                parameters, new BeanPropertyRowMapper<BaseMotivator>());
    }

    @Override
    public List<AttributeMotivator> findAllAttributeMotivators() {
        return this.jdbcTemplate.query("select * from vw_attributemotivators", new BeanPropertyRowMapper<AttributeMotivator>());
    }

    @Override
    public List<AttributeMotivator> findAttributeMotivatorsByAttributeType(Long attributeTypeId) {
        Map parameters = new HashMap();
        parameters.put("attributeTypeId", attributeTypeId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate);

        return template.query("select * from vw_attributemotivators where attributeTypeId = :attributeTypeId",
                parameters, new BeanPropertyRowMapper<AttributeMotivator>());
    }
}
