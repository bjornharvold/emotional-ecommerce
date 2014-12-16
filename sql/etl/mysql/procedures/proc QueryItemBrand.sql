--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemBrand(IN p_BrandID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select b.objectid id, b.brandid tld, b.brandname nm, b.urlname rlnm, b.lelaurl srlnm, x.ky,
( case x.ky
        when 'FacebookURL' then FacebookUrl
        when 'FacebookLikes' then FacebookLikes
        when 'TwitterUrl' then TwitterUrl
        when 'TwitterName' then TwitterName
        when 'TwitterFollowers' then TwitterFollowers
        when 'PopshopsBrandID' then PopshopsBrandId end ) vl
from brand b,
(
        select 'FacebookUrl' ky
        union select 'FacebookLikes' ky
        union select 'TwitterURL' ky
        union select 'TwitterName' ky
        union select 'TwitterFollowers' ky
        union select 'PopshopsBrandID' ky
) x
where brandid = p_BrandID;

END
/
