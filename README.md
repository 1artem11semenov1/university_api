
# Описание
University API - веб-приложение для хранения данных об университете. Целью создания данного сайта является автоматизация работы с информацией о вузе. 
#### Вход
При входе на сайт будет необходимо ввести логин пароль. В случае если введены несуществующие - будет добавлен новый пользователь, в случае, если его удалось найти среди работников или студентов.
#### Функционал для студента
При входе студента на сайт у него буду доступны 4 вкладки. 
- Узнать расписание своей группы на неделю
-  Узнать расписание своей группы за весь период, сохраненный в бд
-  Получить информацию о дисциплинах своей группы и преподавателях, которые их ведут
-  Получить информацию о себе, сохраненную в приложении.
#### Функционал для преподавателя
При входе преподавателя доступны 4 вкладки. 
- Узнать своё расписание на неделю
-  Узнать своё расписание за весь период, сохраненный в бд
-  Получить информацию о группах и студентах, у которых он преподает
-  Получить информацию о себе, сохраненную в приложении.
#### Функционал для администратора.
При входе администратора будут доступны 6 вкладок. 
- Получить список всех студентов, редактировать/удалять выбранного.
- Получить список всех работников, редактировать/удалять выбранного. 
- Получить список всех групп, редактировать/удалять выбранную.
- Получить список всех дисциплин, редактировать/удалять выбранную.
- Получить список всех должностей, редактировать/удалять выбранную.
- Получить список всех подразделений ВУЗа, редактировать/удалять выбранное, так же с этой вкладки возможен переход на аудитории в соответствующем подразделении.
- Добавление расписания. Можно смотреть уже добавленное расписание, а так же добавлять новые занятия. В случае, если количество часов, рассчитанных программой на неделю, исходя из сохраненного в базе количества часов на семестр будет меньше/больше того, что администратор добавил - программа будет высвечивать предупреждение. Так же приложение будет проверять, чтобы администратор не ставил соседние занятия в подразделениях вуза, путь между которыми занимает более 10 минут, и что аудитория, в которой ставится занятие, может вместить всю группу, у которой ставится занятие.

Предметной областью являются студенты и работники ВУЗа, преподаваемые дисциплины, подразделения университета и аудитории.


# Данные
#### Схема бд находится в src/scheme/scheme_full.png
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
    @NotBlank
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
    private String firstName;
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
    @ManyToMany
    @JoinTable(
            name = "employees_positions",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "position_id")}
    )
    @JsonIgnore
    private Set<Position> positions = new HashSet<>();
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
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @NotBlank
    private String positionName;

    @ManyToMany(mappedBy = "positions")
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
public class Disciplines {
    @EmbeddedId
    private DisciplinesKey id;

    @NotNull
    private int countHours

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
public class Unit {
    @Id
    @Column(name = "unit_name")
    String unitName;

    @NotBlank
    String address;

    @OneToMany(mappedBy = "unitName")
    List<ClassRoom> classrooms = new ArrayList<>();

    @OneToMany(mappedBy = "unitFrom")
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
    Unit unitName;

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
    DisctanceKey id;

    @Column(name = "time_minutes")
    int timeMinutes;

    @ManyToOne
    @MapsId("unitFrom")
    @JoinColumn(name = "unit_from")
    Unit unitFrom;
}
```

Класс составного ключа:
```
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class DisctanceKey implements Serializable {
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

# Пользовательские роли
Планирую создать 3 роли: студент, преподаватель и администратор. 
- Студент сможет получить информацию о своей группе, дисциплинах и преподавателях, которые их ведут. 
- Преподаватель - о том, какие дисциплины в каких группах он ведёт. 
- Администратор может добавлять и удалять студентов и преподавателей, редактировать информацию о них. Обновлять распиание.
 
# Технологии разработки

Restful API.
Язык - Java.
Фреймворк - Spring boot.
СУБД - PostgreSQL.
Для связи Java-кода с СУБД использована библиотека Hibernate.
