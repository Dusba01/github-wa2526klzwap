DROP TABLE IF EXISTS favorite CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS note CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password TEXT NOT NULL,
  username VARCHAR(50) UNIQUE NOT NULL,
  name VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE note (
  id SERIAL PRIMARY KEY,
  author_id INTEGER NOT NULL,
  course_id INTEGER NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  file_path TEXT NOT NULL,

  CONSTRAINT fk_note_users
      FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

  CONSTRAINT fk_note_course
      FOREIGN KEY (course_id)
        REFERENCES course(id)
        ON DELETE CASCADE
);

CREATE TABLE rating (
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  note_id INTEGER NOT NULL,
  value INTEGER NOT NULL CHECK (value >= 1 AND value <= 5),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_rating_users
      FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

  CONSTRAINT fk_rating_note
      FOREIGN KEY (note_id)
        REFERENCES note(id)
        ON DELETE CASCADE,

  CONSTRAINT unique_user_note_rating
      UNIQUE (user_id, note_id)
);

CREATE TABLE favorite (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  note_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_favorite_user
      FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

  CONSTRAINT fk_favorite_note
      FOREIGN KEY (note_id)
        REFERENCES note(id)
        ON DELETE CASCADE,

  CONSTRAINT unique_user_note
      UNIQUE (user_id, note_id)
);