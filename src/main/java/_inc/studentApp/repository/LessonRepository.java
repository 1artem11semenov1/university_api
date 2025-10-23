package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, LessonKey> {
    @Query(value = "SELECT * FROM schedule" +
            " WHERE DATE(date) = DATE(:inp_date)"
            , nativeQuery = true)
    List<Lesson> findLessonsByDate(@Param("inp_date") Date date);
}
