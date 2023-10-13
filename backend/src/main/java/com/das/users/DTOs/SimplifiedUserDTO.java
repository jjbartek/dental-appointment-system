package com.das.users.DTOs;

import com.das.users.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedUserDTO {
    private Integer id;
    private String name;
    private List<UserRole> roles;
}
