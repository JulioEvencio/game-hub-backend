CREATE TABLE IF NOT EXISTS tb_comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id UUID REFERENCES tb_users(id) ON DELETE CASCADE,
    game_id UUID REFERENCES tb_games(id) ON DELETE CASCADE
);
