--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemMerchants(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select 'B005OGR1R0' asin, '524968926' prdd
union
select 'B005OGR1R0' asin, '524968991' prdd;

END
/
