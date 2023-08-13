CREATE TABLE plans (
    slug VARCHAR(30) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    target_limit INT NOT NULL,
    api_access_limit INT NOT NULL,
    status_page_type VARCHAR(50) NOT NULL,
    data_retention VARCHAR(50) NOT NULL,
    description TEXT
);

INSERT INTO plans (slug, name, price, target_limit, api_access_limit, status_page_type, data_retention, description) VALUES
('BASIC', 'Basic', 0.0, 10, 1000, 'BASIC', '30_DAYS', 'Basic subscription plan with limited features');
