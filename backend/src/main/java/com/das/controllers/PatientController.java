package com.das.controllers;

import com.das.DTOs.AppointmentDTO;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient")
public class PatientController implements SecuredController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @GetMapping("{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(patientService.getPatient(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<Patient>> getPatients(Pageable p) {
        return new ResponseEntity<>(patientService.getPatients(p), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        return new ResponseEntity<>(patientService.addPatient(patientRequest), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientRequest patientRequest) {
        return new ResponseEntity<>(patientService.updatePatient(id, patientRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/appointments")
    public ResponseEntity<CollectionResponse<AppointmentDTO>> getEmployeeAppointments(
            @PathVariable Integer id,
            @RequestParam(value = "dateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(value = "showPreceding", required = false, defaultValue = "false") boolean showPreceding,
            Pageable pageable) {
        CollectionResponse<AppointmentDTO> response = appointmentService.getAppointmentsByPatientId(id, dateTime, showPreceding, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
