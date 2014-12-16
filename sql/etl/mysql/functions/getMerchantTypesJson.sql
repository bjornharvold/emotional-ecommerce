DELIMITER $$

DROP FUNCTION IF EXISTS `lelaetl`.`getMerchantTypesJson`$$
CREATE DEFINER=`root`@`localhost` FUNCTION  `lelaetl`.`getMerchantTypesJson`(in_merchant_id int) RETURNS longtext CHARSET latin1
    DETERMINISTIC
begin

declare result longtext;
declare branch_count int;
declare has_url int;

set branch_count = 0;
set has_url = 0;

select count(*)
into branch_count
from local_stores
where merchant_id = in_merchant_id;

select case when url is not null then 1 else 0 end
into has_url
from merchant
where merchant_id = in_merchant_id;

set result = '"tp": [';

if has_url = 1 then
  set result = concat(result, '"ONLINE"');
end if;

if branch_count > 0 then

  if has_url = 1 then
    set result = concat(result, ', ');
  end if;

  set result = concat(result, '"LOCAL"');

end if;

set result = concat(result, '],');

return result;

end;

 $$

DELIMITER ;