package com.das.appointments.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentTimeNotAvailable extends RuntimeException {
    Date date;
    Time time;

    public AppointmentTimeNotAvailable(LocalDateTime dateTime) {
        super("Requested appointment time (" + dateTime + ") is not available");
        this.date = date;
        this.time = time;
    }
}
