package com.das.appointments;

import com.das.appointments.entities.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("classpath:scripts/data.sql")
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository underTest;

    @Test
    void postTimeAppointmentShouldNotBeFound() {
        //when
        List<Appointment> appointmentEndedList = underTest.findByPostTime(
                LocalDateTime.of(2023, 5, 11, 17, 30), null).toList();

        //then
        assertThat(appointmentEndedList).isEmpty();
    }

    @Test
    void postTimeAppointmentShouldBeFound() {
        //given
        List<Appointment> allAppointments = underTest.findAll()
                .stream()
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .collect(Collectors.toList());

        //when
        List<Appointment> whenFirstAppointmentStarts = underTest.findByPostTime(
                LocalDateTime.of(2023, 5, 10, 14, 30), null).toList();
        List<Appointment> duringFirstAppointment = underTest.findByPostTime(
                LocalDateTime.of(2023, 5, 10, 15, 0), null).toList();
        List<Appointment> whenFirstAppointmentEnds = underTest.findByPostTime(
                LocalDateTime.of(2023, 5, 10, 15, 30), null).toList();
        List<Appointment> duringSecondAppointment = underTest.findByPostTime(
                LocalDateTime.of(2023, 5, 10, 15, 40), null).toList();

        //then
        assertThat(whenFirstAppointmentStarts)
                .hasSize(3)
                .isEqualTo(allAppointments);
        assertThat(duringFirstAppointment)
                .hasSize(3)
                .isEqualTo(allAppointments);
        assertThat(whenFirstAppointmentEnds)
                .hasSize(3)
                .isEqualTo(allAppointments);
        assertThat(duringSecondAppointment)
                .hasSize(2)
                .isEqualTo(allAppointments.stream()
                    .skip(1)
                    .toList());
    }

    @Test
    void preTimeAppointmentShouldNotBeFound() {
        //when
        List<Appointment> appointmentNotStartedList = underTest.findByPreTime(
                LocalDateTime.of(2023, 5, 9, 15, 0), null).toList();

        //then
        assertThat(appointmentNotStartedList).isEmpty();
    }

    @Test
    void preTimeAppointmentsShouldBeFound() {
        //given
        List<Appointment> allAppointments = underTest.findAll()
                .stream()
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .collect(Collectors.toList());

        //when
        List<Appointment> whenLastAppointmentEnds = underTest.findByPreTime(
                LocalDateTime.of(2023, 5, 11, 16, 30), null).toList();
        List<Appointment> duringFirstAppointment = underTest.findByPreTime(
                LocalDateTime.of(2023, 5, 10, 15, 0), null).toList();
        List<Appointment> whenSecondAppointmentStarts = underTest.findByPreTime(
                LocalDateTime.of(2023, 5, 10, 15, 30), null).toList();
        List<Appointment> duringSecondAppointment = underTest.findByPreTime(
                LocalDateTime.of(2023, 5, 10, 15, 40), null).toList();

        //then
        assertThat(whenLastAppointmentEnds)
                .hasSize(3)
                .isEqualTo(allAppointments);
        assertThat(duringFirstAppointment)
                .hasSize(1)
                .isEqualTo(allAppointments.stream()
                        .limit(1)
                        .toList());
        assertThat(whenSecondAppointmentStarts)
                .hasSize(2)
                .isEqualTo(allAppointments.stream()
                        .limit(2)
                        .toList());
        assertThat(duringSecondAppointment)
                .hasSize(2)
                .isEqualTo(allAppointments.stream()
                        .limit(2)
                        .toList());

    }


    @Test
    void postTimeAppointmentByEmployeeShouldNotBeFound() {
        //when
        List<Appointment> afterAppointmentOfFirstEmployee = underTest.findByPostTimeAndEmployeeId(
                LocalDateTime.of(2023, 5, 10, 15, 31), 1, null).toList();

        //then
        assertThat(afterAppointmentOfFirstEmployee).isEmpty();
    }

    @Test
    void postTimeAppointmentByEmployeeShouldBeFound() {
        //when
        List<Appointment> whenAppointmentOfFirstEmployeeStarts = underTest.findByPostTimeAndEmployeeId(
                LocalDateTime.of(2023, 5, 10, 14, 30), 1, null).toList();
        List<Appointment> beforeAppointmentOfSecondEmployee = underTest.findByPostTimeAndEmployeeId(
                LocalDateTime.of(2023, 5, 10, 14, 30), 2, null).toList();


        //then
        assertThat(whenAppointmentOfFirstEmployeeStarts).hasSize(1);
        assertThat(whenAppointmentOfFirstEmployeeStarts.get(0).getEmployee().getId()).isEqualTo(1);
        assertThat(beforeAppointmentOfSecondEmployee).hasSize(2);
        assertThat(beforeAppointmentOfSecondEmployee.get(0).getEmployee().getId()).isEqualTo(2);
        assertThat(beforeAppointmentOfSecondEmployee.get(1).getEmployee().getId()).isEqualTo(2);
    }


    @Test
    void preTimeAppointmentByEmployeeShouldNotBeFound() {
        //when
        List<Appointment> beforeAppointmentOfSecondEmployee = underTest.findByPreTimeAndEmployeeId(
                LocalDateTime.of(2023, 5, 10, 15, 29), 2, null).toList();

        //then
        assertThat(beforeAppointmentOfSecondEmployee).isEmpty();
    }


    @Test
    void preTimeAppointmentByEmployeeShouldBeFound() {
        //when
        List<Appointment> afterAppointmentOfFirstEmployee = underTest.findByPreTimeAndEmployeeId(
                LocalDateTime.of(2023, 5, 10, 16, 30), 1, null).toList();
        List<Appointment> beforeLastAppointmentOfSecondEmployee = underTest.findByPreTimeAndEmployeeId(
                LocalDateTime.of(2023, 5, 11, 14, 30), 2, null).toList();


        //then
        assertThat(afterAppointmentOfFirstEmployee).hasSize(1);
        assertThat(afterAppointmentOfFirstEmployee.get(0).getEmployee().getId()).isEqualTo(1);
        assertThat(beforeLastAppointmentOfSecondEmployee).hasSize(1);
        assertThat(beforeLastAppointmentOfSecondEmployee.get(0).getEmployee().getId()).isEqualTo(2);
    }

    @Test
    void postTimeAppointmentByPatientShouldNotBeFound() {
        //when
        List<Appointment> afterAppointmentOfSecondPatient = underTest.findByPostTimeAndPatientId(
                LocalDateTime.of(2023, 5, 10, 17, 30), 2, null).toList();

        //then
        assertThat(afterAppointmentOfSecondPatient).isEmpty();
    }

    @Test
    void postTimeAppointmentByPatientShouldBeFound() {
        //when
        List<Appointment> whenAppointmentOfSecondPatientStarts = underTest.findByPostTimeAndPatientId(
                LocalDateTime.of(2023, 5, 10, 15, 30), 2, null).toList();
        List<Appointment> beforeAppointmentsOfFirstPatient = underTest.findByPostTimeAndPatientId(
                LocalDateTime.of(2023, 5, 10, 14, 30), 1, null).toList();


        //then
        assertThat(whenAppointmentOfSecondPatientStarts).hasSize(1);
        assertThat(whenAppointmentOfSecondPatientStarts.get(0).getPatient().getId()).isEqualTo(2);
        assertThat(beforeAppointmentsOfFirstPatient).hasSize(2);
        assertThat(beforeAppointmentsOfFirstPatient.get(0).getPatient().getId()).isEqualTo(1);
        assertThat(beforeAppointmentsOfFirstPatient.get(1).getPatient().getId()).isEqualTo(1);
    }


    @Test
    void preTimeAppointmentByPatientShouldNotBeFound() {
        //when
        List<Appointment> beforeAppointmentOfSecondPatient = underTest.findByPreTimeAndPatientId(
                LocalDateTime.of(2023, 5, 10, 15, 29), 2, null).toList();

        //then
        assertThat(beforeAppointmentOfSecondPatient).isEmpty();
    }


    @Test
    void preTimeAppointmentByPatientShouldBeFound() {
        //when
        List<Appointment> afterFirstAppointmentOfFirstPatient = underTest.findByPreTimeAndPatientId(
                LocalDateTime.of(2023, 5, 10, 16, 30), 1, null).toList();
        List<Appointment> afterAppointmentOfSecondPatient = underTest.findByPreTimeAndPatientId(
                LocalDateTime.of(2023, 5, 10, 16, 31), 2, null).toList();


        //then
        assertThat(afterFirstAppointmentOfFirstPatient).hasSize(1);
        assertThat(afterFirstAppointmentOfFirstPatient.get(0).getPatient().getId()).isEqualTo(1);
        assertThat(afterAppointmentOfSecondPatient).hasSize(1);
        assertThat(afterAppointmentOfSecondPatient.get(0).getPatient().getId()).isEqualTo(2);
    }
}