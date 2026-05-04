package com.githubx.usersms.service.contratos;

import com.github.g.users.server.users.model.DeleteResponse;
import com.githubx.usersms.criteria.models.SearchRequest;
import com.githubx.usersms.model.Transaction;

import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.model.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;


public interface UserService {

    /**
     * Method that lists users
     *
     * @param page     page number [0..n]
     * @param pageSize number of items per page
     * @return returns a list of users
     */
    Page<User> listUsers(Integer page, Integer pageSize);

    /**
     * Method to get a user
     *
     * @param userId user identifier
     * @return returns the user response
     */
    User getUser(String userId);

    /**
     * Method that creates a user
     *
     * @param userRequest receives a user request
     * @param transaction receives transaction information
     * @return returns a created user response
     */
    User createUser(@Valid UserRequest userRequest, Transaction transaction);

    /**
     * Method that deletes a user by its id
     *
     * @param userId id of the user to delete
     */
    DeleteResponse deleteUser(String userId);

    /**
     * Edits an existing User.
     *
     * @param userId      receives a user Id
     * @param userRequest receives a user request with updated information
     * @param transaction receives transaction information
     * @return returns the updated user
     */
    User editUser(String userId, UserRequest userRequest, Transaction transaction);


    /**
     * Method that lists users using a filter
     *
     * @param searchRequest receives search criteria
     * @return returns a list of filtered users
     */
    Page<User> listUsersSearch(SearchRequest searchRequest);
}