package com.githubx.usersms.dao;

import com.githubx.usersms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepositoryImplementation<User, String> {

    /**
     * Method that searches for a user by username
     *
     * @param username user name
     * @return returns a user
     */

    boolean existsByUsername(String username);

    /**
     * Method that searches for users by status and sorts by creation date
     *
     * @param status   user status
     * @param pageable pagination
     * @return returns a list of users
     */
    Page<User> findAllByStatusOrderByCreatedDate(Integer status, Pageable pageable);

    /**
     * Method that searches for a user by email
     *
     * @param email user email
     * @return returns a user
     */
    boolean existsByEmail(String email);

    /**
     * Method that searches for a user by username
     *
     * @param username user name
     * @return returns a user
     */
    User findByUsername(String username);


    /**
     * Method that searches for a user by first name, last name or username
     *
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param username Username of the user
     * @return List of users that meet the search criteria
     */
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameContainingIgnoreCase
    (Pageable pageable, String firstName, String lastName, String username);
}