--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemStores(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select getStrs_EAV(p_CategoryID, p_ItemID);
END
/
