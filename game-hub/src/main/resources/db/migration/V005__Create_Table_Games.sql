CREATE TABLE IF NOT EXISTS tb_games (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(10000) NOT NULL,
    slug VARCHAR(50) NOT NULL UNIQUE,
    amount_downloads BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id UUID REFERENCES tb_users(id) ON DELETE CASCADE
);
