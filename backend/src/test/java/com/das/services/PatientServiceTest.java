package com.das.services;

import com.das.entities.Gender;
import com.das.entities.Patient;
import com.das.exceptions.EmailNotAvailableException;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.PatientRepository;
import com.das.requests.PatientRequest;
import com.das.responses.CollectionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ModelMapper modelMapper;
    private PatientService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PatientService(patientRepository, modelMapper);
    }

    @Test
    void testGetPatient_shouldThrowResourceNotFound() {
        //given
        Integer id = 2;

        //when
        when(patientRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.getPatient(id))
                .withMessage("Patient not found with patient id: " + id);
    }

    @Test
    void testGetPatients_shouldReturnCollection() {
        //given
        List<Patient> listOfPatients = List.of(
                new Patient(
                        2,
                        "Charles Beatrix", "222888111", "charles.beatrix@gmail.com", "Ave Street 22",
                        LocalDate.of(1980, 6, 10),
                        Gender.MALE
                ),
                new Patient(
                        3,
                        "Jon Snow", "111222333", "jon.snow@gmail.com", "Winterfell 11",
                        LocalDate.of(1990, 5, 22),
                        Gender.MALE
                ));
        Page<Patient> pageOfPatients = new PageImpl<>(listOfPatients);
        CollectionResponse<Patient> expectedCollection = CollectionResponse.<Patient>builder()
                .content(pageOfPatients.getContent())
                .pageNumber(pageOfPatients.getNumber())
                .pageSize(pageOfPatients.getSize())
                .totalElements(pageOfPatients.getTotalElements())
                .totalPages(pageOfPatients.getTotalPages())
                .lastPage(pageOfPatients.isLast())
                .build();

        //when
        when(patientRepository.findAll(any(Pageable.class))).thenReturn(pageOfPatients);
        CollectionResponse<Patient> resultCollection = underTest.getPatients(Pageable.ofSize(10));

        //then
        assertThat(resultCollection).isEqualTo(expectedCollection);
    }

    @Test
    void testAddPatient_shouldThrowEmailNotAvailable() {
        //given
        String email = "jon.snow@gmail.com";
        PatientRequest newPatientRequest = new PatientRequest("Jon Snow", "111222333", email, "Winterfell 11",
                LocalDate.of(1990, 5, 22), "MALE");

        //when
        when(patientRepository.existsByEmail(eq(email))).thenReturn(true);

        //then
        assertThatExceptionOfType(EmailNotAvailableException.class)
                .isThrownBy(() -> underTest.addPatient(newPatientRequest))
                .withMessage("Email " + email + " is already in use");
    }

    @Test
    void testAddPatient_shouldAddAndReturnPatient() {
        //given
        String email = "jon.snow@gmail.com";
        PatientRequest newPatientRequest = new PatientRequest("Jon Snow", "111222333", email, "Winterfell 11",
                LocalDate.of(1990, 5, 22), "MALE");
        Patient mappedPatient = new Patient(null, "Jon Snow", "111222333", email, "Winterfell 11",
                LocalDate.of(1990, 5, 22), Gender.MALE);
        Patient savedPatient = new Patient(10, "Jon Snow", "111222333", email, "Winterfell 11",
                LocalDate.of(1990, 5, 22), Gender.MALE);

        //when
        when(patientRepository.existsByEmail(eq(email))).thenReturn(false);
        when(modelMapper.map(eq(newPatientRequest), eq(Patient.class))).thenReturn(mappedPatient);
        when(patientRepository.save(eq(mappedPatient))).thenReturn(savedPatient);
        Patient resultPatient = underTest.addPatient(newPatientRequest);

        //then
        assertThat(resultPatient).isEqualTo(savedPatient);
    }

    @Test
    void testUpdatePatient_shouldThrowResourceNotFound() {
        //given
        String email = "jon.snow@gmail.com";
        Integer id = 10;
        PatientRequest updatePatientRequest = new PatientRequest("Jon Snow", "111222333", email, "Winterfell 11",
                LocalDate.of(1990, 5, 22), "MALE");

        //when
        when(patientRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.updatePatient(id, updatePatientRequest))
                .withMessage("Patient not found with patient id: " + id);
    }

    @Test
    void testUpdatePatient_shouldThrowEmailNotAvailable() {
        //given
        Integer id = 10;
        String newEmail = "anna.stark@gmail.com";
        Patient existingPatient = new Patient(id, "Jon Snow", "111222333", "jon.snow@gmail.com", "Winterfell 11",
                LocalDate.of(1990, 5, 22), Gender.MALE);
        PatientRequest updatePatientRequest = new PatientRequest("Aria Stark", "111222335", newEmail, "Winterfell 12",
                LocalDate.of(1993, 6, 30), "FEMALE");

        //when
        when(patientRepository.findById(eq(id))).thenReturn(Optional.of(existingPatient));
        when(patientRepository.existsByEmail(eq(newEmail))).thenReturn(true);

        //then
        assertThatExceptionOfType(EmailNotAvailableException.class)
                .isThrownBy(() -> underTest.updatePatient(id, updatePatientRequest))
                .withMessage("Email " + newEmail + " is already in use");
    }

    @Test
    void testUpdatePatient_shouldUpdateAndReturnPatient() {
        //given
        Integer id = 10;
        String newEmail = "anna.stark@gmail.com";
        Patient existingPatient = new Patient(id, "Jon Snow", "111222333", "jon.snow@gmail.com", "Winterfell 11",
                LocalDate.of(1990, 5, 22), Gender.MALE);
        PatientRequest updatePatientRequest = new PatientRequest("Aria Stark", "111222335", newEmail, "Winterfell 12",
                LocalDate.of(1993, 6, 30), "FEMALE");
        Patient updatedPatient = new Patient(id,"Aria Stark", "111222335", newEmail, "Winterfell 12",
                LocalDate.of(1993, 6, 30), Gender.FEMALE);

        //when
        ArgumentCaptor<Patient> capturedPatient = ArgumentCaptor.forClass(Patient.class);
        when(patientRepository.findById(eq(id))).thenReturn(Optional.of(existingPatient));
        when(patientRepository.existsByEmail(eq(newEmail))).thenReturn(false);
        doAnswer(invocation -> {
            PatientRequest patientRequest = invocation.getArgument(0);
            Patient patientToMap = invocation.getArgument(1);

            patientToMap.setName(patientRequest.getName());
            patientToMap.setPhoneNumber(patientRequest.getPhoneNumber());
            patientToMap.setEmail(patientRequest.getEmail());
            patientToMap.setAddress(patientRequest.getAddress());
            patientToMap.setDateOfBirth(patientRequest.getDateOfBirth());
            patientToMap.setGender(Gender.valueOf(patientRequest.getGender()));

            return null;
        }).when(modelMapper).map(eq(updatePatientRequest), eq(existingPatient));
        when(patientRepository.save(capturedPatient.capture())).thenReturn(updatedPatient);
        Patient resultPatient = underTest.updatePatient(id, updatePatientRequest);

        //then
        assertThat(resultPatient).isEqualTo(updatedPatient);
        assertThat(capturedPatient.getValue()).isEqualTo(updatedPatient);
    }

    @Test
    void testDeletePatient_shouldThrowResourceNotFound() {
        //given
        Integer id = 10;

        //when
        when(patientRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.deletePatient(id))
                .withMessage("Patient not found with patient id: " + id);
    }

    @Test
    void testDeletePatient_shouldDeletePatient() {
        //given
        Integer id = 10;
        Patient existingPatient = new Patient(id, "Jon Snow", "111222333", "jon.snow@gmail.com", "Winterfell 11",
                LocalDate.of(1990, 5, 22), Gender.MALE);

        //when
        when(patientRepository.findById(eq(id))).thenReturn(Optional.of(existingPatient));
        underTest.deletePatient(id);

        //then
        verify(patientRepository).delete(eq(existingPatient));
    }

}