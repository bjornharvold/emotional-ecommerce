--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemSubAttributes(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
select 'ProductModelName' ky, ProductModelName vl
from item
where categoryid = p_CategoryID and itemid = p_ItemID

union

select 'Brand' ky, b.brandid vl
from item i join brand b using (BrandID)
where categoryid = p_CategoryID and itemid = p_ItemID

union

select x.ky,
       case x.ky when 'MerchantId' then MerchantID
                 when 'MerchantName' then MerchantName
                 when 'LowestPrice' then LowestPrice
                 when 'StoreCount' then StoreCount
                 end vl
from (
        select MerchantId, MerchantName, min(LowestPrice) LowestPrice, count(*) StoreCount
        from (
                select merchantid MerchantId, MerchantName, if(OfferSalePrice > 0, OfferSalePrice, OfferPrice) LowestPrice
                from merchant_offer
                where categoryid = p_CategoryID
                and itemid = p_ItemID
                and merchantid > 0
        ) mo
        group by MerchantId, MerchantName
        order by 3
        limit 1
) lowest,
(
        select 'MerchantId' ky       
        union select 'MerchantName' ky
        union select 'LowestPrice' ky
        union select 'StoreCount' ky
) x

union

select 'ProductImageUrl' ky, getDefaultProductImage_EAV(p_CategoryID, p_ItemID) vl;   

END
/
