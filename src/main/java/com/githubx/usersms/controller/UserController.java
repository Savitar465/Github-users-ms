package com.githubx.usersms.controller;

import com.github.g.users.server.users.api.V1Api;
import com.github.g.users.server.users.model.DeleteResponse;
import com.github.g.users.server.users.model.UserResponse;
import com.githubx.usersms.criteria.models.SearchRequest;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.mapper.UserMapper;
import com.githubx.usersms.model.Transaction;
import com.githubx.usersms.service.contratos.UserService;
import com.githubx.usersms.util.JwtExtractUserUtil;
import com.githubx.usersms.util.TransactionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/users")
@Slf4j
public class UserController implements V1Api {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to get the list of users
     *
     * @param page     page number [0..n]
     * @param pageSize number of items per page
     * @return ResponseEntity containing the list of users and HTTP status 200
     */
    @Operation(
            summary = "Get paginated list of users",
            description = "This endpoint allows you to retrieve a paginated list of registered users." +
                    " It is mandatory to provide the `page` and `pageSize` parameters to control pagination.",
            parameters = {
                    @Parameter(name = "pageSize", description = "Number of records that will be displayed per page.", required = true),
                    @Parameter(name = "page", description = "Number of the page you want to query.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation. The list of users was obtained successfully."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserResponse>> getListUsers(
            @RequestParam(name = "page") @Min(0) int page,
            @RequestParam(name = "pageSize") @Positive int pageSize) {

        Page<UserResponse> users = userService.listUsers(page, pageSize)
                .map(UserMapper.INSTANCE::userToUserResponse);
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint to get a user by its ID
     *
     * @param userId ID of the user to retrieve
     * @return ResponseEntity containing the UserResponse object and HTTP status 200
     */
    @Operation(
            summary = "Get details of a specific user",
            description = "This endpoint allows you to retrieve detailed information of a registered user." +
                    " It is necessary to provide the `userId` parameter, which uniquely identifies the requested user.",
            parameters = {
                    @Parameter(name = "userId", description = "Unique identifier of the user (UUID)", required = true),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation. The user was retrieved successfully."
                    )
            }
    )
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) {
        UserResponse userResponse = UserMapper.INSTANCE
                .userToUserResponse(userService.getUser(userId));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * Endpoint to create a new user
     *
     * @param userRequest object containing the data of the new user
     * @return ResponseEntity containing the UserResponse object and HTTP status 201
     */
    @Operation(
            summary = "Register a new user",
            description = "This endpoint allows the creation of a new user by providing the required data " +
                    "in the request body. If the operation is successful, it returns the details of the created user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data needed to create a new user",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful operation. The user was created successfully."
                    )
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest,
                                                   HttpServletRequest request) {
        Transaction transaction = TransactionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        UserResponse user = UserMapper.INSTANCE
                .userToUserResponse(userService.createUser(userRequest, transaction));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Endpoint to delete a user by its ID
     *
     * @param userId ID of the user to delete
     * @return ResponseEntity containing an EliminarResponse object and HTTP status 200
     */
    @Operation(
            summary = "Delete an existing user",
            description = "This endpoint allows you to permanently delete a user. It is necessary to provide the `userId` parameter, which uniquely identifies the user you want to delete.",
            parameters = {
                    @Parameter(name = "userId", description = "Identifier of the user", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation. The user has been successfully deleted."
                    )
            }
    )
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    /**
     * Endpoint to edit an existing user
     *
     * @param userId      ID of the user to edit
     * @param userRequest object containing the updated data of the user
     * @return ResponseEntity containing the edited UserResponse object and HTTP status 200
     */
    @Operation(
            summary = "Edit an existing user",
            description = "This endpoint allows you to update the details of a registered user. It is necessary to provide the `userId` parameter, which uniquely identifies the user to be edited.",
            parameters = {
                    @Parameter(name = "userId", description = "Unique identifier of the user", required = true),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data needed to edit an existing user",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation. The user was updated correctly."
                    )
            }
    )
    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> editUser(@PathVariable("userId") String userId,
                                                 @RequestBody UserRequest userRequest, HttpServletRequest request) {
        Transaction transaccion = TransactionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        UserResponse userResponse = UserMapper.INSTANCE
                .userToUserResponse(userService.editUser(userId, userRequest, transaccion));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * Endpoint to search a list of users by filter
     *
     * @param searchRequest search filter
     * @return ResponseEntity containing the UserResponse object and HTTP status 200
     */
    @Operation(
            summary = "Search users by filter",
            description = "This endpoint allows you to search for users by filter. It is necessary to provide the `filter` parameter," +
                    " which uniquely identifies the user to be filtered.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data needed to search for users by filter",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation. The filtered users were retrieved correctly."
                    )
            }
    )
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserResponse>> searchUsers(@RequestBody SearchRequest searchRequest) {
        Page<UserResponse> search = userService.listUsersSearch(searchRequest).map(UserMapper.INSTANCE::userToUserResponse);
        return new ResponseEntity<>(search, HttpStatus.OK);
    }

}
