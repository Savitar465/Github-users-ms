package com.githubx.usersms.service.implementacion;

import com.github.g.users.server.users.model.DeleteResponse;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.model.Transaction;
import com.githubx.usersms.model.User;
import com.githubx.usersms.service.contratos.KeycloakService;
import com.githubx.usersms.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @Mock
    private UserDao userDao;

    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .userId("u1")
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void listUsers_delegatesToDao() {
        Page<User> page = new PageImpl<>(List.of(sampleUser));
        when(userDao.findAllByStatusOrderByCreatedDate(any(), any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.listUsers(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userDao).findAllByStatusOrderByCreatedDate(any(), any(Pageable.class));
    }

    @Test
    void getUser_returnsWhenFound() {
        when(userDao.findById("u1")).thenReturn(Optional.of(sampleUser));
        User u = userService.getUser("u1");
        assertNotNull(u);
        assertEquals("u1", u.getUserId());
    }

    @Test
    void getUser_throwsWhenNotFound() {
        when(userDao.findById("notfound")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUser("notfound"));
    }

    @Test
    void createUser_conflictWhenUsernameExists() {
        UserRequest req = new UserRequest();
        req.setUsername("jdoe");
        when(userDao.existsByUsername("jdoe")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> userService.createUser(req, new Transaction()));
    }

    @Test
    void createUser_success() {
        UserRequest req = new UserRequest();
        req.setUsername("jdoe");
        req.setEmail("jdoe@example.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        when(userDao.existsByUsername("jdoe")).thenReturn(false);

        UserRepresentation rep = new UserRepresentation();
        rep.setId("u1");
        rep.setUsername("jdoe");
        when(keycloakService.createKeycloakUser(any())).thenReturn(rep);

        when(userDao.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction trx = new Transaction();
        trx.setTrFecha(new java.util.Date());
        User created = userService.createUser(req, trx);

        assertNotNull(created);
        assertEquals("u1", created.getUserId());
        verify(userDao).save(any(User.class));
    }

    @Test
    void deleteUser_success() {
        when(userDao.findById("u1")).thenReturn(Optional.of(sampleUser));
        sampleUser.setStatus(1); // activo
        when(userDao.save(any(User.class))).thenReturn(sampleUser);
        DeleteResponse resp = userService.deleteUser("u1");
        assertNotNull(resp);
        assertTrue(resp.getSuccess());
        verify(userDao).save(any(User.class));
    }
}

