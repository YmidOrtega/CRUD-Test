ALTER TABLE partial_tokens MODIFY token VARCHAR(1024);
CREATE UNIQUE INDEX token ON partial_tokens(token(255));
