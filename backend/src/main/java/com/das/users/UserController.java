package com.das.users;

import com.das.appointments.DTOs.SimplifiedAppointmentDTO;
import com.das.users.DTOs.UserDTO;
import com.das.common.controllers.SecuredController;
import com.das.users.requests.UserCreateRequest;
import com.das.users.requests.UserUpdateRequest;
import com.das.common.responses.CollectionResponse;
import com.das.appointments.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "User")
public class UserController implements SecuredController {
    private final UserService userService;
    private final AppointmentService appointmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("search/{nameOrEmail}")
    public ResponseEntity<CollectionResponse<UserDTO>> getUsersByNameOrEmail(@PathVariable String nameOrEmail, Pageable pageable) {
        return new ResponseEntity<>(userService.getUsersByNameOrEmail(nameOrEmail, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CollectionResponse<UserDTO>> getUsers(Pageable pageable) {
        return new ResponseEntity<>(userService.getUsers(pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return new ResponseEntity<>(userService.addUser(userCreateRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return new ResponseEntity<>(userService.updateUser(id, userUpdateRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'EMPLOYEE')")
    @GetMapping("{id}/appointments")
    public ResponseEntity<CollectionResponse<SimplifiedAppointmentDTO>> getEmployeeAppointments(
            @PathVariable Integer id,
            @RequestParam(value = "dateTime", required = false) LocalDateTime dateTime,
            @RequestParam(value = "showPreceding", required = false, defaultValue = "false") boolean showPreceding,
            Pageable pageable) {
        CollectionResponse<SimplifiedAppointmentDTO> response = appointmentService.getAppointmentsByEmployeeId(id, dateTime, showPreceding, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
