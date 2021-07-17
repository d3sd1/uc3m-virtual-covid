package org.uc3m.covid.virtual.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotBlank
    @Column(name = "subject_name", unique = false, nullable = false, updatable = true)
    private String name;

    @NotNull
    @Column(name = "course_uc3m_id", unique = false, nullable = true, updatable = false)
    private int courseUc3mId;

    @Column(name = "course_mag_id", unique = false, nullable = true, updatable = false)
    private Integer moodleCourseMagId = null;

    @Column(name = "course_pra_id", unique = false, nullable = true, updatable = false)
    private Integer moodleCoursePraId = null;


    @NotNull
    @Column(name = "from_year", unique = false, nullable = false, updatable = true)
    private byte fromYear;

    @NotNull
    @Column(name = "to_year", unique = false, nullable = false, updatable = true)
    private byte toYear;

    @NotNull
    @Column(name = "semester", unique = false, nullable = false, updatable = true)
    private byte semester;

    @NotNull
    @Column(name = "course_group_id", unique = false, nullable = false, updatable = true)
    private int group;

    public int getCourseUc3mId() {
        return courseUc3mId;
    }

    public void setCourseUc3mId(int courseUc3mId) {
        this.courseUc3mId = courseUc3mId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoodleCourseMagId() {
        return moodleCourseMagId == null ? 0:moodleCourseMagId;
    }

    public void setMoodleCourseMagId(int moodleCourseMagId) {
        this.moodleCourseMagId = moodleCourseMagId;
    }

    public int getMoodleCoursePraId() {
        return moodleCoursePraId == null ? 0:moodleCoursePraId;
    }

    public void setMoodleCoursePraId(int moodleCoursePraId) {
        this.moodleCoursePraId = moodleCoursePraId;
    }

    public byte getFromYear() {
        return fromYear;
    }

    public void setFromYear(byte fromYear) {
        this.fromYear = fromYear;
    }

    public byte getToYear() {
        return toYear;
    }

    public void setToYear(byte toYear) {
        this.toYear = toYear;
    }

    public byte getSemester() {
        return semester;
    }

    public void setSemester(byte semester) {
        this.semester = semester;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", moodleCourseMagId=" + moodleCourseMagId +
                ", moodleCoursePraId=" + moodleCoursePraId +
                ", fromYear=" + fromYear +
                ", toYear=" + toYear +
                ", semester=" + semester +
                ", group=" + group +
                '}';
    }
}
