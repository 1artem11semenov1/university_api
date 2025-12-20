-- корпуса
INSERT INTO units (id, unit_name, address) VALUES
(1,'pmpuB', 'Университетский проспект, д.35, лит. Б'),
(2,'pmpuD', 'Университетский проспект, д.35, лит. Д'),
(3,'pmpuA', 'Университетский проспект, д.35, лит. А'),
(4,'himfak', 'Университетский проспект, д.26, лит. А');

-- дистанции
INSERT INTO distances (unit_from, unit_to, time_minutes) VALUES
(1, 2, 2),
(2, 1, 2),
(1, 3, 2),
(3, 1, 2),
(2, 3, 2),
(3, 2, 2),
(1, 4, 15),
(4, 1, 15),
(3, 4, 15),
(4, 3, 15),
(2, 4, 15),
(4, 2, 15);

-- кабинеты
INSERT INTO classrooms (id, number, unit_id, capacity) VALUES
(1,'212Д', 2, 30),
(2,'225/1,2', 1, 25),
(3,'03', 4, 40),
(4,'2005', 3, 35),
(5,'4007', 3, 50),
(6,'215Д',2,45);

insert into positions (id, position_name) values
(1, 'admin'),
(2, 'BD teacher'),
(3, 'Algorythms teacher'),
(4, 'Kubernetes teacher'),
(5, 'BZHD teacher'),
(6, 'IT clinic teacher'),
(7, 'Probability theory teacher');

-- преподаватели (в employees)
INSERT INTO employees (id, email, first_name, last_name, date_of_birth, experience) VALUES
(1,'korovkin@spb.ru', 'Максим', 'Коровкин', '1978-01-01', 15),
(2,'nikiforov@spb.ru', 'Константин', 'Никифоров', '1978-01-01', 15),
(3,'frolov@spb.ru', 'Сергей', 'Фролов', '1978-01-01', 15),
(4,'maslikov@spb.ru', 'Александр', 'Масликов', '1978-01-01', 15),
(5,'blekanov@spb.ru', 'Иван', 'Блеканов', '1978-01-01', 15),
(6,'svirkin@spb.ru', 'Михаил', 'Свиркин', '1978-01-01', 15);


-- должности-преподаватели
insert into employees_positions (employee_id, position_id) values
(1, 2),
(2, 3),
(3, 4),
(4, 5),
(5, 6),
(6, 7);

-- преподаватели (аккаунт)
INSERT INTO users (user_id, email, password, role, username, employee_id) VALUES
(2,'korovkin@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','korovkin@spb.ru',1),
(3,'nikiforov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','nikiforov@spb.ru',2),
(4,'svirkin@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','svirkin@spb.ru',6),
(5,'frolov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','frolov@spb.ru',3),
(6,'maslikov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','maslikov@spb.ru',4),
(7,'blekanov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','blekanov@spb.ru',5);

-- Группы
INSERT INTO groups (id,group_name) VALUES
(1,'23Б11'),
(2,'23Б12');

-- студенты (в роли)
INSERT INTO students (id,email, first_name, last_name, date_of_birth, enter_year, level, group_id) VALUES
(7,'semenov@spb.ru', 'Артём', 'Семёнов', '2006-03-18', '2023', 'bac', 1),
(8,'kurmakaev@spb.ru', 'Нияз', 'Курмакаев', '2005-01-01', '2023', 'bac', 1),
(9,'testst12@spb.ru', 'Студент', 'Тестовый', '2005-02-12', '2023', 'bac', 2),
(10,'testst121@spb.ru', 'Тест', 'Студентовый', '2005-02-12', '2023', 'bac', 2);


-- студенты (аккаунт)
INSERT INTO users (user_id, email, password, role, username, student_id) VALUES
(10,'semenov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','semenov@spb.ru',7),
(11,'kurmakaev@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','kurmakaev@spb.ru',8),
(12,'testst12@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','testst12@spb.ru',9),
(13,'testst121@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','testst121@spb.ru',10);

-- дисциплины
INSERT INTO disciplines (id, discipline_name, group_id, teacher_id, count_hours) VALUES
(1,'Введение в системы баз данных, лекция', 1, 1, 72),
(2,'Введение в системы баз данных, лекция', 2, 1, 72),
(3,'Проверочное занятие', 1, 5, 25),
(4,'Алгоритмы и анализ сложности, лекция', 1, 2, 72),
(5,'Алгоритмы и анализ сложности, лекция', 2, 2, 72),
(6,'Прикладные задачи теории вероятности, лекция', 1, 6, 72),
(7,'Прикладные задачи теории вероятности, лекция', 2, 6, 72),
(8,'Алгоритм и анализ сложности, практика', 1, 2, 72),
(9,'Алгоритм и анализ сложности, практика', 2, 2, 72),
(10,'Введение в системы баз данных, практика', 1, 1, 72),
(11,'Введение в системы баз данных, практика', 2, 1, 72),
(12,'kubernetes', 1, 3, 72),
(13,'kubernetes', 2, 3, 72),
(14,'Безопасность жизнедеятельности', 1, 4, 72),
(15,'Безопасность жизнедеятельности', 2, 4, 72),
(16,'Технологии Web', 1, 5, 72),
(17,'Технологии Web', 2, 5, 72);

--занятия
DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 1 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                5,
                2,
                (d::text || ' 11:15')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 1 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                4,
                2,
                (d::text || ' 11:15')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 1 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                1,
                1,
                (d::text || ' 09:30')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;
DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 1 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                2,
				1,
                (d::text || ' 09:30')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 3 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                7,
                2,
                (d::text || ' 11:15')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 3 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                6,
				2,
                (d::text || ' 11:15')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 3 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                9,
                2,
                (d::text || ' 09:30')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 3 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                8,
                2,
                (d::text || ' 13:40')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 3 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                10,
                4,
                (d::text || ' 15:25')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 3 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                11,
                4,
                (d::text || ' 13:40')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 4 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                12,
                5,
                (d::text || ' 13:40')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 4 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                12,
                5,
                (d::text || ' 15:25')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 4 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                13,
                5,
                (d::text || ' 13:40')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 4 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                13,
                5,
                (d::text || ' 15:25')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 5 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                14,
                3,
                (d::text || ' 10:00')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 5 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                15,
                3,
                (d::text || ' 10:00')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;

DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 6 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                16,
                6,
                (d::text || ' 10:00')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;
DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 6 THEN
            INSERT INTO schedule(
                discipline_id,
                classroom_id,
                date
            ) VALUES (
                17,
                6,
                (d::text || ' 10:00')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;