CREATE TABLE users (
  id         SERIAL PRIMARY KEY,
  user_id    VARCHAR(255) UNIQUE NOT NULL,
  email      VARCHAR(100) NOT NULL,
  name       VARCHAR(100) NOT NULL,
  password   VARCHAR(100) NOT NULL,
  created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE FUNCTION set_update_time() RETURNS OPAQUE AS '
  begin
    new.updated_at := ''now'';
    return new;
  end;
' LANGUAGE plpgsql;
CREATE TRIGGER update_tri BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE set_update_time();
