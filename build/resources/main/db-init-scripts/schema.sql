create table AUTHORS
(
    id      INT             PRIMARY KEY,
    name    VARCHAR(255)    NOT NULL
);

create table COURSES
(
    id          INT             PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    category    VARCHAR(20)     NOT NULL,
    rating      INT             NOT NULL,
    description VARCHAR(1000)   NOT NULL
);
