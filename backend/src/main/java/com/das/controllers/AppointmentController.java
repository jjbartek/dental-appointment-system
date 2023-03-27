package com.das.controllers;

import com.das.config.AppConstants;
import com.das.entities.Appointment;
import com.das.responses.CollectionResponse;
import com.das.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<CollectionResponse<Appointment>> getAppointments(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "date", required = false) Date date,
            @RequestParam(value = "time", required = false) Time time) {
        CollectionResponse<Appointment> response = appointmentService.getAppointments(pageNumber, pageSize, date, time);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> addAppointment(@RequestBody @Valid Appointment appointment) {
        return new ResponseEntity<>(appointmentService.addAppointment(appointment), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable("id") Integer id, @RequestBody @Valid Appointment appointment) {
        return new ResponseEntity<>(appointmentService.updateAppointment(id, appointment), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Integer id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
