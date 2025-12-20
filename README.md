
# Описание
University API - веб-приложение для хранения данных об университете. Целью создания данного api является доступ к расписанию и связанным с ним данными а так же их добавление.
Предметной областью являются студенты и работники ВУЗа, должности, учебные группы, преподаваемые дисциплины, подразделения университета и аудитории, расстояния между подразделениями, занятия.
#### Вход
При входе на сайт будет необходимо ввести логин и пароль. Для не зарегестрированных пользователей реализовано добавление пользователя в ходе которого проверяются существование записи с соответствующим email в таблицах students и employees, а так же корректность указанной роли.



# Данные
#### Схема бд находится в src/scheme/Scheme.png. Схема с указанием связей:src/scheme/Scheme-keys.png.
## Имеются следующие сущности:

#### Student (содержатся в таблице Students)
- id, int, primary key
- email - varchar,  not blank, unique
- first_name - varchar
- last_name - varchar
- date_of_birth - date, not null
- group_id - varchar, not null + ограничение на пустую строку (not blank)
- level - varchar
- enterYear - varchar

Код класса сущности:
```
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "students")
public class Student extends Person{
    @ManyToOne
    @JoinColumn(
            name = "group_id",
            foreignKey = @ForeignKey(
                    name = "group_id",
                    foreignKeyDefinition = "FOREIGN KEY (group_id) REFERENCES groups(group_name) ON UPDATE CASCADE ON DELETE SET NULL"
            )
    )

    private Group group;
    private String level;
    private String enterYear;
}
```



Родительский класс Person:
```
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String email;
    @Transient
    private int age;
    public int getAge() {
        if (dateOfBirth != null) {
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return 0;
    }
}
```

#### Employee (содержатся в таблице employees)
- id, int, primary key
- email - varchar, not blank, unique
- first_name - varchar
- last_name - varchar
- date_of_birth - date, not null
- experience - int

Код класса сущности:
```
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "employees")
@NoArgsConstructor
@Setter
public class Employee extends Person{
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employees_positions",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "position_id")}
    )
    @JsonIgnore
    private List<Position> positions = new ArrayList<>();
    private int experience;

    @OneToMany(mappedBy = "teacherEmail")
    @JsonIgnore
    private List<Disciplines> disciplines = new ArrayList<>();

}
```

#### Position (содержатся в таблице positions)
- id - int, primary key
- position_name - varchar, not blank

Код класса сущности:
```
@Entity
@Data
@Table(name = "positions")
@Getter
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @NotBlank
    private String positionName;

    @ManyToMany(mappedBy = "positions", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();
}
```

#### таблица employees_positions
- employee_id - int, primary key, foreign key на таблицу employee
- position_id - int, primary key, foreign key на таблицу positions

Служит для связи между employees и positions, не имеет сущности в коде.

#### Group (содержатся в таблице groups)
- id, int, primary key, foreign key на таблицу disciplines
- group_name - varchar

Код класса сущности:
```
@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Column(unique = true)
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students = new LinkedList<>();

    @OneToMany(mappedBy = "groupID")
    @JsonIgnore
    private List<Disciplines> disciplines = new ArrayList<>();
}

```

#### Disciplines (содержатся в таблице disciplines)
- id, int, primary key 
- discipline_name - varchar, not blank
- group_id - int, foreign key на таблицу groups 
- teacher_id - int, foreign key на таблицу employees
- count_hours - int

Код класса сущности:
```
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class Disciplines {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotBlank
    private String disciplineName;
    @NotNull
    private Long groupID;
    @NotNull
    private Long teacherID;

    @NotNull
    private int countHours;

    @ManyToOne
    @MapsId("groupID")
    @JoinColumn(
            name = "group_id",
            foreignKey = @ForeignKey(
                    name = "disciplines_groups",
                    foreignKeyDefinition = "FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE RESTRICT"
            ))
    private Group group;

    @ManyToOne
    @MapsId("teacherID")
    @JoinColumn(
            name = "teacher_id",
            foreignKey = @ForeignKey(
                    name = "disciplines_employees",
                    foreignKeyDefinition = "FOREIGN KEY (teacher_id) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE RESTRICT"
            )
    )
    private Employee teacher;

    @OneToMany(mappedBy = "discipline")
    List<Lesson> lessons = new LinkedList<>();

    public void setWithoutId(String disciplineName, Long groupID, Long teacherID, int countHours, Group group, Employee teacher, List<Lesson> lessons){
        this.disciplineName = disciplineName;
        this.groupID = groupID;
        this.teacherID = teacherID;
        this.countHours = countHours;
        this.group = group;
        this.teacher = teacher;
        this.lessons = lessons;
    }
}
```

#### Unit (содержатся в таблице units)

- id, int, primary key
- unit_name - varchar, not blank, unique
- address - varchar, not blank

Код класса сущности:
```
@Data
@Entity
@Table(name = "units")
@NoArgsConstructor
@Setter
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Column(name = "unit_name", unique = true)
    @NotBlank
    String unitName;

    @NotBlank
    String address;

    @JsonIgnore
    @OneToMany(mappedBy = "unit")
    List<ClassRoom> classrooms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "unitF")
    List<Distance> distances = new ArrayList<>();
}
```

#### ClassRoom (содержатся в таблице classrooms)

- id, int, primary key
- number - varchar, not blank
- unit_id - int, foreign key на таблицу units
- capacity - int

Код класса сущности:
```
@Entity
@Data
@Table(name = "classrooms")
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Column(name = "number")
    @NotBlank
    String classroomNumber;
    @Column(name = "unit_id")
    @NotNull
    Long unitID;

    int capacity;

    @ManyToOne
    @MapsId("unitID")
    @JoinColumn(
            name = "unit_id",
            foreignKey = @ForeignKey(
                    name = "units_classrooms",
                    foreignKeyDefinition = "FOREIGN KEY (unit_id) REFERENCES units(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    Unit unit;

    @JsonIgnore
    @OneToMany(mappedBy = "classroom")
    List<Lesson> lessons = new LinkedList<>();
}
```

#### Distance (содержатся в таблице distances)

- unit_from - int, primary key, foreign key на таблицу units
- unit_to - int, primary key, foreign key на таблицу units
- time_minutes - int

Код класса сущности:
```
@Entity
@Data
@Table(name = "distances")
public class Distance {
    @EmbeddedId
    DistanceKey id;

    @Column(name = "time_minutes")
    int timeMinutes;

    @ManyToOne
    @MapsId("unitFrom")
    @JoinColumn(
            name = "unit_from",
            foreignKey = @ForeignKey(
                    name = "units_distances",
                    foreignKeyDefinition = "FOREIGN KEY (unit_from) REFERENCES units(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    Unit unitF;

    @ManyToOne
    @MapsId("unitTo")
    @JoinColumn(
            name = "unit_to",
            foreignKey = @ForeignKey(
                    name = "units_distances_to",
                    foreignKeyDefinition = "FOREIGN KEY (unit_to) REFERENCES units(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    Unit unitT;
}
```

Класс составного ключа:
```
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class DistanceKey implements Serializable {
    @Column(name = "unit_from")
    Long unitFrom;

    @Column(name = "unit_to")
    Long unitTo;
}
```
#### Lesson (содержатся в таблице schedule)

- discipline_id - int, primary key
- classroom_id - int, primary key
- date - datetime, primary key

Код класса сущности:
```
@Data
@Entity
@Table(name = "schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @EmbeddedId
    LessonKey id;

    @ManyToOne
    @MapsId("classroom")
    @JoinColumns(
            foreignKey = @ForeignKey(
                    name = "classrooms_schedule",
                    foreignKeyDefinition = "FOREIGN KEY (classroom_number, unit_name) REFERENCES classrooms(number, unit_name) ON UPDATE CASCADE ON DELETE CASCADE"
            ),
            value = {
                    @JoinColumn(
                            name = "classroom_number",
                            referencedColumnName = "number"
                    ),
                    @JoinColumn(
                            name = "unit_name",
                            referencedColumnName = "unit_name"
                    )
            }
    )
    ClassRoom classroom;

    @ManyToOne
    @MapsId("discipline")
    @JoinColumns(
            foreignKey = @ForeignKey(
                    name = "disciplines_schedule",
                    foreignKeyDefinition = "FOREIGN KEY (teacher_email, discipline_name, group_name) REFERENCES disciplines(teacher_email, discipline_name, group_name) ON UPDATE CASCADE ON DELETE CASCADE"
            ),
            value = {
                    @JoinColumn(
                            name = "discipline_name",
                            referencedColumnName = "discipline_name"
                    ),
                    @JoinColumn(
                            name = "group_name",
                            referencedColumnName = "group_name"
                    ),
                    @JoinColumn(
                            name = "teacher_email",
                            referencedColumnName = "teacher_email"
                    )
            }
    )
    Disciplines discipline;
}
```

Класс составного ключа:
```
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class LessonKey implements Serializable {
    Long discipline;

    Long classroom;

    Date date;
}
```

# Политика каскадных операций
На внешние ключи таблиц были установлены следующие ограничения:
- students-groups: ON UPDATE CASCADE ON DELETE SET NULL
- groups-disciplines: ON UPDATE CASCADE ON DELETE RESTRICT
- employees-disciplines: ON UPDATE CASCADE ON DELETE RESTRICT
- disciplines-schedule: ON UPDATE CASCADE ON DELETE CASCADE
- units-classrooms: ON UPDATE CASCADE ON DELETE CASCADE
- classrooms-schedule: ON UPDATE CASCADE ON DELETE CASCADE
- units-distances: ON UPDATE CASCADE ON DELETE CASCADE на оба ключа (подразделения отправки и прибытия)

# Пользовательские роли
#### Студент ("ROLE_STUDENT")
Функционал для студента:
При входе студента на сайт у него буду доступны 4 метода. 
- Узнать расписание указанной группы на неделю.
-  Узнать расписание указанной группы за весь период, сохраненный в бд.
-  Получить информацию о дисциплинах указанной группы и преподавателях, которые их ведут. Данные о преподавателях ограничены полями (email, first_name, last_name, positions, experience)
-  Получить информацию о себе, сохраненную в приложении. При выполнении данного запроса система проверяет авторизованного на данный момент пользователя и выдаёт информацию о нём.
-  Получить информацию о том, какие группы есть.
#### Преподаватель ("ROLE_TEACHER")
Функционал для преподавателя:
При входе преподавателя доступны 6 методов. 
- Узнать расписание указанного преподавателя на неделю.
-  Узнать расписание указанного преподавателя за весь период, сохраненный в бд
-  Получить информацию о группах, в которых он преподает.  При выполнении данного запроса система проверяет авторизованного на данный момент пользователя и выдаёт группы, у которых ведёт авторизованный преподаватель.
-  Получить информацию о студентах из групп, в которых он преподает. Выполняется аналогичная проверка, можно отправить запрос о студентах группы, в которой авторизованный преподаватель ведёт какую-либо дисциплину. Данные о студентах ограничены полями (email, first_name, last_name, group, enter_year)
-  Получить информацию о себе, сохраненную в приложении. При выполнении данного запроса система проверяет авторизованного на данный момент пользователя и выдаёт информацию о нём.
-  Получить информацию о том какие дисциплины в каких группах преподаёт указанный преподаватель.
-  Получить информацию о других преподавателях.
#### Администратор("ROLE_ADMIN")
Функционал для администратора:
При входе администратора доступны методы для получения, редактирования, добавления и удаления любых записей в БД. Также администратор имеет доступ к методам, предназначенным для студентов и преподавателей, за исключением getGroups (у преподавателя) и getInfo(у студента и преподавателя), так как эти методы используют информацию о зарегестрированном на данный момент пользователе.
Также администратору доступно добавление расписания. Можно смотреть уже добавленное расписание, а так же добавлять новые занятия. 
При добавлении расписания реализованы следующие проверки:
- Корректность аудитории. Проверяется, что аудитория, в которой ставится занятие, может вместить всю группу, у которой ставится занятие.
- Проверка пересечения групп. Если ставится занятие у нескольких групп, также проверяется вместимость аудитории.
- Корректность времени. Проверяется, что занятие не ставится на то же время, на которое уже стоит занятие у той же группы.
- Проверка корректности подразделения. Администратор не сможет поставить соседние занятия в подразделениях вуза, путь между которыми занимает более, чем время между занятиями.
- Количество часов. В случае, если количество часов, рассчитанных программой на неделю, исходя из сохраненного в базе количества часов на семестр будет меньше/больше того, что администратор добавил - программа будет высвечивать предупреждение. 

# Технологии разработки

Restful API.
Язык - Java.
Фреймворк - Spring.
СУБД - PostgreSQL.
Для связи Java-кода с СУБД использована библиотека Hibernate.
Для авторизации и выставления ролей - Spring security.


# Запуск приложения
## Вариант 1 - docker (необходимо: docker desktop)
1) Запуск контейнеров
В терминале в корне проекта прописать
'''
docker-compose up
'''
2) Заполнение БД (опционально. для демонстрации)
- Переходим на localhost:5050 (веб pgAdmin).  ->
- Для входа - логин: admin@admin.com, пароль: root ->
- Подключаемся к серверу
- Port:5432
- Maintenance database: postgres
- Username: postgres
- Password:210ro10 ->
- далее в Servers/<имя, которое вы дали>/Databases/student_db/Schemas/public/Tables открываем Query tool (правая кнопка мыши -> Query tool) и вставляем в окно скрипт, лежащий в корне проекта (init_db.sql)

3) Переход в документацию приложения: http://localhost:8080/swagger-ui/index.html#/

## Вариант 2 - IntellijIDEA (Необходимо: jdk17+, pgAdmin4, postgreSQL)
1) Для запуска из ide необходимо предварительно развернуть сервер

В pgAdmin4:
- Add New Server -> 
- во вкладке General: вводим любое имя -> 
- во вкладке Connection:
- Host name/address: localhost
- Port:5432
- Maintenance database: postgres
- Username: postgres
- Password:210ro10

2) В ide:
- 2.1) Заходим в src/main/java/StudentAppApplication
- 2.2) Запускаем его (зеленый треугольник сверху)

3) Заполнение БД (опционально. для демонстрации)
- в pgAdmin4:
- в Servers/<имя, которое вы дали>/Databases/student_db/Schemas/public/Tables открываем Query tool (правая кнопка мыши -> Query tool) и вставляем в окно скрипт, лежащий в корне проекта (init_db.sql)

4)Для перехода на страницу документации открываем http://localhost:8080/swagger-ui/index.html#/

# Стандартные данные БД
после выполнения init_db.sql будет:
- Созданно 2 группы (23Б11, 23Б12) и забито расписание на семестр под них
- Создано 4 студенческих аккаунта:
- semenov@spb.ru, kurmakaev@spb.ru, testst12@spb.ru, testst121@spb.ru. Первые двое - группа 23Б11, остальные - группа 23Б12
- Создано 6 преподавательских аккаунтов (а также их должности + должность admin):
- korovkin@spb.ru, nikiforov@spb.ru, svirkin@spb.ru, frolov@spb.ru, maslikov@spb.ru, blekanov@spb.ru
- создано 4 подразделения ВУЗа, для них заполнены дистанции и аудитории

#### !!! Для всех студентов и преподавателей из скрипта инициализации пароль: 123 !!! администратор - (INIT_ADMIN, INIT_ADMIN)
