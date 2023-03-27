CREATE TABLE IF NOT EXISTS CURRENCIES_TBL (
    id SERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(255) NOT NULL
);

insert into CURRENCIES_TBL (id, name, code, symbol) VALUES (default, 'Indian Rupee', 'INR', '₹');
insert into CURRENCIES_TBL (id, name, code, symbol)  VALUES (default, 'British Pound', 'GBP', '£');
insert into CURRENCIES_TBL (id, name, code, symbol)  VALUES (default, 'Euro', 'EURO', '€');
insert into CURRENCIES_TBL (id, name, code, symbol)  VALUES (default, 'American Dollar', 'USD', '$');