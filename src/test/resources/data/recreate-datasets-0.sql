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