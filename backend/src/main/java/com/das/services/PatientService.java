package com.das.services;

import com.das.entities.Patient;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.PatientRepository;
import com.das.requests.PatientRequest;
import com.das.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public Patient getPatient(Integer id) {
        return getPatientOrThrow(id);
    }

    public CollectionResponse<Patient> getPatients(Pageable p) {
        Page<Patient> page = patientRepository.findAll(p);

        return CollectionResponse.<Patient>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    public Patient addPatient(PatientRequest patientRequest) {
        Patient patient = modelMapper.map(patientRequest, Patient.class);

        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Integer id, PatientRequest updatedPatient) {
        Patient patient = getPatientOrThrow(id);

        modelMapper.map(updatedPatient, patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Integer id) {
        Patient patient = getPatientOrThrow(id);

        patientRepository.delete(patient);
    }

    private Patient getPatientOrThrow(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", id));
    }
}
