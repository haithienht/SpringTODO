DROP SCHEMA IF EXISTS `tododb`;

CREATE SCHEMA `tododb` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `tododb`;

CREATE TABLE `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    birthday DATE,
    email VARCHAR(200) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    image VARCHAR(200),
    is_admin TINYINT(1) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO
    `user` (name, birthday, email, `password`, is_admin)
VALUES
    (
        'Thien',
        '1993-09-18',
        'thien@mail.com',
        'thien',
        1
    ),
    (
        'User 1',
        '1990-12-01',
        'user1@mail.com',
        'user1',
        0
    ),
    (
        'User 2',
        '1991-08-22',
        'user2@mail.com',
        'user2',
        0
    );

CREATE TABLE `project` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    detail TEXT,
    deadline TIMESTAMP,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_userid_user_id FOREIGN KEY (user_id) REFERENCES `user`(id)
);

INSERT INTO
    `project` (user_id, title, detail, deadline)
VALUES
    -- 1
    (2, 'User 1''s Project 1', '', NULL),
    -- 2
    (2, 'User 1''s Project 2', '', NULL),
    -- 3
    (3, 'User 2''s Project 1', '', NULL),
    -- 4
    (3, 'User 2''s Project 2', '', NULL),
    -- 5
    (3, 'User 2''s Project 3', '', NULL);

CREATE TABLE `user_project` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_userproject_userid_user_id FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_userproject_projectid_project_id FOREIGN KEY (project_id) REFERENCES `project`(id)
);

CREATE TABLE list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    detail TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_list_projectid_project_id FOREIGN KEY (project_id) REFERENCES project(id)
);

INSERT INTO
    list (project_id, title, detail)
VALUES
    -- 1
    (1, 'Project 1''s List 1', ''),
    -- 2
    (1, 'Project 1''s List 2', ''),
    -- 3
    (1, 'Project 1''s List 3', ''),
    -- 4
    (2, 'Project 2''s List 1', ''),
    -- 5
    (2, 'Project 2''s List 2', ''),
    -- 6
    (2, 'Project 2''s List 3', ''),
    -- 7
    (3, 'Project 1''s List 1', ''),
    -- 8
    (3, 'Project 1''s List 2', ''),
    -- 9
    (3, 'Project 1''s List 3', ''),
    -- 10
    (3, 'Project 1''s List 4', ''),
    -- 11
    (4, 'Project 2''s List 1', ''),
    -- 12
    (4, 'Project 2''s List 2', ''),
    -- 13
    (4, 'Project 2''s List 3', ''),
    -- 14
    (5, 'Project 3''s List 1', ''),
    -- 15
    (5, 'Project 3''s List 2', ''),
    -- 16
    (5, 'Project 3''s List 3', '');

CREATE TABLE task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    list_id INT,
    task_list_id INT,
    title VARCHAR(200) NOT NULL,
    detail TEXT,
    type varchar(20),
    is_done TINYINT(1) NOT NULL DEFAULT 0,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_listid_list_id FOREIGN KEY (list_id) REFERENCES list(id),
    CONSTRAINT fk_task_tasklistid_task_id FOREIGN KEY (task_list_id) REFERENCES task(id)
);

INSERT INTO
    task (list_id, task_list_id, title, type, is_done)
VALUES
    -- 1
    (1, NULL, 'L1 List Task 1', 'list', 0),
    -- 2
    (NULL, 1, 'L1 List Task 1''s Item 1', 'item', 1),
    -- 3
    (NULL, 1, 'L1 List Task 1''s Item 2', 'item', 0),
    -- 4
    (1, NULL, 'L1 List Task 2', 'list', 0),
    -- 5
    (NULL, 4, 'L1 List Task 2''s Item 1', 'item', 0),
    -- 6
    (NULL, 4, 'L1 List Task 2''s Item 2', 'item', 0),
    -- 7
    (2, NULL, 'L2 Item Task 1', 'item', 1),
    -- 8
    (2, NULL, 'L2 Item Task 2', 'item', 0),
    -- 9
    (2, NULL, 'L2 Item Task 3', 'item', 0),
    -- 10
    (3, NULL, 'L3 Item Task 1', 'item',, 0),
    -- 11
    (3, NULL, 'L3 List Task 1', 'list', NULL, 0),
    -- 12
    (3, NULL, 'L3 List Task 2', 'list', NULL, 0);

CREATE TABLE list_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    list_order TEXT,
    CONSTRAINT fk_listorder_projectid_project_id FOREIGN KEY (project_id) REFERENCES project(id)
);

INSERT INTO
    list_order (project_id, list_order)
VALUES
    (1, '1 3 2'),
    (2, '5 6 4');

CREATE TABLE task_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    list_id INT,
    task_id INT,
    task_order TEXT,
    CONSTRAINT fk_taskorder_listtid_list_id FOREIGN KEY (list_id) REFERENCES list(id),
    CONSTRAINT fk_taskorder_tasktid_task_id FOREIGN KEY (task_id) REFERENCES task(id)
);

INSERT INTO
    task_order (list_id, task_id, task_order)
VALUES
    (1, NULL, '1 4'),
    (2, NULL, '9 8 7'),
    (NULL, 4, '6 5');