/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 2/13/12
 * Time: 10:50 PM
 * Responsibility:
 */
@Document
public class NavigationBar extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 5790068089199615858L;

    /**
     * url name
     */
    @NotNull
    @NotEmpty
    private String rlnm;

    /**
     * groups
     */
    private List<CategoryGroup> grps;

    /**
     * Leaf categories (Calculated)
     */
    private List<String> ctgrrlnms;

    @Transient
    private List<Category> ctgrs;

    /**
     * Locale
     */
    @NotNull
    private Locale lcl;

    @NotNull
    private Boolean dflt;

    public List<CategoryGroup> getGrps() {
        return grps;
    }

    public void setGrps(List<CategoryGroup> grps) {
        this.grps = grps;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Locale getLcl() {
        return lcl;
    }

    public void setLcl(Locale lcl) {
        this.lcl = lcl;
    }

    public Boolean getDflt() {
        return dflt;
    }

    public void setDflt(Boolean dflt) {
        this.dflt = dflt;
    }

    public List<String> getCtgrrlnms() {
        return ctgrrlnms;
    }

    public void setCtgrrlnms(List<String> ctgrrlnms) {
        this.ctgrrlnms = ctgrrlnms;
    }

    public List<Category> getCtgrs() {
        return ctgrs;
    }

    public void setCtgrs(List<Category> ctgrs) {
        this.ctgrs = ctgrs;
    }

    /**
     * Method supports retrieving a categorygroup at any level
     *
     * @param categoryGroupId categoryGroupId
     * @return
     */
    public CategoryGroup findCategoryGroupById(String categoryGroupId) {
        CategoryGroup result = null;

        if (this.grps != null && !this.grps.isEmpty()) {
            result = findCategoryGroupById(this.grps, null, categoryGroupId);
        }

        return result;
    }

    /**
     * Method supports retrieving a categorygroup at any level
     *
     * @param categoryGroupUrlName categoryGroupUrlName
     * @return
     */
    public CategoryGroup findCategoryGroupByUrlName(String categoryGroupUrlName) {
        CategoryGroup result = null;

        if (this.grps != null && !this.grps.isEmpty()) {
            result = findCategoryGroupByUrlName(this.grps, null, categoryGroupUrlName);
        }

        return result;
    }

    private CategoryGroup findCategoryGroupById(List<CategoryGroup> groups, String parentCategoryGroupId, String categoryGroupId) {
        CategoryGroup result = null;

        for (CategoryGroup cg : groups) {
            if (StringUtils.equals(cg.getD(), categoryGroupId)) {
                cg.setNavigationBarUrlName(getRlnm());
                cg.setParentCategoryGroupId(parentCategoryGroupId);

                result = cg;
                break;
            }

            if (cg.getChldrn() != null && !cg.getChldrn().isEmpty()) {
                result = findCategoryGroupById(cg.getChldrn(), cg.getD(), categoryGroupId);

                if (result != null) {
                    break;
                }
            }
        }

        return result;
    }

    private CategoryGroup findCategoryGroupByUrlName(List<CategoryGroup> groups, String parentCategoryGroupId, String categoryGroupUrlName) {
        CategoryGroup result = null;

        for (CategoryGroup cg : groups) {
            if (StringUtils.equals(cg.getRlnm(), categoryGroupUrlName)) {
                cg.setNavigationBarUrlName(getRlnm());
                cg.setParentCategoryGroupId(parentCategoryGroupId);

                result = cg;
                break;
            }

            if (cg.getChldrn() != null && !cg.getChldrn().isEmpty()) {
                result = findCategoryGroupById(cg.getChldrn(), cg.getD(), categoryGroupUrlName);

                if (result != null) {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Recursively add a category group
     *
     * @param cg cg
     * @return True if the category group was added
     */
    public boolean addCategoryGroup(CategoryGroup cg) {
        CategoryGroup dupe = null;

        if (this.grps == null) {
            this.grps = new ArrayList<CategoryGroup>();
        }

        if (StringUtils.isBlank(cg.getD())) {
            cg.setD(new BigInteger(130, new SecureRandom()).toString(32));
        }

        if (StringUtils.isNotBlank(cg.getParentCategoryGroupId())) {
            // this means this group needs to be attached to a nested category group
            CategoryGroup parent = findCategoryGroupById(cg.getParentCategoryGroupId());

            if (parent.getChldrn() == null) {
                parent.setChldrn(new ArrayList<CategoryGroup>());
            }

            for (CategoryGroup qq : parent.getChldrn()) {
                if (StringUtils.equals(qq.getD(), cg.getD())) {
                    dupe = qq;
                    break;
                }
            }

            // overwrite original
            if (dupe != null) {
                parent.getChldrn().remove(dupe);
            }

            parent.getChldrn().add(cg);
        } else {

            for (CategoryGroup qq : this.grps) {
                if (StringUtils.equals(qq.getD(), cg.getD())) {
                    dupe = qq;
                    break;
                }
            }

            // overwrite original
            if (dupe != null) {
                this.grps.remove(dupe);
            }

            this.grps.add(cg);
        }

        return true;
    }

    public boolean removeCategoryGroup(String categoryGroupId) {
        boolean result = false;

        if (this.grps != null && !this.grps.isEmpty()) {
            result = removeCategoryGroup(this.grps, categoryGroupId);
        }

        return result;
    }

    /**
     * Recursively search and remove a category group from a nested list
     *
     * @param groups          groups
     * @param categoryGroupId categoryGroupId
     * @return Whether the list entry was deleted or not
     */
    private boolean removeCategoryGroup(List<CategoryGroup> groups, String categoryGroupId) {
        CategoryGroup toRemove = null;
        boolean result = false;

        for (CategoryGroup cg : groups) {
            if (StringUtils.equals(cg.getD(), categoryGroupId)) {
                toRemove = cg;
                break;
            }

            if (cg.getChldrn() != null && !cg.getChldrn().isEmpty()) {
                result = removeCategoryGroup(cg.getChldrn(), categoryGroupId);

                if (result) {
                    break;
                }
            }
        }

        if (toRemove != null) {
            groups.remove(toRemove);
            result = true;
        }

        return result;
    }

    public static void main(String[] args) {
        List<CategoryGroup> groups = new ArrayList<CategoryGroup>();
        CategoryGroup parent = new CategoryGroup();
        parent.setNm("Parent");
        parent.setRlnm("parent");
        parent.setD("parent");
        groups.add(parent);
        List<CategoryGroup> children = new ArrayList<CategoryGroup>();
        CategoryGroup child1 = new CategoryGroup();
        child1.setNm("Child1");
        child1.setRlnm("chld1");
        child1.setD("chld1");
        CategoryGroup child2 = new CategoryGroup();
        child2.setNm("Child2");
        child2.setRlnm("chld2");
        child2.setD("chld2");
        children.add(child1);
        children.add(child2);
        parent.setChldrn(children);

        List<CategoryGroup> children2 = new ArrayList<CategoryGroup>();
        CategoryGroup child1a = new CategoryGroup();
        child1a.setNm("Child1a");
        child1a.setRlnm("chld1a");
        child1a.setD("chld1a");
        CategoryGroup child2a = new CategoryGroup();
        child2a.setNm("Child2a");
        child2a.setRlnm("chld2a");
        child2a.setD("chld2a");
        children2.add(child1a);
        children2.add(child2a);
        child2.setChldrn(children2);

        NavigationBar nb = new NavigationBar();
        nb.setGrps(groups);
        CategoryGroup result = nb.findCategoryGroupById(groups, "parent", "chld2a");
        System.out.println("Found: " + result.getRlnm());

        nb.addCategoryGroup(result);

        nb.removeCategoryGroup("chld2a");

        System.out.println("Test complete: " + nb.toString());
    }
}
