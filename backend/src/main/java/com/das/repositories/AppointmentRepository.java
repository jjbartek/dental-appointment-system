package com.das.repositories;

import com.das.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.sql.Time;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findByDateAndTimeAndEmployeeId(Date date, Time time, Integer userId, Pageable p);
}
