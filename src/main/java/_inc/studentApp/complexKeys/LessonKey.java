package _inc.studentApp.complexKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

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