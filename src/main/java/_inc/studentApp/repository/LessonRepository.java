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

    @Query(
            value = "SELECT classroom_id, discipline_id, date FROM schedule" +
                    " JOIN disciplines ON schedule.discipline_id = disciplines.id" +
                    " JOIN groups ON disciplines.group_id = groups.id" +
                    " WHERE group_name = :inp_group " +
                    " and DATE(date) >= DATE(:date_week_start) " +
                    " and DATE(date) <= DATE(:date_week_end) "
            , nativeQuery = true
    )
    List<Lesson> findLessonsOnWeek(@Param("inp_group") String groupName
            , @Param("date_week_start") Date start
            , @Param("date_week_end") Date end);

    @Query(value = "SELECT classroom_id, discipline_id, date FROM schedule" +
            " JOIN disciplines ON schedule.discipline_id = disciplines.id" +
            " JOIN groups ON disciplines.group_id = groups.id" +
            " WHERE group_name = :inp_group "
            , nativeQuery = true)
    List<Lesson> findAllWithGroup(@Param("inp_group") String groupName);


    @Query(
            value = "SELECT classroom_id, discipline_id, date  FROM schedule" +
                    " JOIN disciplines ON discipline_id = disciplines.id" +
                    " JOIN employees ON disciplines.teacher_id = employees.id" +
                    " WHERE employees.email = :inp_email" +
                    " and DATE(date) >= DATE(:date_week_start) " +
                    " and DATE(date) <= DATE(:date_week_end) "
            , nativeQuery = true
    )
    List<Lesson> findTeacherLessonsOnWeek(@Param("inp_email") String email
            , @Param("date_week_start") Date start
            , @Param("date_week_end") Date end);

    @Query(value = "SELECT * FROM schedule" +
            " JOIN disciplines ON discipline_id = disciplines.id" +
            " JOIN employees ON disciplines.teacher_id = employees.id" +
            " WHERE employees.email = :inp_email"
            , nativeQuery = true)
    List<Lesson> findAllWithTeacher(@Param("inp_email") String email);

}
