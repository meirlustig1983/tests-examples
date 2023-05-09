DROP TABLE bank_account;

CREATE TABLE IF NOT EXISTS bank_account
(
       id              SERIAL PRIMARY KEY,
       first_name      VARCHAR(255)            NOT NULL,
       last_name       VARCHAR(255)            NOT NULL,
       balance         NUMERIC(10, 2),
       minimum_balance NUMERIC(10, 2),
       active          BOOLEAN,
       created_at      TIMESTAMP DEFAULT NOW(),
       updated_at      TIMESTAMP DEFAULT NOW()
);

INSERT INTO bank_account (id, first_name, last_name, balance, minimum_balance, active, created_at, updated_at)
VALUES (1, 'John', 'Doe', 1000, 500, true, now(), now());