Delimiter $$

drop function if exists objectid_seq$$

create function objectid_seq() returns int
deterministic
begin

  update objectid_sequence set val=last_insert_id(val+1);
  return last_insert_id();

end;

$$

Delimiter ;