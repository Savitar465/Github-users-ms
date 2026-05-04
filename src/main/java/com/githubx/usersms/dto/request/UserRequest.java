package com.githubx.usersms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    String lastName;
    String email;
    String username;
    String firstName;
    String password;
}
