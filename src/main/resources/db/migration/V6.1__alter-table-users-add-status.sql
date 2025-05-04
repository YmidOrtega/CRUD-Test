ALTER TABLE users ADD status VARCHAR(15)
    CHECK (status IN ('pending', 'active', 'inactive', 'suspended', 'deleted'))
    DEFAULT 'pending' NOT NULL;