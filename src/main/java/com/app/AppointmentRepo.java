package com.app;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String>{

	long countByStatus(String status);

	Appointment findByAppointmentId(String appointmentId);

}
