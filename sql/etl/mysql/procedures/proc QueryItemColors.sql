--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemColors(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select Hex_Value hx, ImageAngle nm, if(Preferred=1, 'true', 'false') prfrrd, ImageURL_Small small, ImageURL_Medium medium, ImageURL_Large large
FROM tbl_cnet_images_resized ir
WHERE CategoryID = p_CategoryID
AND ItemID = p_ItemID
order by preferred desc;

END
/
