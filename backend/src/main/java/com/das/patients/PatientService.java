package com.das.patients;

import com.das.common.exceptions.EmailNotAvailableException;
import com.das.common.exceptions.ResourceNotFoundException;
import com.das.common.responses.CollectionResponse;
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
        checkEmailUniqueness(patientRequest.getEmail());
        Patient patient = modelMapper.map(patientRequest, Patient.class);

        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Integer id, PatientRequest updatedPatient) {
        Patient patient = getPatientOrThrow(id);
        if (!patient.getEmail().equals(updatedPatient.getEmail())) checkEmailUniqueness(updatedPatient.getEmail());

        modelMapper.map(updatedPatient, patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Integer id) {
        Patient patient = getPatientOrThrow(id);

        patientRepository.delete(patient);
    }

    private void checkEmailUniqueness(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new EmailNotAvailableException(email);
        }
    }

    private Patient getPatientOrThrow(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", id));
    }
}
