--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemAttributes(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select
    id as tld,
    ObjectID as id,
    'en_us' as lcl,
    UrlName as rlnm,
    LelaURL as srlnm,
    ProductModelName as nm,
    'PRODUCT' as tp
from item
where CategoryID = p_CategoryID and ItemID = p_ItemID;
    
END
/
