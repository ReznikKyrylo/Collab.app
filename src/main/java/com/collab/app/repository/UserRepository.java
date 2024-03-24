package com.collab.app.repository;

import com.collab.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where email =?1", nativeQuery = true)
    User getUserByEmail(String email);
}
