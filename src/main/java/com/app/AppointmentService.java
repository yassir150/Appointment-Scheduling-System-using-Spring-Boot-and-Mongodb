package com.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepo repo;

	@Autowired
	private UserRepo userRepo;


	/**
	 * Creates a new user account.
	 * @param user The user object containing user details.
	 * @return true if the user account is successfully created, false otherwise.
	 */
	public boolean createUserAccount(User user) {
		if (userRepo.save(user) != null) {
			return true;
		} else
			return false;
	}


	/**
	 * Finds a user by their email.
	 * @param email The email of the user to find.
	 * @return true if the user is found.
	 */
	public boolean findByUserEmail(String email) {
        return userRepo.findByEmail(email) != null;
	}













	/**
	 * Authenticates a user based on email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return User.
	 */
	public User userLogin(String email, String password) {
		return userRepo.findByEmailAndPassword(email, password);
	}



	/**
	 * Saves an appointment.
	 * @param appointment The appointment object to save.
	 * @return true if the appointment is successfully saved.
	 */
	public boolean saveData(Appointment appointment) {
		repo.save(appointment);
		return true;
	}









	/**
	 * Retrieves all appointments.
	 * @return A list of all appointments.
	 */
	public List<Appointment> findAllAppointments() {
		return repo.findAll();
	}












	/**
	 * Finds appointments by user ID.
	 * @param userId The ID of the user.
	 * @return A list of appointments for the specified user.
	 */
	public List<Appointment> findAppointmentsByPatientId(String userId) {
		return repo.findByPatientId(userId);
	}

	/**
	 * Finds appointments by user ID.
	 * @param appointmentId The ID of the user.
	 * @return A list of appointments for the specified user.
	 */
	public Appointment findAppointmentById(String appointmentId) {
		return repo.findAppointmentById(appointmentId);
	}


	public boolean deleteAppointment(String id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}


	public boolean updateAppointmentStatus(String id, String status) {
		Appointment appointment = repo.findById(id).orElse(null);
		if (appointment != null) {
			appointment.setStatus(status);
			repo.save(appointment);
			return true;
		}
		return false;
	}



	public List<User> findAllPatients() {
		return userRepo.findByUserType("patient");
	}

	public Map<String, Long> getAppointmentCountsForPatients() {
		List<User> patients = findAllPatients();
		Map<String, Long> appointmentCounts = new HashMap<>();
		for (User patient : patients) {
			long count = repo.countByPatientId(patient.getId());
			appointmentCounts.put(patient.getId(), count);
		}
		return appointmentCounts;
	}







	public boolean updateAppointment(Appointment appointment) {
		if (repo.existsById(appointment.getId())) {
			repo.save(appointment);
			return true;
		} else {
			return false;
		}
	}

	public boolean deletePatient(String id) {
		try {
			userRepo.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * Generates a random string for appointment ID.
	 * @return A random string prefixed with "RQ" followed by 5 random digits.
	 */
	public static String generateRandomString() {
		final String FIXED_PART = "RQ";
		final int RANDOM_PART_LENGTH = 5;
		StringBuilder sb = new StringBuilder(FIXED_PART);
		Random random = new Random();

		for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
			int digit = random.nextInt(10);
			sb.append(digit);
		}

		return sb.toString();
	}
}
