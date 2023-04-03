package com.das.repositories;

import com.das.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query(value = "SELECT a FROM Appointment a WHERE a.startTime >= :dateTime OR a.endTime >= :dateTime")
    Page<Appointment> findByPostTime(@Param("dateTime") LocalDateTime dateTime, Pageable p);

    @Query(value = "SELECT a FROM Appointment a WHERE a.endTime <= :dateTime OR a.startTime = :dateTime")
    Page<Appointment> findByPreTime(@Param("dateTime") LocalDateTime dateTime, Pageable p);

    @Query(value = "SELECT a FROM Appointment a WHERE a.employee.id = :employeeId AND (a.startTime >= :dateTime OR a.endTime >= :dateTime)")
    Page<Appointment> findByPostTimeAndEmployeeId(@Param("dateTime") LocalDateTime dateTime, @Param("employeeId") Integer employeeId, Pageable p);

    @Query(value = "SELECT a FROM Appointment a WHERE a.employee.id = :employeeId AND (a.endTime <= :dateTime OR a.startTime = :dateTime)")
    Page<Appointment> findByPreTimeAndEmployeeId(@Param("dateTime") LocalDateTime dateTime, @Param("employeeId") Integer employeeId, Pageable p);

    @Query(value = "SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND (a.startTime >= :dateTime OR a.endTime >= :dateTime)")
    Page<Appointment> findByPostTimeAndPatientId(@Param("dateTime") LocalDateTime dateTime, @Param("patientId") Integer patientId, Pageable p);

    @Query(value = "SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND (a.endTime <= :dateTime OR a.startTime = :dateTime)")
    Page<Appointment> findByPreTimeAndPatientId(@Param("dateTime") LocalDateTime dateTime, @Param("patientId") Integer patientId, Pageable p);

    @Query(value = """
            SELECT a
            FROM Appointment a WHERE a.employee.id = :employeeId AND
            ((a.startTime <= :startTime AND a.endTime > :startTime) OR (a.startTime < :endTime AND a.endTime >= :endTime))""")
    List<Appointment> findByTimeBetweenAndEmployeeId(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime,
                                                        @Param("employeeId") Integer employeeId);
}
