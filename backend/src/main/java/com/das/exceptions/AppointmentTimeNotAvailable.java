package com.das.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class AppointmentTimeNotAvailable extends RuntimeException {
    Date date;
    Time time;

    public AppointmentTimeNotAvailable(Date date, Time time) {
        super("Requested appointment time (" + date.toLocalDate() + " " + time.toLocalTime() + ") has already been taken");
        this.date = date;
        this.time = time;
    }
}
