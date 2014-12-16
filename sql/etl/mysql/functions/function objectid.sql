Delimiter $$

drop function if exists objectid$$

create function objectid() returns char(24)
deterministic
begin

  declare result char(24);

  select concat(
    hex(unix_timestamp(utc_timestamp())),
    right(lpad(substr(convert(md5(@@hostname) using 'utf8'), 1, 6), 6, '0'), 6),
    right(lpad(hex(connection_id()), 4, '0'), 4),
    right(lpad(hex(objectid_seq()), 6, '0'), 6))
  into result;

  return result;

end;

$$

Delimiter ;