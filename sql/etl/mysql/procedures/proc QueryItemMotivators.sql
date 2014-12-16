--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemMotivators(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
SELECT A, B, C, D, E, F, G, H, I, J, K, L
FROM vw_basemotivators m
where categoryid = p_CategoryID and itemid = p_ItemID;

END
/
