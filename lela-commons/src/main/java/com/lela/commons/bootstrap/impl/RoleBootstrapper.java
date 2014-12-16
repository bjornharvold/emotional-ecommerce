/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.RoleService;
import com.lela.domain.document.Role;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required roles into the system
 */
@SuppressWarnings("unchecked")
@Component("roleBootstrapper")
public class RoleBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(RoleBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/roles.xml");

    @Value("${bootstrapper.role.enabled:true}")
    private Boolean enabled;

    private final RoleService roleService;

    @Autowired
    public RoleBootstrapper(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " roles in db");
        log.info("Omitted " + omitted + " roles from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseXMLFile());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private List<Role> parseXMLFile() throws Exception {
        List<Role> result = new ArrayList<Role>();

        SAXReader reader = new SAXReader();
        Document document = reader.read(file.getInputStream());
        document.normalize();

        List<Element> roles = document.selectNodes("//roles/role");

        for (Element e : roles) {
            Role role = null;

            String name = e.elementText("name");
            String urlName = e.elementText("urlname");
            String description = e.elementText("description");
            String statusCode = e.elementText("statusCode");

            role = new Role();
            role.setNm(name);
            role.setRlnm(urlName);

            Element rightsElement = e.element("rights");

            if (rightsElement != null) {

                List<Element> rightElementList = rightsElement.elements("right");

                if (rightElementList != null && !rightElementList.isEmpty()) {
                    for (Element element : rightElementList) {
                        String rightStatusCode = element.attributeValue("statusCode");

                        if (StringUtils.isNotBlank(rightStatusCode)) {
                            role.getRghts().add(rightStatusCode);
                        }
                    }
                }
            }

            result.add(role);

        }

        return result;
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param roles roles
     * @throws BootstrapperException
     *
     */
    private void persist(List<Role> roles) throws BootstrapperException {
        List<Role> dbList = new ArrayList<Role>();

        try {

            for (Role role : roles) {
                Role tmp = roleService.findRoleByUrlName(role.getRlnm());

                if (tmp == null) {
                    log.info("Creating role with name: " + role.getNm());
                    dbList.add(role);
                    populated++;
                } else if (tmp.getRghts().size() != role.getRghts().size()) {
                    log.info("Updating role with name: " + role.getNm());
                    tmp.setRghts(role.getRghts());
                    dbList.add(tmp);
                    populated++;
                } else {
                    log.info("Role already exists with status code: " + role.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                roleService.saveRoles(dbList);
            }
        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "RoleBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}