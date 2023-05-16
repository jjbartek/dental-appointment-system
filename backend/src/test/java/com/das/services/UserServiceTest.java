package com.das.services;

import com.das.DTOs.UserDTO;
import com.das.entities.Role;
import com.das.entities.User;
import com.das.exceptions.EmailNotAvailableException;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.UserRepository;
import com.das.requests.UserCreateRequest;
import com.das.requests.UserUpdateRequest;
import com.das.responses.CollectionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, modelMapper, passwordEncoder);
    }

    @Test
    void testGetUsers_shouldReturnResponse() {
        //given
        Page<User> page = new PageImpl<>(List.of(
                new User("User 1", "user1@gmail.com", "password", List.of(Role.EMPLOYEE)),
                new User("User 2", "user2@gmail.com", "password", List.of(Role.EMPLOYEE))));
        List<UserDTO> userDTOS = List.of(
                new UserDTO(1, "User 1", "user1@gmail.com", List.of(Role.EMPLOYEE)),
                new UserDTO(2, "User 2", "user2@gmail.com", List.of(Role.EMPLOYEE)));

        CollectionResponse<UserDTO> expectedResponse = CollectionResponse.<UserDTO>builder()
                .content(userDTOS)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();

        //when
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(eq(page.getContent().get(0)), eq(UserDTO.class))).thenReturn(userDTOS.get(0));
        when(modelMapper.map(eq(page.getContent().get(1)), eq(UserDTO.class))).thenReturn(userDTOS.get(1));
        CollectionResponse<UserDTO> response = underTest.getUsers(Pageable.ofSize(10));

        //then
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void testAddUser_shouldAddUser() {
        //given
        String email = "user1@gmail.com";
        String password = "password";
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("User 1", email, password, List.of("EMPLOYEE"));

        User mappedUser = new User("User 1", email, "hashedPassword", List.of(Role.EMPLOYEE));
        User createdUser = new User(1, "User 1", email, "hashedPassword", List.of(Role.EMPLOYEE));
        UserDTO userDTO = new UserDTO(1, "User 1", email, List.of(Role.EMPLOYEE));

        //when
        when(userRepository.existsByEmail(eq(email))).thenReturn(false);
        when(passwordEncoder.encode(eq(password))).thenReturn("hashedPassword");
        when(modelMapper.map(eq(userCreateRequest), eq(User.class))).thenReturn(mappedUser);
        when(userRepository.save(eq(mappedUser))).thenReturn(createdUser);
        when(modelMapper.map(eq(createdUser), eq(UserDTO.class))).thenReturn(userDTO);
        UserDTO userDTOResponse = underTest.addUser(userCreateRequest);

        //then
        assertThat(userDTOResponse).isEqualTo(userDTO);
    }

    @Test
    void testAddUser_shouldThrowEmailNotAvailable() {
        //given
        String email = "user1@gmail.com";
        String password = "password";
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("User 1", email, password, List.of("EMPLOYEE"));

        //when
        when(userRepository.existsByEmail(eq(email))).thenReturn(true);

        //then
        assertThatExceptionOfType(EmailNotAvailableException.class)
                .isThrownBy(() -> underTest.addUser(userCreateRequest))
                .withMessage("Email " + email + " is already in use");
    }

    @Test
    void testUpdateUser_shouldThrowResourceNotFound() {
        //given
        Integer id = 5;
        UserUpdateRequest userUpdateRequest =
                new UserUpdateRequest("User 1", "u1@gmail.com", null, List.of("EMPLOYEE"));

        //when
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());


        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.updateUser(id, userUpdateRequest))
                .withMessage("User not found with user id: " + id);
    }

    @Test
    void testUpdateUser_shouldThrowEmailNotAvailable() {
        //given
        Integer id = 5;
        String newEmail = "diffeent@gmail.com";
        User existingUser =
                new User(5, "User 1", "u1@gmail.com", "hashedPassword", List.of(Role.EMPLOYEE));
        UserUpdateRequest userUpdateRequest =
                new UserUpdateRequest("User 1", newEmail, null, List.of("EMPLOYEE"));

        //when
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(eq(newEmail))).thenReturn(true);

        //then
        assertThatExceptionOfType(EmailNotAvailableException.class)
                .isThrownBy(() -> underTest.updateUser(id, userUpdateRequest))
                .withMessage("Email " + newEmail + " is already in use");
    }

    @Test
    void testUpdateUser_shouldUpdateUserWithEmailAndPasswordUnchanged() {
        //given
        Integer id = 5;
        User existingUser =
                new User(5, "User 1", "u1@gmail.com", "hashedPassword", List.of(Role.EMPLOYEE));
        UserUpdateRequest userUpdateRequest =
                new UserUpdateRequest("Different name", "u1@gmail.com", null, List.of("ADMIN"));

        //when
        ArgumentCaptor<User> capturedUser = ArgumentCaptor.forClass(User.class);
        User expectedUser =
                new User(5, "Different name", "u1@gmail.com", "hashedPassword", List.of(Role.ADMIN));
        UserDTO expectedUserDTO = new UserDTO(1, "Different name", "u1@gmail.com", List.of(Role.ADMIN));

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(existingUser));
        when(userRepository.save(capturedUser.capture())).thenReturn(expectedUser);
        when(modelMapper.map(eq(expectedUser), eq(UserDTO.class))).thenReturn(expectedUserDTO);
        UserDTO userDTOResult = underTest.updateUser(5, userUpdateRequest);


        //then
        verify(userRepository, never()).existsByEmail(any());
        verify(passwordEncoder, never()).encode(any());

        assertThat(capturedUser.getValue()).isEqualTo(expectedUser);
        assertThat(userDTOResult).isEqualTo(expectedUserDTO);
    }

    @Test
    void testUpdateUser_shouldUpdateUserWithEmailAndPasswordChanged() {
        //given
        Integer id = 5;
        String newEmail = "newemail@gmail.com";
        String newPassword = "newPassword";
        User existingUser =
                new User(5, "User 1", "u1@gmail.com", "hashedPassword", List.of(Role.EMPLOYEE));
        UserUpdateRequest userUpdateRequest =
                new UserUpdateRequest("Different name", newEmail, newPassword, List.of("ADMIN"));

        //when
        String encodedNewPassword = "encodedNewPassword";
        ArgumentCaptor<User> capturedUser = ArgumentCaptor.forClass(User.class);
        User expectedUser =
                new User(5, "Different name", newEmail, encodedNewPassword, List.of(Role.ADMIN));
        UserDTO expectedUserDTO = new UserDTO(1, "Different name", newEmail, List.of(Role.ADMIN));

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(existingUser));
        when(userRepository.save(capturedUser.capture())).thenReturn(expectedUser);
        when(userRepository.existsByEmail(eq(newEmail))).thenReturn(false);
        when(passwordEncoder.encode(eq(newPassword))).thenReturn(encodedNewPassword);
        when(modelMapper.map(eq(expectedUser), eq(UserDTO.class))).thenReturn(expectedUserDTO);
        UserDTO userDTOResult = underTest.updateUser(5, userUpdateRequest);

        //then
        assertThat(capturedUser.getValue()).isEqualTo(expectedUser);
        assertThat(userDTOResult).isEqualTo(expectedUserDTO);
    }

    @Test
    void testDeleteUser_shouldThrowResourceNotFound() {
        //given
        Integer id = 5;

        //when
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.deleteUser(id))
                .withMessage("User not found with user id: " + id);
    }

    @Test
    void testDeleteUser_shouldDeleteUser() {
        //given
        Integer id = 5;
        User existingUser =
                new User(5, "User 1", "u1@gmail.com", "hashedPassword", List.of(Role.EMPLOYEE));

        //when
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(existingUser));
        underTest.deleteUser(id);

        //then
        verify(userRepository, times(1)).delete(eq(existingUser));
    }

    @Test
    void testGetUserById_shouldThrowResourceNotFound() {
        //given
        Integer id = 5;

        //when
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.getUserById(id))
                .withMessage("User not found with user id: " + id);
    }

    @Test
    void testGetUserById_shouldReturnUserDTO() {
        //given
        Integer id = 5;
        User existingUser =
                new User(5, "User 1", "u1@gmail.com", "hashedPassword", List.of(Role.EMPLOYEE));

        //when
        UserDTO expectedUserDTO = new UserDTO(1, "User 1", "u1@gmail.com", List.of(Role.EMPLOYEE));
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(existingUser));
        when(modelMapper.map(eq(existingUser), eq(UserDTO.class))).thenReturn(expectedUserDTO);
        UserDTO userDTOResult = underTest.getUserById(id);

        //then
        assertThat(userDTOResult).isEqualTo(expectedUserDTO);
    }

    @Test
    void testGetUsersByNameOrEmail_shouldReturnEmptyCollectionResponse() {
        //given
        String givenString = "lorem";

        //when
        Page<User> page = Page.empty();
        when(userRepository.findByEmailOrName(any(), any())).thenReturn(page);
        CollectionResponse<UserDTO> collectionResponseResult = underTest.getUsersByNameOrEmail(givenString, null);

        //then
        CollectionResponse<UserDTO> expectedResponse = CollectionResponse.<UserDTO>builder()
                .content(List.of())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();

        assertThat(collectionResponseResult).isEqualTo(expectedResponse);
    }

    @Test
    void testGetUsersByNameOrEmail_shouldReturnCollectionResponse() {
        //given
        String givenString = "user";
        Page<User> page = new PageImpl<>(List.of(
                new User("user 1", "anemail@gmail.com", "password", List.of(Role.EMPLOYEE)),
                new User("user 2", "user2@gmail.com", "password", List.of(Role.EMPLOYEE))));
        List<UserDTO> userDTOS = List.of(
                new UserDTO(1, "user 1", "anemail@gmail.com", List.of(Role.EMPLOYEE)),
                new UserDTO(2, "user 2", "user2@gmail.com", List.of(Role.EMPLOYEE)));

        //when
        when(userRepository.findByEmailOrName(any(), any())).thenReturn(page);
        when(modelMapper.map(eq(page.getContent().get(0)), eq(UserDTO.class))).thenReturn(userDTOS.get(0));
        when(modelMapper.map(eq(page.getContent().get(1)), eq(UserDTO.class))).thenReturn(userDTOS.get(1));
        CollectionResponse<UserDTO> collectionResponseResult = underTest.getUsersByNameOrEmail(givenString, null);

        //then
        CollectionResponse<UserDTO> expectedResponse = CollectionResponse.<UserDTO>builder()
                .content(userDTOS)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();

        assertThat(collectionResponseResult).isEqualTo(expectedResponse);
    }


}