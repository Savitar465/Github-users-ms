package com.githubx.usersms.dto.request;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRolRequest {
    String userId;
    String lastName;
    String email;
    String username;
    String password;
    String firstName;
    Date birthDate;
    List<RolRequest> roles;
}