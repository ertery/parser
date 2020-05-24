CREATE SCHEMA IF NOT EXISTS parser;

GRANT ALL ON SCHEMA parser TO parser_app;
GRANT ALL ON ALL TABLES IN SCHEMA parser TO parser_app;
GRANT ALL ON ALL SEQUENCES IN SCHEMA parser TO parser_app;

CREATE TABLE IF NOT EXISTS parser.department
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    dep_code    VARCHAR(20)  NOT NULL,
    dep_job     VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    UNIQUE (dep_code, dep_job)
);

INSERT INTO parser.department (dep_code, dep_job, description)
VALUES ('IT001', 'Programmer', 'Programmer Java'),
       ('IT001', 'Senior Programmer', 'Senior programmer Java'),
       ('IT001', 'Team Lead', 'Team Lead Java'),
       ('QA001', 'QA engineer', ' Best QA engineer ever'),
       ('QA001', 'Senior QA engineer', 'Best of the best QA engineer ever'),
       ('QA002', 'Autotests QA engineer', 'QA that write code, weird...'),
       ('IT002', 'Programmer', 'Programmer C#'),
       ('IT002', 'Senior Programmer', 'Senior programmer C#'),
       ('A001', 'Accountant', 'Yours closest fellow');
