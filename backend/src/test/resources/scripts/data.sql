INSERT INTO users (id, email, username, password)
VALUES (1, 'Edward Smith', 'edward.smith@gmail.com', 'password');
INSERT INTO users_roles (user_id, userRole)
    VALUES (1, 'EMPLOYEE');

INSERT INTO users (id, email, username, password)
    VALUES (2, 'Jon Snow', 'jon.snow@gmail.com', 'password');
INSERT INTO users_roles (user_id, userRole)
    VALUES (2, 'EMPLOYEE');

INSERT INTO patients (id, address, date_of_birth, email, patient_name, phone_number, gender)
    VALUES (1, 'Winterfell Street 33, North', '1990-04-03', 'rob.stark@gmail.com', 'Rob Stark', '123456789', 'MALE');
INSERT INTO patients (id, address, date_of_birth, email, patient_name, phone_number, gender)
    VALUES (2, 'Winterfell Street 33, North', '1995-05-06', 'sansa.stark@gmail.com', 'Sansa Stark', '987654321', 'FEMALE');

INSERT INTO services (id, duration, min_price, service_name)
    VALUES (1, 60, 500, 'Teeth whitening');
INSERT INTO services (id, duration, min_price, service_name)
    VALUES (2, 60, 300, 'Tooth filling');
INSERT INTO services (id, duration, min_price, service_name)
    VALUES (3, 60, 1000, 'Root canal treatment');


INSERT INTO appointments (id, start_time, end_time, employee_id, patient_id, total, notes, a_status)
    VALUES (1, '2023-05-11 15:30', '2023-05-11 16:30', 2, 1, null, null, 'RESERVED');
INSERT INTO appointments (id, start_time, end_time, employee_id, patient_id, total, notes, a_status)
    VALUES (2, '2023-05-10 15:30', '2023-05-10 16:30', 2, 2, null, null, 'RESERVED');
INSERT INTO appointments (id, start_time, end_time, employee_id, patient_id, total, notes, a_status)
    VALUES (3, '2023-05-10 14:30', '2023-05-10 15:30', 1, 1, null, null, 'RESERVED');

INSERT INTO appointments_services (appointment_id, service_id) VALUES (1, 3);
INSERT INTO appointments_services (appointment_id, service_id) VALUES (2, 1);
INSERT INTO appointments_services (appointment_id, service_id) VALUES (2, 2);