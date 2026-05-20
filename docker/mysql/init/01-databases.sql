CREATE DATABASE IF NOT EXISTS libroclases_academico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS libroclases_asistencia CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS libroclases_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON libroclases_academico.* TO 'libroclases'@'%';
GRANT ALL PRIVILEGES ON libroclases_asistencia.* TO 'libroclases'@'%';
GRANT ALL PRIVILEGES ON libroclases_auth.* TO 'libroclases'@'%';
FLUSH PRIVILEGES;
