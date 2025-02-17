CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   title VARCHAR(50),
   description TEXT,
   created TIMESTAMP,
   done BOOLEAN
);