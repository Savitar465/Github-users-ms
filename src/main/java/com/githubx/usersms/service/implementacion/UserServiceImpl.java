package com.githubx.usersms.service.implementacion;

import com.github.g.users.server.users.model.DeleteResponse;
import com.githubx.usersms.abstracts.EstadoAbstract;
import com.githubx.usersms.criteria.SearchSpecification;
import com.githubx.usersms.criteria.models.SearchRequest;
import com.githubx.usersms.dao.UserDao;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.model.Transaction;
import com.githubx.usersms.model.User;
import com.githubx.usersms.service.contratos.KeycloakService;
import com.githubx.usersms.service.contratos.UserService;
import com.githubx.usersms.util.errorhandling.exceptions.EntityConflictException;
import com.githubx.usersms.util.errorhandling.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final KeycloakService keycloakService;
    private final UserDao userDao;


    public UserServiceImpl(UserDao userDao, KeycloakService keycloakService) {
        this.userDao = userDao;
        this.keycloakService = keycloakService;
    }

    @Override
    public Page<User> listUsers(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userDao.findAllByStatusOrderByCreatedDate(EstadoAbstract.ACTIVO, pageable);
    }

    @Override
    public User getUser(String userId) {
        return userDao.findById(userId).orElseThrow(() -> {
            log.error("No user exists with id: {}", userId);
            return new EntityNotFoundException("No user exists with id: " + userId);
        });
    }

    @Override
    public User createUser(@Valid UserRequest userRequest, Transaction transaction) {
        if (userDao.existsByUsername(userRequest.getUsername())) {
            log.error("User with username {} already exists", userRequest.getUsername());
            throw new EntityConflictException("User with username " + userRequest.getUsername() + " already exists");
        }
        UserRepresentation userRepresentation = keycloakService.createKeycloakUser(userRequest);
        if (userRepresentation == null) {
            log.error("Error creating user in Keycloak");
            throw new EntityConflictException("Error creating user in Keycloak");
        }
        transaction.setTrUserId(userRepresentation.getId());
        User user = User.builder()
                .userId(userRepresentation.getId())
                .email(userRequest.getEmail())
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .status(EstadoAbstract.ACTIVO)
                .createdByUser(transaction.getTrUserId())
                .createdDate(transaction.getTrFecha())
                .modifiedByUser(transaction.getTrUserId())
                .modifiedDate(transaction.getTrFecha()).build();

        return userDao.save(user);
    }

    @Override
    public DeleteResponse deleteUser(String userId) {
        User userToDelete = userDao.findById(userId).orElseThrow(() -> {
            log.error("User with ID '{}' does not exist in the system.", userId);
            return new EntityNotFoundException("User with ID '" + userId + "' does not exist in the system.");
        });
        if (userToDelete.getStatus().equals(EstadoAbstract.INACTIVO)) {
            log.error("User with ID '{}' has already been deleted previously.", userId);
            throw new EntityConflictException("User with ID '" + userId + "' has already been deleted previously.");
        } else {
            userToDelete.setStatus(EstadoAbstract.INACTIVO);
            userDao.save(userToDelete);
            log.info("User with ID '{}' has been successfully deleted.", userId);
            return new DeleteResponse(userId, true);
        }
    }

    @Override
    public User editUser(String userId, UserRequest userRequest, Transaction transaction) {
        User userToEdit = getUserByIdInternal(userId);
        if (userToEdit.getStatus().equals(EstadoAbstract.INACTIVO)) {
            log.error("User with id: {} is deleted", userId);
            throw new EntityConflictException("User with id: " + userId + " is deleted");
        } else {
            userToEdit.setUsername(userRequest.getUsername());
            userToEdit.setEmail(userRequest.getEmail());
            userToEdit.setFirstName(userRequest.getFirstName());
            userToEdit.setLastName(userRequest.getLastName());
            userToEdit.setModifiedByUser(transaction.getTrUserId());
            userToEdit.setModifiedDate(transaction.getTrFecha());
            User updatedUser = userDao.save(userToEdit);
            log.info(updatedUser.getUserId());
            return updatedUser;
        }
    }

    public User getUserByIdInternal(String userId) {
        return userDao.findById(userId).orElseThrow(() -> {
            log.error("User with id:{} does not exist", userId);
            return new EntityNotFoundException("User with id: " + userId + " does not exist");
        });
    }

    @Override
    public Page<User> listUsersSearch(SearchRequest searchRequest) {
        SearchSpecification<User> specification = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("User: {}", userDao.findAll(specification, pageable));
        return userDao.findAll(specification, pageable);
    }
}