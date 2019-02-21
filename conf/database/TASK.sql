CREATE TABLE IF NOT EXISTS TASK
(
  ID        INTEGER PRIMARY KEY AUTOINCREMENT,
  STATUS    TEXT CHECK (lower(STATUS) IN ('submitted', 'started', 'completed')),
  SUBMITTED BIGINT DEFAULT (strftime('%s', 'now')),
  STARTED   BIGINT,
  COMPLETED BIGINT
);

