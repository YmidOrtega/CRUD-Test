ALTER TABLE users ADD COLUMN active BOOLEAN DEFAULT TRUE;
UPDATE users SET active = TRUE WHERE active IS NULL;