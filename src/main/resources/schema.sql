CREATE TABLE board
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    title      VARCHAR(255),
    content    VARCHAR(255),
    created_at TIMESTAMP,
    CONSTRAINT pk_board PRIMARY KEY (id)
);