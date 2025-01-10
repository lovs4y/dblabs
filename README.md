### Orphans control

Create trigger to control auto-removing orphan cv_skills 
on cvs delete

```sql 
CREATE OR REPLACE FUNCTION remove_cv_skills_orphans()
    RETURNS TRIGGER AS $$begin

    DELETE FROM cv_skills WHERE cv_id = OLD.ID;

    RETURN OLD;
end;$$ LANGUAGE plpgsql;

CREATE TRIGGER remove_cv_skills_orphans
    BEFORE DELETE ON cvs FOR EACH ROW
    EXECUTE PROCEDURE remove_cv_skills_orphans();
```

### Is hired

To have better statistic about recruiter efficiency let's add 
is_hired field and salary to cvs
```sql 
alter table cvs add column is_hired boolean;
alter table cvs add column salary bigint;
```

To make queries for statistic more simple and give client 
and opportunity to easily filter needed columns
without implementing 'dynamic queries' let's
use View's

#### Skills popularity VIEW
Represents information about selected skill,
average knowledge rate and the most popular skill
```sql 
CREATE VIEW skill_popularity_view AS
SELECT
    s.id,
    s.code,
    floor(avg(knowledge_rate)) AS average_knowledge_rate,
    count(cs.*) AS popularity_rate
FROM skills AS s
LEFT JOIN cv_skills cs ON s.id = cs.skill_id
GROUP BY s.id, cs.id
ORDER BY popularity_rate DESC;
```

#### Recruiter Efficiency VIEW
Represents number of hired applicants, average knowledge rate
and metainformation

```sql
CREATE VIEW recruiter_efficiency_view AS
SELECT cvs.recruiter_id as id,
       (select email from recruiters where id = cvs.recruiter_id) as email,
       sum(CASE WHEN is_hired = true THEN 1 ELSE 0 END) as number_of_hired,
       spec.code as main_recruiter_specialty,
       floor(avg(cvsk.knowledge_rate)) as average_knowledge_rate,
       count(cvs) as total_processed_cvs
FROM recruiters AS r
         CROSS JOIN cvs
         INNER JOIN specialties spec ON cvs.specialty_id = spec.id
         INNER JOIN cv_skills cvsk ON cvs.id = cvsk.cv_id
GROUP BY cvs.recruiter_id, spec.code;
```

### Average skill salary

Let's create additional view to see average skill salary,
number of hired applicants with of skill and max salary for skill
vs specialty

```sql
create view skills_salary_view as
select skills.id as id,
       floor(avg(cvs.salary)) as avg_skill_salary,
       skills.name as name,
       max(cvs.salary) as max_skill_salary,
       count(cvs) as hired_applicants,
       spec.code as specialty_code
from skills
         inner join cv_skills on skills.id = cv_skills.skill_id
         inner join cvs on cv_skills.cv_id = cvs.id
         inner join specialties spec on cvs.specialty_id = spec.id
where cvs.is_hired = true
group by skills.id, spec.code
```

### Mock data

Now we can create cvs with random numbers to see
how our statistics and other queries works

```sql
create or replace function test_create_cv(numberOfCVs bigint)
    returns void
as $$
declare first_name_gen varchar;
        last_name_gen varchar;
        email_gen varchar;
        recruiter_id_gen bigint;
        specialty_id_gen bigint;
        created_cv_id bigint;
        skill_id_gen bigint;
        knowledge_rate_gen int;
BEGIN
    for i in 1..numberOfCVs loop

            select (array['andrew', 'john', 'stew', 'veronika', 'joshua'])[floor(random() * 5 + 1)] as first_name
            order by random()
            limit 1 into first_name_gen;

            select (array['smith', 'doan', 'singh', 'boscassi', 'gianelli'])[floor(random() * 5 + 1)] as first_name
            order by random()
            limit 1 into last_name_gen;

            email_gen := first_name_gen || '.' || last_name_gen || '@email.com';

            select id from recruiters order by random() limit 1 into recruiter_id_gen;
            select id from specialties order by random() limit 1 into specialty_id_gen;

            insert into cvs(first_name, last_name, recruiter_id, specialty_id, is_hired, salary)
            values (
                       first_name_gen,
                       last_name_gen,
                       recruiter_id_gen,
                       specialty_id_gen,
                       random() > 0.5,
                       floor(random() * (10000-500 + 1) + 500)
                   )
            returning id into created_cv_id;

            select id from skills order by random() limit 1 into skill_id_gen;
            select (random() * 9 + 1)::int into knowledge_rate_gen;

            insert into cv_skills(skill_id, cv_id, knowledge_rate)
            values (skill_id_gen, created_cv_id, knowledge_rate_gen);

        end loop;
end;

$$ language plpgsql;
```