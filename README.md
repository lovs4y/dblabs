### DB initialization

Create table `specialties`
```SQL
CREATE TABLE IF NOT EXISTS specialties (
    id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL,
    name VARCHAR,
    code VARCHAR
);
```

Create table `skills`
```SQL
CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL,
    name VARCHAR,
    code VARCHAR,
    type VARCHAR NOT NULL
);
```

Create table `recruiters`

```SQL
create table if not exists recruiters (
    id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL,
    email VARCHAR,
    phone_number VARCHAR,
    jira_id BIGINT UNIQUE NOT NULL
);
```

Create table `cvs`
```SQL
create table if not exists cvs (
    id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL,
    first_name VARCHAR NOT NULL,
    mid_name VARCHAR,
    last_name VARCHAR NOT NULL,
    description TEXT,
    recruiter_comment TEXT,
    recruiter_id BIGINT NOT NULL REFERENCES recruiters(id),
    specialty_id BIGINT NOT NULL REFERENCES specialties(id)
);
```

Create table `cv_skills`
```SQL
CREATE TABLE IF NOT EXISTS cv_skills (
    id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL,
    skill_id BIGINT NOT NULL REFERENCES skills(id),
    cv_id BIGINT NOT NULL REFERENCES cvs(id),
    knowledge_rate INTEGER CHECK (knowledge_rate > 0 AND knowledge_rate <= 10)
);
```

---

### Insert initial data

##### Add 3 new recruiters

```SQL 
INSERT INTO recruiters (email, phone_number, jira_id) VALUES ('recruiter1_email@gmail.com', '+3805050505', 1);
INSERT INTO recruiters (email, phone_number, jira_id) VALUES ('recruiter2_email@gmail.com', '+3806060606', 2);
INSERT INTO recruiters (email, phone_number, jira_id) VALUES ('recruiter3_email@gmail.com', '+3807070707', 3);
```

##### Add 7 new skills

```SQL 
INSERT INTO skills (name, code, type) VALUES ('Java', 'JAVA', 'Development');
INSERT INTO skills (name, code, type) VALUES ('Java Script', 'JS', 'Development');
INSERT INTO skills (name, code, type) VALUES ('Agile', 'AGILE', 'Project Management');
INSERT INTO skills (name, code, type) VALUES ('Kanban', 'KANBAN', 'Project Management');
INSERT INTO skills (name, code, type) VALUES ('Selenium', 'AQA_SELENIUM', 'Testing');
INSERT INTO skills (name, code, type) VALUES ('Angular', 'ANGL', 'Development');
INSERT INTO skills (name, code, type) VALUES ('Figma', 'FIGMA', 'Design');
```

##### Add 7 new specialties

```SQL 
INSERT INTO specialties (name, code) VALUES ('Development', 'DEV');
INSERT INTO specialties (name, code) VALUES ('Project Management', 'PM');
INSERT INTO specialties (name, code) VALUES ('Design', 'DESIGN');
INSERT INTO specialties (name, code) VALUES ('Automation Quality Assurance', 'AQA');
```

##### Add 6 coworkers

```SQL
INSERT INTO cvs (first_name, mid_name, last_name, description, recruiter_comment, recruiter_id, specialty_id)
VALUES (
        'Vladyslava',
        'Viktorivna',
        'Prazhmovska',
        'I"m good java developer!',
        'Yeah, she is really cool developer :)',
        3,
        1
        );

INSERT INTO cvs (first_name, mid_name, last_name, description, recruiter_comment, recruiter_id, specialty_id)
VALUES (
           'Andrii',
           'Andriyovych',
           'Volostnykh',
           'Just hire me, and you will see the magic',
           'Dammit, he knows what CQRS is',
           3,
           1
       );

INSERT INTO cvs (first_name, mid_name, last_name, description, recruiter_comment, recruiter_id, specialty_id)
VALUES (
           'Elyar',
           null,
           'bin Muhamed',
           'Figma is my main instrument of doing things',
           'Not bad',
           2,
           3
       );

INSERT INTO cvs (first_name, mid_name, last_name, description, recruiter_comment, recruiter_id, specialty_id)
VALUES (
           'Viktor',
           'Volodymyrovych',
           'Korin',
           'A lot of years, a lot of projects',
           'Good guy, but bold',
           1,
           2
       );

INSERT INTO cvs (first_name, mid_name, last_name, description, recruiter_comment, recruiter_id, specialty_id)
VALUES (
           'Kyrylo',
           'Viktorovych',
           'Korin',
           'I"m just beginner, but love my profession',
           'Has good basic and mid knowledge, so I would recommend to hire',
           2,
           4
       );
```

##### Relate CV with skills

```SQL 
INSERT INTO cv_skills (skill_id, cv_id, knowledge_rate) VALUES (1, 1, 10);
INSERT INTO cv_skills (skill_id, cv_id, knowledge_rate) VALUES (2, 1, 10);
INSERT INTO cv_skills (skill_id, cv_id, knowledge_rate) VALUES (6, 1, 10);
INSERT INTO cv_skills (skill_id, cv_id, knowledge_rate) VALUES (1, 3, 10);
```


### Testing

Let's check recruiters
```SQL 
SELECT * FROM recruiters;
```

Let's check specialties
```SQL 
SELECT * FROM specialties;
```

Let's check applicants
```SQL 
SELECT * FROM cvs;
```

Let's check skills
```SQL 
SELECT * FROM skills;
```

Let's check skills CV skills
```SQL
SELECT * FROM cv_skills;
```

Count number of processed CV's by one recruiter
```SQL
SELECT count(*) FROM cvs WHERE recruiter_id = 3
```

Check CV and skills relation

```SQL
SELECT 
    cvs.first_name || ' ' || cvs.mid_name || ' ' || cvs.last_name AS full_name, 
    knowledge_rate, 
    skills.code
FROM cvs
INNER JOIN cv_skills ON cvs.id = cv_skills.cv_id
INNER JOIN skills ON cv_skills.skill_id = skills.id
```

Select all CV's by skill
```SQL
SELECT * FROM cvs
                  INNER JOIN cv_skills ON cvs.id = cv_skills.cv_id
                  INNER JOIN skills ON cv_skills.skill_id = skills.id
WHERE skills.id = 1
```
