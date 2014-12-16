Delimiter $$

drop function if exists lelaetl.substr_count$$

create function substr_count(str varchar(2048), search varchar(2048))
returns int
deterministic
begin

declare result int;

set result = 0;

if str is not null then
set result =  (LENGTH(str) - LENGTH(REPLACE(str,search, ""))) / LENGTH(search);
end if;

return result;

end;

$$

Delimiter ;