package com.das.repositories;

import com.das.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findByDateAndTimeAndEmployeeId(Date date, Time time, Integer userId, Pageable p);
    Page<Appointment> findByDateAndTimeAndPatientId(Date date, Time time, Integer userId, Pageable p);
    Optional<Appointment> findFirstByDateAndTimeBeforeAndEmployeeIdOrderByTimeDesc(Date date, Time time, Integer id);
    Optional<Appointment> findFirstByDateAndTimeBetweenAndEmployeeIdOrderByTimeAsc(Date date, Time startTime, Time endTime, Integer userId);
    Page<Appointment> findByDateAndTime(Date date, Time time, Pageable p);
}
