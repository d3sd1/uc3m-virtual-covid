package org.uc3m.covid.virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uc3m.covid.virtual.entity.Subject;
import org.uc3m.covid.virtual.entity.User;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCourseUc3mId(int courseUc3mId);
}

