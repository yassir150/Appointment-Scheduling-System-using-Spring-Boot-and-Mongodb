package com.app;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String>{

	long countByStatus(String status);

	List<Appointment> getAllByAppointmentDateIsNear();

	Appointment findAppointmentById(String id);

	List<Appointment> findByPatientId(String patientId);


	long countByPatientId(String doctorId);
}
