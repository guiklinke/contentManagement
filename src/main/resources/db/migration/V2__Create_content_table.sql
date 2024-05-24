CREATE TABLE content (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR NOT NULL,
    category VARCHAR(7) NOT NULL,
    thumbnail_url VARCHAR NOT NULL,
    content_url VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);