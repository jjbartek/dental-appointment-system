package com.das.DTOs;

import com.das.entities.Role;
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
    private List<Role> roles;
}
