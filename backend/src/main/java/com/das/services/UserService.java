package com.das.services;

import com.das.entities.User;
import com.das.exceptions.ResourceNotFoundException;
import com.das.payloads.UserCreateDTO;
import com.das.payloads.UserDTO;
import com.das.payloads.UserUpdateDTO;
import com.das.repositories.UserRepository;
import com.das.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUsers(Integer pageNumber, Integer pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(p);

        return getResponseFromPage(page);
    }

    public UserResponse getUserByNameOrEmail(String nameOrEmail, Integer pageNumber, Integer pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findByEmailOrName(nameOrEmail, nameOrEmail, p);

        return getResponseFromPage(page);
    }

    public UserDTO addUser(UserCreateDTO userCreateDTO) {
        User user = modelMapper.map(userCreateDTO, User.class);
        user = userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO updateUser(Integer id, UserUpdateDTO userUpdateDTO) {
        User user = getUserOrThrow(id);

        user.setName(userUpdateDTO.getName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setRoles(userUpdateDTO.getRoles());

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        user = userRepository.save(user);


        return modelMapper.map(user, UserDTO.class);
    }

    public void deleteUser(Integer id) {
        User user = getUserOrThrow(id);
        userRepository.delete(user);
    }

    public UserDTO getUserById(Integer id) {
        User user = getUserOrThrow(id);
        return modelMapper.map(user, UserDTO.class);
    }

    private UserResponse getResponseFromPage(Page<User> page) {
        List<UserDTO> users = page.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return UserResponse.builder()
                .content(users)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    private User getUserOrThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", id));
    }
}
