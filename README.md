
# Описание
University API - веб-приложение для хранения данных об университете. Целью создания данного api является доступ к расписанию и связанным с ним данными а так же их добавление.
Предметной областью являются студенты и работники ВУЗа, должности, учебные группы, преподаваемые дисциплины, подразделения университета и аудитории, расстояния между подразделениями, занятия.
#### Вход
При входе на сайт будет необходимо ввести логин и пароль. Для не зарегестрированных пользователей реализовано добавление пользователя в ходе которого проверяются существование записи с соответствующим email в таблицах students и employees, а так же корректность указанной роли.



# Данные
#### Схема бд находится в src/scheme/Scheme.png. Схема с указанием связей:src/scheme/Scheme-keys.png.
## Имеются следующие сущности:

#### Student (содержатся в таблице Students)
- email - varchar, primary key
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
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateOfBirth;
    @Id
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
- email - varchar, primary key
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
- position_id - varchar, primary key, foreign key на таблицу positions

Служит для связи между employees и positions, не имеет сущности в коде.

#### Group (содержатся в таблице groups)
- group_name - varchar, primary key, foreign key на таблицу disciplines

Код класса сущности:
```
@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students = new LinkedList<>();
    @OneToMany(mappedBy = "groupName", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Disciplines> disciplines = new ArrayList<>();
}
```

#### Disciplines (содержатся в таблице disciplines)
- discipline_name - varchar, primary key
- group_name - varchar, primary key, foreign key на таблицу groups
- teacher_email - varchar, primary key, foreign key на таблицу employees
- count_hours - int

Код класса сущности:
```
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Disciplines {
    @EmbeddedId
    private DisciplinesKey id;

    @NotNull
    private int countHours;

    @ManyToOne
    @MapsId("groupName")
    @JoinColumn(name = "group_name")
    private Group groupName;

    @ManyToOne
    @MapsId("teacherEmail")
    @JoinColumn(name = "teacher_email")
    private Employee teacherEmail;

    public String getDisciplineName(){
        return this.id.getName();
    }

    @OneToMany(mappedBy = "discipline")
    List<Lesson> lessons = new LinkedList<>();
}
```

Класс DisciplinesKey (содержит описание составного ключа для Discipline):
```
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class DisciplinesKey implements Serializable {
    @Column(name = "discipline_name")
    private String disciplineName;
    private String groupName;
    private String teacherEmail;

    public String getName() {return disciplineName;}
}
```

#### Unit (содержатся в таблице units)

- unit_name - varchar, primary key
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
    @Column(name = "unit_name")
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

- number - varchar, primary key
- unit_name - varchar, primary key, foreign key на таблицу units
- capacity - int

Код класса сущности:
```
@Entity
@Data
@Table(name = "classrooms")
public class ClassRoom {
    @EmbeddedId
    ClassRoomKey id;
    int capacity;

    @ManyToOne
    @MapsId("unitName")
    @JoinColumn(name = "unit_name")
    @JsonIgnore
    Unit unit;

    @JsonIgnore
    @OneToMany(mappedBy = "classroom")
    List<Lesson> lessons = new LinkedList<>();
}
```

Класс составного ключа:
```
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ClassRoomKey implements Serializable {
    @Column(name = "number")
    String classroomNumber;
    @Column(name = "unit_name")
    String unitName;
}
```

#### Distance (содержатся в таблице distances)

- unit_from - varchar, primary key, foreign key на таблицу units
- unit_to - varchar, primary key
- time_minutes int

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
    @JoinColumn(name = "unit_from")
    @JsonIgnore
    Unit unitF;

    @ManyToOne
    @MapsId("unitTo")
    @JoinColumn(name = "unit_to")
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
    String unitFrom;

    @Column(name = "unit_to")
    String unitTo;
}
```
#### Lesson (содержатся в таблице schedule)

- discipline_name - varchar, foreign key на таблицу disciplines
- group_name - varchar, primary key, foreign key на таблицу disciplines
- teacher_email - varchar, primary key, foreign key на таблицу disciplines
- number - int, foreign key на таблицу classrooms
- unit_name - varchar, primary key, foreign key на таблицу classrooms
- date - datetime
- идентифицируется по всем полям

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
    @JoinColumns({
            @JoinColumn(name = "classroom_number", referencedColumnName = "number"),
            @JoinColumn (name = "unit_name", referencedColumnName = "unit_name")
    })
    ClassRoom classroom;

    @ManyToOne
    @MapsId("discipline")
    @JoinColumns({
            @JoinColumn(name = "discipline_name", referencedColumnName = "discipline_name"),
            @JoinColumn (name = "group_name", referencedColumnName = "group_name"),
            @JoinColumn (name = "teacher_email", referencedColumnName = "teacher_email")
    })
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
    @Embedded
    DisciplinesKey discipline;
    @Embedded
    ClassRoomKey classroom;

    Date date;
}
```

# Ограничения целостности
На внешние ключи таблиц были установлены следующие ограничения:
- students-groups: ON UPDATE CASCADE ON DELETE SET NULL
- groups-disciplines: ON UPDATE CASCADE ON DELETE RESTRICT
- disciplines-schedule: ON UPDATE CASCADE ON DELETE RESTRICT
- units-classrooms: ON UPDATE CASCADE ON DELETE RESTRICT
- classrooms-schedule: ON UPDATE CASCADE ON DELETE RESTRICT
- units-distances: ON UPDATE CASCADE ON DELETE RESTRICT на оба ключа (подразделения отправки и прибытия)

# Пользовательские роли
#### Студент ("ROLE_STUDENT")
Функционал для студента:
При входе студента на сайт у него буду доступны 4 метода. 
- Узнать расписание своей группы на неделю
-  Узнать расписание своей группы за весь период, сохраненный в бд
-  Получить информацию о дисциплинах своей группы и преподавателях, которые их ведут
-  Получить информацию о себе, сохраненную в приложении.
#### Преподаватель ("ROLE_TEACHER")
Функционал для преподавателя:
При входе преподавателя доступны 6 методов. 
- Узнать своё расписание на неделю
-  Узнать своё расписание за весь период, сохраненный в бд
-  Получить информацию о группах, в которых он преподает
-  Получить информацию о студентах из групп, в которых он преподает
-  Получить информацию о себе, сохраненную в приложении.
-  Получить информацию о том какие дисциплины в каких группах он преподаёт
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
Фреймворк - Spring boot.
СУБД - PostgreSQL.
Для связи Java-кода с СУБД использована библиотека Hibernate.
