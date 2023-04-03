package com.das.controllers;

import com.das.DTOs.AppointmentDTO;
import com.das.DTOs.UserDTO;
import com.das.config.AppConstants;
import com.das.requests.UserCreateRequest;
import com.das.requests.UserUpdateRequest;
import com.das.responses.CollectionResponse;
import com.das.services.AppointmentService;
import com.das.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AppointmentService appointmentService;

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("search/{nameOrEmail}")
    public ResponseEntity<CollectionResponse<UserDTO>> getUserByNameOrEmail(
            @PathVariable String nameOrEmail,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(userService.getUserByNameOrEmail(nameOrEmail, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<UserDTO>> getUsers(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(userService.getUsers(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return new ResponseEntity<>(userService.addUser(userCreateRequest), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return new ResponseEntity<>(userService.updateUser(id, userUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/appointments")
    public ResponseEntity<CollectionResponse<AppointmentDTO>> getEmployeeAppointments(
            @PathVariable Integer id,
            @RequestParam(value = "dateTime", required = false) LocalDateTime dateTime,
            @RequestParam(value = "showPreceding", required = false, defaultValue = "false") boolean showPreceding,
            Pageable pageable) {
        CollectionResponse<AppointmentDTO> response = appointmentService.getAppointmentsByEmployeeId(id, dateTime, showPreceding, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
