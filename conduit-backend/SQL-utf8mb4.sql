-- Cambia la base de datos
ALTER DATABASE conduit CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Cambia la tabla
ALTER TABLE article CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE comment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;




ALTER TABLE article MODIFY description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE article MODIFY body TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

ALTER TABLE comment MODIFY body TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

ALTER TABLE user MODIFY bio TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
