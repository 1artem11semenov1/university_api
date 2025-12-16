-- корпуса
INSERT INTO units (unit_name, address) VALUES
('pmpuB', 'Университетский проспект, д.35, лит. Б'),
('pmpuD', 'Университетский проспект, д.35, лит. Д'),
('pmpuA', 'Университетский проспект, д.35, лит. А'),
('himfak', 'Университетский проспект, д.26, лит. А');

-- дистанции
INSERT INTO distances (unit_from, unit_to, time_minutes) VALUES
('pmpuB', 'pmpuD', 2),
('pmpuD', 'pmpuB', 2),
('pmpuB', 'pmpuA', 2),
('pmpuA', 'pmpuB', 2),
('pmpuD', 'pmpuA', 2),
('pmpuA', 'pmpuD', 2),
('pmpuB', 'himfak', 15),
('himfak', 'pmpuB', 15),
('pmpuA', 'himfak', 15),
('himfak', 'pmpuA', 15),
('pmpuD', 'himfak', 15),
('himfak', 'pmpuD', 15);

-- кабинеты
INSERT INTO classrooms (number, unit_name, capacity) VALUES
('212Д', 'pmpuD', 30),
('225/1,2', 'pmpuB', 25),
('03', 'himfak', 40),
('2005', 'pmpuA', 35),
('4007', 'pmpuA', 50),
('215Д','pmpuD',45);

insert into positions (id, position_name) values
(1, 'admin'),
(2, 'BD teacher'),
(3, 'Algorythms teacher'),
(4, 'Kubernetes teacher'),
(5, 'BZHD teacher'),
(6, 'IT clinic teacher'),
(7, 'Probability theory teacher');

-- преподаватели (в employees)
INSERT INTO employees (email, first_name, last_name, date_of_birth, experience) VALUES
('korovkin@spb.ru', 'Максим', 'Коровкин', '1978-01-01', 15),
('nikiforov@spb.ru', 'Константин', 'Никифоров', '1978-01-01', 15),
('frolov@spb.ru', 'Сергей', 'Фролов', '1978-01-01', 15),
('maslikov@spb.ru', 'Александр', 'Масликов', '1978-01-01', 15),
('blekanov@spb.ru', 'Иван', 'Блеканов', '1978-01-01', 15),
('svirkin@spb.ru', 'Михаил', 'Свиркин', '1978-01-01', 15);


-- должности-преподаватели
insert into employees_positions (employee_id, position_id) values
('korovkin@spb.ru', 2),
('nikiforov@spb.ru', 3),
('frolov@spb.ru', 4),
('maslikov@spb.ru', 5),
('blekanov@spb.ru', 6),
('svirkin@spb.ru', 7);

-- преподаватели (аккаунт)
INSERT INTO users (user_id, email, password, role, username, employee_id) VALUES
(2,'korovkin@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','korovkin@spb.ru','korovkin@spb.ru'),
(3,'nikiforov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','nikiforov@spb.ru','nikiforov@spb.ru'),
(4,'svirkin@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','svirkin@spb.ru','svirkin@spb.ru'),
(5,'frolov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','frolov@spb.ru','frolov@spb.ru'),
(6,'maslikov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','maslikov@spb.ru','maslikov@spb.ru'),
(7,'blekanov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_TEACHER','blekanov@spb.ru','blekanov@spb.ru');

-- Группы
INSERT INTO groups (group_name) VALUES
('23Б11'),
('23Б12');

-- студенты (в роли)
INSERT INTO students (email, first_name, last_name, date_of_birth, enter_year, level, group_id) VALUES
('semenov@spb.ru', 'Артём', 'Семёнов', '2006-03-18', '2023', 'bac', '23Б11'),
('kurmakaev@spb.ru', 'Нияз', 'Курмакаев', '2005-01-01', '2023', 'bac', '23Б11'),
('testst12@spb.ru', 'Студент', 'Тестовый', '2005-02-12', '2023', 'bac', '23Б12'),
('testst121@spb.ru', 'Тест', 'Студентовый', '2005-02-12', '2023', 'bac', '23Б12');


-- студенты (аккаунт)
INSERT INTO users (user_id, email, password, role, username, student_id) VALUES
(10,'semenov@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','semenov@spb.ru','semenov@spb.ru'),
(11,'kurmakaev@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','kurmakaev@spb.ru','kurmakaev@spb.ru'),
(12,'testst12@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','testst12@spb.ru','testst12@spb.ru'),
(13,'testst121@spb.ru', '$2a$10$4q1VkboFiMNzpQj3bx/XseaOE1l55l/trXzDex5e2zEB0E6x2sezu', 'ROLE_STUDENT','testst121@spb.ru','testst121@spb.ru');

-- дисциплины
INSERT INTO disciplines (discipline_name, group_name, teacher_email, count_hours) VALUES
('Введение в системы баз данных, лекция', '23Б11', 'korovkin@spb.ru', 72),
('Введение в системы баз данных, лекция', '23Б12', 'korovkin@spb.ru', 72),
('Проверочное занятие', '23Б11', 'blekanov@spb.ru', 25),
('Алгоритмы и анализ сложности, лекция', '23Б11', 'nikiforov@spb.ru', 72),
('Алгоритмы и анализ сложности, лекция', '23Б12', 'nikiforov@spb.ru', 72),
('Прикладные задачи теории вероятности, лекция', '23Б11', 'svirkin@spb.ru', 72),
('Прикладные задачи теории вероятности, лекция', '23Б12', 'svirkin@spb.ru', 72),
('Алгоритм и анализ сложности, практика', '23Б11', 'nikiforov@spb.ru', 72),
('Алгоритм и анализ сложности, практика', '23Б12', 'nikiforov@spb.ru', 72),
('Введение в системы баз данных, практика', '23Б11', 'korovkin@spb.ru', 72),
('Введение в системы баз данных, практика', '23Б12', 'korovkin@spb.ru', 72),
('kubernetes', '23Б11', 'frolov@spb.ru', 72),
('kubernetes', '23Б12', 'frolov@spb.ru', 72),
('Безопасность жизнедеятельности', '23Б11', 'maslikov@spb.ru', 72),
('Безопасность жизнедеятельности', '23Б12', 'maslikov@spb.ru', 72),
('Технологии Web', '23Б11', 'blekanov@spb.ru', 72),
('Технологии Web', '23Б12', 'blekanov@spb.ru', 72);

--занятия
DO $$
DECLARE
    d date := '2025-09-01';
BEGIN
    WHILE d <= '2025-12-31' LOOP
        -- Понедельник (0=Sunday, 1=Monday)
        IF EXTRACT(DOW FROM d) = 1 THEN
            INSERT INTO schedule(
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Алгоритмы и анализ сложности, лекция',
                '23Б12',
                'nikiforov@spb.ru',
                '225/1,2',
                'pmpuB',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Алгоритмы и анализ сложности, лекция',
                '23Б11',
                'nikiforov@spb.ru',
                '225/1,2',
                'pmpuB',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Введение в системы баз данных, лекция',
                '23Б11',
                'korovkin@spb.ru',
                '212Д',
                'pmpuD',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Введение в системы баз данных, лекция',
                '23Б12',
                'korovkin@spb.ru',
                '212Д',
                'pmpuD',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Прикладные задачи теории вероятности, лекция',
                '23Б12',
                'svirkin@spb.ru',
                '225/1,2',
                'pmpuB',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Прикладные задачи теории вероятности, лекция',
                '23Б11',
                'svirkin@spb.ru',
                '225/1,2',
                'pmpuB',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Алгоритм и анализ сложности, практика',
                '23Б12',
                'nikiforov@spb.ru',
                '225/1,2',
                'pmpuB',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Алгоритм и анализ сложности, практика',
                '23Б11',
                'nikiforov@spb.ru',
                '225/1,2',
                'pmpuB',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Введение в системы баз данных, практика',
                '23Б11',
                'korovkin@spb.ru',
                '2005',
                'pmpuA',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Введение в системы баз данных, практика',
                '23Б12',
                'korovkin@spb.ru',
                '2005',
                'pmpuA',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'kubernetes',
                '23Б11',
                'frolov@spb.ru',
                '4007',
                'pmpuA',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'kubernetes',
                '23Б11',
                'frolov@spb.ru',
                '4007',
                'pmpuA',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'kubernetes',
                '23Б12',
                'frolov@spb.ru',
                '4007',
                'pmpuA',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'kubernetes',
                '23Б12',
                'frolov@spb.ru',
                '4007',
                'pmpuA',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Безопасность жизнедеятельности',
                '23Б11',
                'maslikov@spb.ru',
                '03',
                'himfak',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Безопасность жизнедеятельности',
                '23Б12',
                'maslikov@spb.ru',
                '03',
                'himfak',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Технологии Web',
                '23Б11',
                'blekanov@spb.ru',
                '215Д',
                'pmpuD',
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
                discipline_name,
                group_name,
                teacher_email,
                classroom_number,
                unit_name,
                date
            ) VALUES (
                'Технологии Web',
                '23Б12',
                'blekanov@spb.ru',
                '215Д',
                'pmpuD',
                (d::text || ' 10:00')::timestamp
            )
            ON CONFLICT DO NOTHING;
        END IF;
        d := d + INTERVAL '1 day';
    END LOOP;
END $$;