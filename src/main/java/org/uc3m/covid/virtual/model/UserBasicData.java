package org.uc3m.covid.virtual.model;

import org.uc3m.covid.virtual.entity.Subject;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserBasicData {
    private String fullName;
    private String email;
    private long userMoodleId;
    private Set<Subject> subjects;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUserMoodleId() {
        return userMoodleId;
    }

    public void setUserMoodleId(long userMoodleId) {
        this.userMoodleId = userMoodleId;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
