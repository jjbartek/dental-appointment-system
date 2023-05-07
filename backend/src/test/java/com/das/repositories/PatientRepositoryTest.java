package com.das.repositories;

import com.das.entities.Gender;
import com.das.entities.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {
    @Autowired
    private PatientRepository underTest;

    @Test
    void patientByEmailCanBeFound() {
        //given
        String email = "charles.beatrix@gmail.com";
        Patient patient = new Patient(
                "Charles Beatrix", "222888111", email, "Ave Street 22",
                LocalDate.of(1980, 6, 10),
                Gender.MALE
        );
        underTest.save(patient);

        //when
        Boolean patientExists = underTest.existsByEmail(email);

        //should
        assertThat(patientExists).isTrue();
    }

    @Test
    void patientByEmailCanNotBeFound() {
        //given
        String email = "charles.beatrix@gmail.com";

        //when
        Boolean patientExists = underTest.existsByEmail(email);

        //should
        assertThat(patientExists).isFalse();
    }
}