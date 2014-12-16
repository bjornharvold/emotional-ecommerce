--/
CREATE DEFINER=`lelaetl`@`%` PROCEDURE QueryItemFilters(IN p_CategoryID int, IN p_ItemID int)
    READS SQL DATA
    DETERMINISTIC
BEGIN
    SELECT x.AnswerKey ky, x.vl
    FROM (SELECT d.ItemID, t.TunerID, ta.AnswerKey, 
      IF( (SUM(IF(
                  EXISTS(
                    
                    SELECT * FROM item_attribute atr 
                      WHERE atr.CategoryID = t.CategoryID 
                      AND atr.ItemID = d.ItemID 
                      AND atr.AttributeTypeID = tav.AttributeTypeID 
                      AND atr.AttributeValue = tav.AttributeValue
                      AND tav.AnswerValue = 'true'), 1, 0 )
                )>0
            )
            OR
            
            EXISTS(
              SELECT * FROM item_attribute atr 
              WHERE atr.CategoryID = t.CategoryID 
              AND atr.ItemID = d.ItemID 
              AND atr.AttributeTypeID = tav.AttributeTypeID 
              AND LOWER(atr.AttributeValue) LIKE concat('%',tav.AttributeValue,'%')
              AND tav.AnswerValue = 'contains'
            )
            OR
            
            EXISTS(
              SELECT * FROM item_attribute atr 
              WHERE atr.CategoryID = t.CategoryID 
              AND atr.ItemID = d.ItemID 
              AND atr.AttributeTypeID = tav.AttributeTypeID 
              AND atr.AttributeValue != ''
              AND tav.AnswerValue = 'nonblank'
            ),
            'true','false') AS vl
      FROM  category_tuners t
      JOIN  category_tuners_answers ta ON ta.TunerID = t.TunerID
      JOIN  category_tuners_answers_value tav ON tav.TunerID = ta.TunerID AND tav.AnswerID = ta.AnswerID AND tav.AnswerValue = ta.AnswerValue
      JOIN item d ON d.CategoryID = t.CategoryID
      WHERE d.UseThisItem = 1 AND t.CategoryID = p_CategoryID and d.ItemID = p_ItemID
      GROUP BY 1,2,3) x
  GROUP BY x.ItemID, x.TunerID;
END
/
