package com.githubx.usersms.controller;

import com.github.g.users.server.users.model.DeleteResponse;
import com.github.g.users.server.users.model.UserResponse;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.model.User;
import com.githubx.usersms.service.contratos.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    // will create a MockHttpServletRequest per test when needed

    @InjectMocks
    private UserController userController;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().userId("u1").username("jdoe").email("jdoe@example.com").firstName("John").lastName("Doe").build();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("user_db_id", "u1")
                .subject("u1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUser_returnsUserResponse() {
        when(userService.getUser("u1")).thenReturn(sampleUser);
        var resp = userController.getUser("u1");
        assertEquals(200, resp.getStatusCode().value());
        UserResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals("u1", body.getUserId());
    }

    @Test
    void listUsers_returnsPage() {
        Page<User> page = new PageImpl<>(List.of(sampleUser));
        when(userService.listUsers(0, 10)).thenReturn(page);
        var resp = userController.getListUsers(0, 10);
        assertEquals(200, resp.getStatusCode().value());
        var body = resp.getBody();
        assertNotNull(body);
        assertEquals(1, body.getTotalElements());
    }

    @Test
    void createUser_invokesServiceAndReturnsCreated() {
        UserRequest req = new UserRequest();
        req.setUsername("jdoe");
        req.setEmail("jdoe@example.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        when(userService.createUser(any(), any())).thenReturn(sampleUser);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        var resp = userController.createUser(req, servletRequest);
        assertEquals(201, resp.getStatusCode().value());
        UserResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals("u1", body.getUserId());
    }

    @Test
    void deleteUser_returnsDeleteResponse() {
        when(userService.deleteUser("u1")).thenReturn(new DeleteResponse("u1", true));
        var resp = userController.deleteUser("u1");
        assertEquals(200, resp.getStatusCode().value());
        var body = resp.getBody();
        assertNotNull(body);
        assertTrue(body.getSuccess());
    }
}

