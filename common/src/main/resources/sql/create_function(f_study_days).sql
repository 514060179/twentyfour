DELIMITER $$

CREATE
    FUNCTION f_study_days(id INT, start_time DATE, end_time DATE)
    RETURNS INT
    BEGIN
    DECLARE days INT;
    DECLARE flag INT;
    DECLARE previous_day DATE;
    SET days := 0;
    SET flag := 1;
    SET previous_day := end_time;

    WHILE flag>0 DO
        SELECT COUNT(DISTINCT(DATE(record_time))) INTO flag  FROM  study_record
        WHERE record_user_id = id
        AND DATE(record_time) = previous_day ;
        IF flag > 0 THEN
            SET days := days + 1;
            SET previous_day := DATE_SUB(previous_day,INTERVAL 1 DAY);
        END IF;
    END WHILE;
    RETURN days;
    END$$

DELIMITER ;