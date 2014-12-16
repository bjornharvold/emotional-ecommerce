--/
CREATE FUNCTION medianForBrand(p_motivator int, p_brandId int) RETURNS decimal(12,3)
    READS SQL DATA
    DETERMINISTIC
BEGIN

declare median int;

select avg(a.motivatorscore) into median
FROM (
  SELECT @rownum:=@rownum+1 AS rownum, motivatorscore 
  FROM (SELECT @rownum:=0) r, productmotivator p join item i using (itemid, brandid, categoryid)
  where i.UseThisItem = 1 and i.ResearchComplete = 1 and motivator = p_motivator and p.brandid = p_brandId and motivatorscore is not null
  order by motivatorscore 
) a,
(
  select 0.5+count(*)/2 median
  FROM productmotivator p2 join item i2 using (itemid, brandid, categoryid)
  where i2.UseThisItem = 1 and i2.ResearchComplete = 1 and motivator = p_motivator and p2.brandid = p_brandId and motivatorscore is not null
) b
WHERE a.rownum between (b.median - 0.5) and (b.median +0.5);

return median;

END
/
