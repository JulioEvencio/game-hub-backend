CREATE TABLE IF NOT EXISTS tb_screenshots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_type VARCHAR(5) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    game_id UUID REFERENCES tb_games(id) ON DELETE CASCADE
);
