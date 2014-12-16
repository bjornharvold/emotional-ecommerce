DELIMITER $$

DROP FUNCTION IF EXISTS `lelaetl`.`getBlogAssociationJson`$$
CREATE DEFINER=`root`@`localhost` FUNCTION  `lelaetl`.`getBlogAssociationJson`(blogs varchar(1024)) RETURNS varchar(1024) CHARSET latin1
    DETERMINISTIC
begin

declare pos int;
declare result varchar(1024);

set pos = 1;
set result = '';

if blogs is not null and length(trim(blogs)) > 0 then

  /* REMOVE ANY LINE FEEDS */
  set blogs = replace(blogs, '\n', ' ');

  set result = '"ssctns": [';

  while length(trim(split_str(trim(leading 'http://' from blogs), 'http://', pos))) > 0 do

    set result = concat(result, '{ "tp": "BLOG", "rl": "http://');
    set result = concat(result, trim(split_str(trim(leading 'http://' from blogs), 'http://', pos)));
    set result = concat(result, '" }, ');

    set pos = pos + 1;

  end while;

  set result = trim(trailing ', ' from result);

  set result = concat(result, '], ');

end if;

return result;

end;

 $$

DELIMITER ;