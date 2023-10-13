package com.das.users;

import com.das.users.DTOs.UserDTO;
import com.das.common.exceptions.EmailNotAvailableException;
import com.das.common.exceptions.ResourceNotFoundException;
import com.das.users.entities.User;
import com.das.users.entities.UserRole;
import com.das.users.requests.UserCreateRequest;
import com.das.users.requests.UserUpdateRequest;
import com.das.common.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public CollectionResponse<UserDTO> getUsers(Pageable p) {
        Page<User> page = userRepository.findAll(p);

        return getResponseFromPage(page);
    }

    public CollectionResponse<UserDTO> getUsersByNameOrEmail(String nameOrEmail, Pageable p) {
        Page<User> page = userRepository.findByEmailOrName(nameOrEmail, p);

        return getResponseFromPage(page);
    }

    @Transactional
    public UserDTO addUser(UserCreateRequest userCreateRequest) {
        checkEmailUniqueness(userCreateRequest.getEmail());
        userCreateRequest.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        User user = modelMapper.map(userCreateRequest, User.class);
        user = userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public UserDTO updateUser(Integer id, UserUpdateRequest userUpdateRequest) {
        User user = getUserOrThrow(id);
        if (!user.getEmail().equals(userUpdateRequest.getEmail())) checkEmailUniqueness(userUpdateRequest.getEmail());

        user.setName(userUpdateRequest.getName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setRoles(userUpdateRequest.getRoles().stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toList()));

        if (userUpdateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        user = userRepository.save(user);


        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = getUserOrThrow(id);
        userRepository.delete(user);
    }

    public UserDTO getUserById(Integer id) {
        User user = getUserOrThrow(id);
        return modelMapper.map(user, UserDTO.class);
    }

    private void checkEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailNotAvailableException(email);
        }
    }

    private CollectionResponse<UserDTO> getResponseFromPage(Page<User> page) {
        List<UserDTO> users = page.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return CollectionResponse.<UserDTO>builder()
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
