CREATE TABLE IF NOT EXISTS tb_games (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    slug VARCHAR(50) NOT NULL UNIQUE,
    amount_downloads BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    picture_id UUID REFERENCES tb_files(id) ON DELETE CASCADE,
    file_id UUID REFERENCES tb_files(id) ON DELETE CASCADE,
    user_id UUID REFERENCES tb_users(id) ON DELETE CASCADE
);
