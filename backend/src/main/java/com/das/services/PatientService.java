package com.das.services;

import com.das.entities.Patient;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.PatientRepository;
import com.das.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
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

    public Patient addPatient(Patient service) {
        service.setId(null);

        return patientRepository.save(service);
    }

    @Transactional
    public Patient updatePatient(Integer id, Patient updatedPatient) {
        Patient service = getPatientOrThrow(id);

        service.setAddress(updatedPatient.getAddress());
        service.setName(updatedPatient.getName());
        service.setDateOfBirth(updatedPatient.getDateOfBirth());
        service.setPhoneNumber(updatedPatient.getPhoneNumber());
        service.setEmail(updatedPatient.getEmail());

        return patientRepository.save(service);
    }

    @Transactional
    public void deletePatient(Integer id) {
        Patient service = getPatientOrThrow(id);

        patientRepository.delete(service);
    }

    private Patient getPatientOrThrow(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", id));
    }
}
