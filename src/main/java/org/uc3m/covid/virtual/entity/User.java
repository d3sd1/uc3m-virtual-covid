package org.uc3m.covid.virtual.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Table(name = "users")
public class User {
    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "id_uc3m", unique = true, nullable = false, updatable = false)
    private long uc3mId;

    @JsonIgnore
    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "uc3m_password", unique = false, nullable = false, updatable = true)
    private String uc3mPassword;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "full_name", unique = false, nullable = false, updatable = true)
    private String fullName;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 250)
    @Column(name = "email", unique = false, nullable = false, updatable = true)
    private String email;

    @NotNull
    @Column(name = "user_moodle_id", unique = true, nullable = false, updatable = true)
    private long moodleId;

    @NotBlank
    @Column(name = "jwt", unique = false, nullable = true, updatable = true)
    private String jwt;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_subjects",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_id")})
    private Set<Subject> subjects = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUc3mId() {
        return uc3mId;
    }

    public void setUc3mId(long uc3mId) {
        this.uc3mId = uc3mId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMoodleId() {
        return moodleId;
    }

    public void setMoodleId(long moodleId) {
        this.moodleId = moodleId;
    }

    @JsonIgnore
    public String getUc3mPassword() {
        return uc3mPassword;
    }

    @JsonIgnore
    public void setUc3mPassword(String uc3mPassword) {
        this.uc3mPassword = uc3mPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getUc3mId() == user.getUc3mId() &&
                Objects.equals(getUc3mPassword(), user.getUc3mPassword()) &&
                Objects.equals(getFullName(), user.getFullName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getJwt(), user.getJwt()) &&
                Objects.equals(getSubjects(), user.getSubjects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUc3mId(), getUc3mPassword(), getFullName(), getEmail(), getJwt(), getSubjects());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uc3mId=" + uc3mId +
                ", uc3mPassword='" + uc3mPassword + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", jwt='" + jwt + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}
