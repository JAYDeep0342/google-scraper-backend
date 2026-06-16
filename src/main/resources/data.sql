INSERT INTO users (id, username, password, name)
VALUES (
  10,
  'jaydeep',
  '$2a$10$ZddyIANKErLnVjancODR2OHVBtHHJprZGAe296HbxUP4qKwLyWrki',
  'Jaydeep'
);

INSERT INTO users_roles (user_id, role)
VALUES (10, 'ADMIN');