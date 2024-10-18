CREATE TABLE IF NOT EXISTS tb_likes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    is_like BOOLEAN NOT NULL,
    user_id UUID REFERENCES tb_users(id) ON DELETE CASCADE,
    game_id UUID REFERENCES tb_games(id) ON DELETE CASCADE
);
