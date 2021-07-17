package org.uc3m.covid.virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uc3m.covid.virtual.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUc3mId(long uc3mId);
}

