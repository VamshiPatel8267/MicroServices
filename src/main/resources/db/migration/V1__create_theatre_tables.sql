CREATE TABLE cities (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT pk_cities
        PRIMARY KEY (id),

    CONSTRAINT uq_cities_location
        UNIQUE (name, state, country)
);

CREATE TABLE theatres (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT pk_theatres
        PRIMARY KEY (id),

    CONSTRAINT fk_theatres_city
        FOREIGN KEY (city_id)
        REFERENCES cities(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_theatres_city_id
    ON theatres(city_id);

CREATE INDEX idx_theatres_status
    ON theatres(status);

CREATE TABLE screens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    theatre_id BIGINT NOT NULL,
    screen_number INT NOT NULL,
    name VARCHAR(100),
    screen_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT pk_screens
        PRIMARY KEY (id),

    CONSTRAINT uq_screens_theatre_number
        UNIQUE (theatre_id, screen_number),

    CONSTRAINT chk_screens_number
        CHECK (screen_number > 0),

    CONSTRAINT fk_screens_theatre
        FOREIGN KEY (theatre_id)
        REFERENCES theatres(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_screens_theatre_id
    ON screens(theatre_id);

CREATE TABLE seats (
    id BIGINT NOT NULL AUTO_INCREMENT,
    screen_id BIGINT NOT NULL,
    row_label VARCHAR(10) NOT NULL,
    seat_number INT NOT NULL,
    seat_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT pk_seats
        PRIMARY KEY (id),

    CONSTRAINT uq_seats_screen_row_number
        UNIQUE (screen_id, row_label, seat_number),

    CONSTRAINT chk_seats_number
        CHECK (seat_number > 0),

    CONSTRAINT fk_seats_screen
        FOREIGN KEY (screen_id)
        REFERENCES screens(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_seats_screen_id
    ON seats(screen_id);