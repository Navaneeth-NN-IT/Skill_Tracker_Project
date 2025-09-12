
package com.example.skill_tracker_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_tracker_project.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * Spring Data JPA automatically implements this method based on its name.
     *
     * @param email the email of the user to find
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     * This is more efficient than fetching the whole entity.
     *
     * @param email the email to check for
     * @return true if a user with the email exists, false otherwise
     */
    Boolean existsByEmail(String email);
}