INSERT INTO users (id, email, password, full_name, role) VALUES
  (1, 'admin@food.com', '{noop}admin123', 'Admin', 'ROLE_ADMIN'),
  (2, 'user@food.com', '{noop}user123', 'Test User', 'ROLE_USER')
ON DUPLICATE KEY UPDATE email=VALUES(email);

INSERT INTO restaurants (id, name, address, active) VALUES
  (10, 'Pasta Palace', '123 Main St', true),
  (11, 'Sushi Stop', '9 Ocean Ave', true)
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO menu_items (id, restaurant_id, name, description, price, available) VALUES
  (100, 10, 'Spaghetti', 'Classic tomato pasta', 199.00, true),
  (101, 10, 'Alfredo', 'Creamy white sauce', 249.00, true),
  (200, 11, 'California Roll', 'Crab and avocado', 299.00, true)
ON DUPLICATE KEY UPDATE name=VALUES(name);
