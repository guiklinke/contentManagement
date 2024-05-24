package com.ContentAPI.ContentAPI.repositories;

import com.ContentAPI.ContentAPI.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);

    @Query("SELECT u.id FROM users u WHERE u.login = :login")
    Long findIdByLogin(@Param("login") String login);

}