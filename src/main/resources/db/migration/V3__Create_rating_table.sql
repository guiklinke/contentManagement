CREATE TABLE rating (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content_id BIGINT NOT NULL,
    comment VARCHAR(200),
    score INT NOT NULL CHECK (score >= 1 AND score <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (content_id) REFERENCES content(id)
);