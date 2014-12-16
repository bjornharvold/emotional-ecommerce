--/
CREATE DEFINER=`lelaetl`@`%` FUNCTION getDefaultProductImage_EAV(pCategoryID int, pItemID int) RETURNS varchar(5000) CHARSET latin1
    DETERMINISTIC
BEGIN
DECLARE tempresult, result VARCHAR(5000);
SELECT 
  v.ImageURL_Large into tempresult
FROM tbl_cnet_images_resized v
where v.CategoryID = pCategoryID
AND v.ItemID = pItemID
ORDER BY Preferred DESC
LIMIT 1;
SET result = tempresult;
RETURN result;  
END
/
