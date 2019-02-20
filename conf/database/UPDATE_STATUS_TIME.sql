CREATE TRIGGER IF NOT EXISTS UPDATE_STATUS_TIME
  AFTER UPDATE OF STATUS
  ON TASK
  FOR EACH ROW
  BEGIN
    UPDATE TASK
    SET SUBMITTED = strftime('%s', 'now')
    WHERE lower(status) = 'submitted'
      AND SUBMITTED IS NULL
      AND ID = NEW.ID;


    UPDATE TASK
    SET STARTED = strftime('%s', 'now')
    WHERE lower(status) = 'started'
      AND STARTED IS NULL
      AND ID = NEW.ID;

    UPDATE TASK
    SET COMPLETED = strftime('%s', 'now')
    WHERE lower(status) = 'completed'
      AND COMPLETED IS NULL
      AND ID = NEW.ID;

  END;