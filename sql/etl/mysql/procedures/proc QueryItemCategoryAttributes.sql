--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemCategoryAttributes(IN p_CategoryID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select catalogkey as ctlgky, objectid as id, categoryname as nm, categoryorder as rdr, urlname as rlnm, lelaurl as srlnm, categoryid as tld
from category
where CategoryID = p_CategoryID;
    
END
/
