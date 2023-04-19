package com.das.controllers;

import com.das.DTOs.SimplifiedAppointmentDTO;
import com.das.entities.Patient;
import com.das.requests.PatientRequest;
import com.das.responses.CollectionResponse;
import com.das.services.AppointmentService;
import com.das.services.PatientService;
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
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient")
public class PatientController implements SecuredController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'EMPLOYEE')")
    @GetMapping("{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(patientService.getPatient(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<CollectionResponse<Patient>> getPatients(Pageable p) {
        return new ResponseEntity<>(patientService.getPatients(p), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        return new ResponseEntity<>(patientService.addPatient(patientRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientRequest patientRequest) {
        return new ResponseEntity<>(patientService.updatePatient(id, patientRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'EMPLOYEE')")
    @GetMapping("{id}/appointments")
    public ResponseEntity<CollectionResponse<SimplifiedAppointmentDTO>> getPatientAppointments(
            @PathVariable Integer id,
            @RequestParam(value = "dateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(value = "showPreceding", required = false, defaultValue = "false") boolean showPreceding,
            Pageable pageable) {
        CollectionResponse<SimplifiedAppointmentDTO> response = appointmentService.getAppointmentsByPatientId(id, dateTime, showPreceding, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
