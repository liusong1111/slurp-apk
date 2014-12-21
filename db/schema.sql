CREATE TABLE apks (
  id           INTEGER PRIMARY KEY,
  apk_url      TEXT,
  file_path    TEXT,
  file_name    TEXT,
  package_name char(255),
  digest       char(255),
  created_at   timestamp DEFAULT CURRENT_TIMESTAMP
);