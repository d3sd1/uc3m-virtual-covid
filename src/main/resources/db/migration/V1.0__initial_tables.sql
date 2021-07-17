CREATE TABLE users (
id bigint PRIMARY KEY,
id_uc3m bigint unique NOT NULL,
full_name varchar NOT NULL,
email varchar NOT NULL UNIQUE,
uc3m_password varchar NOT NULL,
user_moodle_id bigint NOT NULL UNIQUE,
jwt varchar NOT NULL
);
CREATE TABLE subjects (
id bigint PRIMARY KEY,
subject_name varchar NOT NULL,
course_uc3m_id int NOT NULL UNIQUE,
course_mag_id int UNIQUE,
course_pra_id int UNIQUE,
from_year smallint NOT NULL,
to_year smallint NOT NULL,
semester smallint NOT NULL,
course_group_id int NOT NULL
);
CREATE TABLE IF NOT EXISTS user_subjects (
user_id bigint,
subject_id bigint,
PRIMARY KEY(user_id, subject_id)
);