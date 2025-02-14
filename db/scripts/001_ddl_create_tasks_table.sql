CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   description TEXT,
   created TIMESTAMP NOT NULL DEFAULT NOW(),
   done BOOLEAN
);