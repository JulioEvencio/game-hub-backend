CREATE TABLE IF NOT EXISTS tb_users_roles (
    user_id UUID REFERENCES tb_users(id) ON DELETE CASCADE,
    role_id UUID REFERENCES tb_roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
