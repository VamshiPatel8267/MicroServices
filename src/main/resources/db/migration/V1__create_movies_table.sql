CREATE TABLE movies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT NULL,
    duration_minutes INT NOT NULL,
    release_date DATE NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT chk_movie_duration
        CHECK (duration_minutes > 0)
);

CREATE INDEX idx_movies_status
    ON movies(status);