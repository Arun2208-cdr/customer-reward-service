CREATE TABLE customer (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(20),
    city VARCHAR(50)
);

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    date DATE,
    amount DOUBLE,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);