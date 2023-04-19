package com.das.controllers;

import com.das.DTOs.AppointmentDTO;
import com.das.DTOs.SimplifiedAppointmentDTO;
import com.das.requests.AppointmentRequest;
import com.das.responses.CollectionResponse;
import com.das.services.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment")
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
public class AppointmentController implements SecuredController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<CollectionResponse<SimplifiedAppointmentDTO>> getAppointments(
            @RequestParam(value = "dateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(value = "showPreceding", required = false, defaultValue = "false") boolean showPreceding,
            Pageable pageable) {
        CollectionResponse<SimplifiedAppointmentDTO> response = appointmentService.getAppointments(dateTime, showPreceding, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody @Valid AppointmentRequest appointment) {
        return new ResponseEntity<>(appointmentService.addAppointment(appointment), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable("id") Integer id, @RequestBody @Valid AppointmentRequest appointment) {
        return new ResponseEntity<>(appointmentService.updateAppointment(id, appointment), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Integer id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
