CREATE TABLE IF NOT EXISTS tb_files (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_type VARCHAR(4) NOT NULL,
    file_url VARCHAR(255) NOT NULL
);
