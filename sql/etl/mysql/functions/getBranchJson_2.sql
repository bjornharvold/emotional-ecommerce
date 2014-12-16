DELIMITER $$

DROP FUNCTION IF EXISTS `lelaetl`.`getBranchJson`$$
CREATE DEFINER=`root`@`localhost` FUNCTION  `lelaetl`.`getBranchJson`(in_merchant_id int, in_store_number int) RETURNS varchar(65000) CHARSET latin1
    DETERMINISTIC
begin

declare result varchar(65000);
declare done int default 0;
declare name varchar(50);
declare number int;
declare address varchar(100);
declare city varchar(50);
declare state varchar(15);
declare zip varchar(10);
declare phone varchar(25);
declare lat varchar(100);
declare lon varchar(100);

set result = '{ ';

select store_name, store_number, address, city, state, zip_code, phone_number, latitude, longitude
into name, number, address, city, state, zip, phone, lat, lon
from local_stores
where merchant_id = in_merchant_id
and store_number = in_store_number;

  set result = concat(result, '"strnmbr": "', number, '", ');

if name is not null then
  set result = concat(result, '"nm": "', ifnull(name, ''), '", ');
end if;

if lat is not null then
  set result = concat(result, '"lt": "', ifnull(lat, ''), '", ');
end if;

if lon is not null then
  set result = concat(result, '"lng": "', ifnull(lon, ''), '", ');
end if;

if city is not null then
  set result = concat(result, '"cty": "', ifnull(city, ''), '", ');
end if;

if address is not null then
  set result = concat(result, '"ddrss": "', ifnull(address, ''), '", ');
end if;

if state is not null then
  set result = concat(result, '"st": "', ifnull(state, ''), '", ');
end if;

if zip is not null then
  set result = concat(result, '"zp": "', ifnull(zip, ''), '", ');
end if;


set result = concat(result, '}\n');

return result;

end;

 $$

DELIMITER ;