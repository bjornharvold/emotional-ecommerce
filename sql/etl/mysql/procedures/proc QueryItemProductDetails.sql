--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemProductDetails(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
SELECT
    s.sectionname   AS sctn,
    s.sectionorder  AS sctnrdr,
    g.groupname     AS grp,
    g.GroupOrder    AS grprdr,
    ga.orderingroup AS rdr,
    ga.attrname     AS ky,
    pdp.partname    AS vl
FROM
    productdetailgroups g
JOIN
    productdetailgroupattrs ga
ON
    g.GroupID = ga.GroupID
AND g.SectionID = ga.SectionID
JOIN
    productdetailsections s
ON
    g.SectionID = s.SectionID
AND g.CategoryID = s.CategoryID
JOIN
    product_detail_part pdp
ON
    pdp.SectionID = ga.SectionID
AND pdp.GroupID = ga.GroupID
AND pdp.OrderInGroup = ga.OrderInGroup
JOIN
    product_detail_part_value pdv
ON
    pdv.SectionID = pdp.SectionID
AND pdv.GroupID = pdp.GroupID
AND pdv.OrderInGroup = pdp.OrderInGroup
AND pdv.PartSeq = pdp.PartSeq
JOIN
    item d
ON
    d.CategoryID = s.CategoryID
WHERE
    d.UseThisItem = 1
AND ga.ProductDetailAttributeValueTypeID = 1
AND s.CategoryID = p_CategoryID
AND ItemID = p_ItemID
GROUP BY
    d.ItemID,
    s.SectionID,
    g.GroupID,
    ga.OrderInGroup,
    1,2
HAVING
    SUM(IF(EXISTS
    (
        SELECT
            *
        FROM
            item_attribute atr
        WHERE
            atr.CategoryID = s.CategoryID
        AND atr.ItemID = d.ItemID
        AND atr.AttributeTypeID = pdv.AttributeTypeID
        AND atr.AttributeValue = pdv.AttributeValue),1,0)) > 0
UNION
SELECT
    s.SectionName                                         sctn,
    s.SectionOrder                                        sctnrdr,
    g.GroupName                                           grp,
    g.GroupOrder                                          grprdr,
    ga.OrderInGroup                                       rdr,
    ga.AttrName                                           ky,
    COALESCE(atr.AttributeValue, atc.DefaultValue,'null') vl
FROM
    productdetailgroups g
JOIN
    productdetailgroupattrs ga
ON
    g.GroupID = ga.GroupID
AND g.SectionID = ga.SectionID
JOIN
    productdetailsections s
ON
    g.SectionID = s.SectionID
AND g.CategoryID = s.CategoryID
JOIN
    product_detail_attribute_type pdt
ON
    pdt.SectionID = ga.SectionID
AND pdt.GroupID = ga.GroupID
AND pdt.OrderInGroup = ga.OrderInGroup
JOIN
    item d
ON
    d.CategoryID = s.CategoryID
JOIN
    attribute_type_category atc
ON
    atc.CategoryID = d.CategoryID
AND atc.AttributeTypeID = pdt.AttributeTypeID
LEFT JOIN
    item_attribute atr
ON
    atr.CategoryID = d.CategoryID
AND atr.ItemID = d.ItemID
AND atr.AttributeTypeID = pdt.AttributeTypeID
AND atr.AttributeSequence = 1
WHERE
    d.UseThisItem = 1
AND ga.ProductDetailAttributeValueTypeID IN (2,3)
AND s.CategoryID = p_CategoryID
AND d.itemid = p_ItemID
ORDER BY
    sctnrdr,
    grprdr,
    rdr;
    
END
/
