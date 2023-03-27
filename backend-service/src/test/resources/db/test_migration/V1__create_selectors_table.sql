CREATE TABLE IF NOT EXISTS CURRENCIES_TBL (
    id SERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(255) NOT NULL
);

insert into CURRENCIES_TBL (id, name, code, symbol) VALUES (default, 'Indian Rupee', 'INR', '₹');

CREATE TABLE IF NOT EXISTS USERS_TBL (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    passwrd VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

insert into USERS_TBL (id, firstname,lastname, username,email, passwrd, role) values (1, 'Mehul', 'Soni',  'msoni0','mehul.soni89@gmail.com', 'string','USER')