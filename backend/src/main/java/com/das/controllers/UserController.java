package com.das.controllers;

import com.das.config.AppConstants;
import com.das.entities.Appointment;
import com.das.payloads.UserCreateDTO;
import com.das.payloads.UserDTO;
import com.das.payloads.UserUpdateDTO;
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
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return new ResponseEntity<>(userService.addUser(userCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(userService.updateUser(id, userUpdateDTO), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/appointments")
    public ResponseEntity<CollectionResponse<Appointment>> getEmployeeAppointments(
            @PathVariable Integer id,
            @RequestParam(value = "dateTime", required = false) LocalDateTime dateTime,
            @RequestParam(value = "showPreceding", required = false, defaultValue = "false") boolean showPreceding,
            Pageable pageable) {
        CollectionResponse<Appointment> response = appointmentService.getAppointmentsByEmployeeId(id, dateTime, showPreceding, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
