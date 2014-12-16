/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.dto.Group;
import com.lela.domain.dto.Section;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/7/11
 * Time: 2:38 PM
 * Responsibility:
 */
public final class ItemDetails {

    /**
     * Field description
     */
    private List<Section> sctns = new ArrayList<Section>();

    /**
     * Field description
     */
    private RelevantItem rlvnttm;

    /**
     * Field description
     */
    private Item tm;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param tm tm
     */
    public ItemDetails(AbstractItem tm) {
        if (tm instanceof Item) {
            this.tm = (Item) tm;
        } else if (tm instanceof RelevantItem) {
            this.rlvnttm = (RelevantItem) tm;
        }

        processDetails(tm);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public RelevantItem getRlvnttm() {
        return rlvnttm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Section> getSctns() {
        return sctns;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Item getTm() {
        return tm;
    }

    /**
     * Method description
     *
     * @param sectionName  sectionName
     * @param sectionOrder sectionOrder
     * @param groupName    groupName
     * @param groupOrder   groupOrder
     * @return Return value
     */
    private Group getGroup(String sectionName, Integer sectionOrder, String groupName, Integer groupOrder) {
        Group   result  = null;
        Section section = getSection(sectionName, sectionOrder);

        if (section != null) {
            if ((section.getGrps() != null) && !section.getGrps().isEmpty()) {
                for (Group group : section.getGrps()) {
                    if (StringUtils.equals(group.getNm(), groupName) && group.getRdr().equals(groupOrder)) {
                        result = group;

                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param sectionName sectionName
     * @param order       order
     * @return Return value
     */
    private Section getSection(String sectionName, Integer order) {
        Section result = null;

        if ((sctns != null) && !sctns.isEmpty()) {
            for (Section sctn : sctns) {
                if (StringUtils.equals(sctn.getNm(), sectionName) && sctn.getRdr().equals(order)) {
                    result = sctn;

                    break;
                }
            }
        }

        return result;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @param tm tm
     */
    private void processDetails(AbstractItem tm) {
        if (tm != null) {

            // now we loop through and create sections and groups
            for (Attribute attribute : tm.getAttrs()) {
                String  sectionName  = attribute.getSctn();
                Integer sectionOrder = attribute.getSctnrdr();
                String  groupName    = attribute.getGrp();
                Integer groupOrder   = attribute.getGrprdr();

                if (StringUtils.isNotBlank(sectionName) && (sectionOrder != null)) {
                    Section section = getSection(sectionName, sectionOrder);

                    if (section == null) {
                        section = new Section();
                        section.setNm(attribute.getSctn());
                        section.setRdr(attribute.getSctnrdr());
                        sctns.add(section);
                    }

                    if (StringUtils.isNotBlank(groupName) && (groupOrder != null)) {
                        Group group = getGroup(sectionName, sectionOrder, groupName, groupOrder);

                        if (group == null) {
                            group = new Group();
                            group.setNm(groupName);
                            group.setRdr(groupOrder);
                            section.getGrps().add(group);
                        }

                        group.getAttrs().add(attribute);
                    }
                }

                // finally, we need to sort all collections by order
                if ((sctns != null) && !sctns.isEmpty()) {
                    Collections.sort(sctns, new SectionsComparator());

                    for (Section section : sctns) {
                        if ((section.getGrps() != null) && !section.getGrps().isEmpty()) {
                            Collections.sort(section.getGrps(), new GroupComparator());

                            for (Group group : section.getGrps()) {
                                if ((group.getAttrs() != null) && !group.getAttrs().isEmpty()) {
                                    Collections.sort(group.getAttrs(), new AttributeComparator());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class SectionsComparator implements Comparator<Section> {
        @Override
        public int compare(Section s1, Section s2) {
            int result = 0;

            if (s1.getRdr() > s2.getRdr()) {
                result = 1;
            } else if (s1.getRdr() < s2.getRdr()) {
                result = -1;
            }

            return result;
        }
    }

    private class GroupComparator implements Comparator<Group> {
        @Override
        public int compare(Group g1, Group g2) {
            int result = 0;

            if (g1.getRdr() > g2.getRdr()) {
                result = 1;
            } else if (g1.getRdr() < g2.getRdr()) {
                result = -1;
            }

            return result;
        }
    }

    private class AttributeComparator implements Comparator<Attribute> {
        @Override
        public int compare(Attribute a1, Attribute a2) {
            int result = 0;

            if (a1.getRdr() > a2.getRdr()) {
                result = 1;
            } else if (a1.getRdr() < a2.getRdr()) {
                result = -1;
            }

            return result;
        }
    }
}
