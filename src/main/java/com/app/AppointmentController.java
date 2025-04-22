package com.app;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppointmentController {

	@Autowired
	private AppointmentService service;

	@Autowired
	private AppointmentRepo repo;

	/**
	 * Handles GET requests for the home page.
	 * @param model The model to pass attributes to the view.
	 * @return The name of the home view.
	 */
	@GetMapping("/")
	public String getIndexPage(Model model) {
		return "home";
	}



	@GetMapping("/appointment")
	public String getAppointmentPage(Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}
		model.addAttribute("patientName", session.getAttribute("username"));
		return "appointment";
	}




	@GetMapping("/user/create")
	public String getUserCreate(Model model, @ModelAttribute User user) {
		return "register";
	}




	@PostMapping("/user/create")
	public String getPostUser(Model model, @ModelAttribute User user) {
		boolean result = service.findByUserEmail(user.getEmail());
		user.setUserType("patient");
		if (result) {
			model.addAttribute("exist", "This " + user.getEmail() + " Already Exist");
			return "redirect:/user/create";
		} else if (service.createUserAccount(user)) {
			model.addAttribute("success", "Your User Account created");
			return "redirect:/login";
		} else {
			model.addAttribute("error", "Your User Account creationed Failed");
			return "redirect:/user/create";
		}

	}




	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}



	@PostMapping("/login")
	public String postLogin(Model model, @ModelAttribute User user , HttpSession session) {
		User data = service.userLogin(user.getEmail(), user.getPassword());
		boolean loginstatus = data != null;
		if (loginstatus) {
			if (data.getUserType().equals("doctor")) {
				session.setAttribute("id", data.getId());
				session.setAttribute("userType", "doctor");
				session.setAttribute("username", data.getUsername());
				return "redirect:/doctordashboard";
			} else {
				session.setAttribute("id", data.getId());
				session.setAttribute("userType", "patient");
				session.setAttribute("username", data.getUsername());
				return "redirect:/patientdashboard";
			}
		} else {
			model.addAttribute("error", "Login Failed");
			return "login";
		}
	}





	@GetMapping("/patientdashboard")
	public String getUSerDashboardPage(Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult) ) {
			return authResult;
		}

		if (session.getAttribute("userType").equals("doctor")) {
			// If authenticated as admin, show dashboard
			return "redirect:/";
		}

		List<Appointment> appointments = service.findAppointmentsByPatientId(session.getAttribute("id").toString());
		model.addAttribute("patientName", "Hello "+session.getAttribute("username"));
		model.addAttribute("appointments", appointments);
		return "patientdashboard";
	}




	@PostMapping("/appointment")
	public String postIndexPage(Model model, @ModelAttribute Appointment appointment, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult) ) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (session.getAttribute("userType").equals("admin")) {
			// If authenticated as admin, show dashboard
			return "redirect:/";
		}

		appointment.setPatientId(session.getAttribute("id").toString());

		if (service.saveData(appointment)) {
			model.addAttribute("success", "Your Appointment is Saved");
		} else {
			model.addAttribute("error", "Unable Book your Appointment due to Error");
		}
		return "redirect:/patientdashboard";
	}




	@GetMapping("/appointment/edit/{id}")
	public String showEditForm(@PathVariable("id") String id, Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		Appointment appointment = service.findAppointmentById(id);
		if (appointment == null) {
			// Handle the case where the appointment is not found
			model.addAttribute("error", "Appointment not found");
			return "redirect:/patientdashboard";
		}

		model.addAttribute("appointment", appointment);
		return "editappointment";
	}






	@PostMapping("/appointment/edit")
	public String updateAppointment(@ModelAttribute Appointment appointment, Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		appointment.setPatientId(session.getAttribute("id").toString());

		if (service.updateAppointment(appointment)) {
			model.addAttribute("success", "Appointment updated successfully");
		} else {
			model.addAttribute("error", "Unable to update appointment due to an error");
		}

		return "redirect:/patientdashboard";
	}




	@GetMapping("/appointment/delete/{id}")
	public String deleteAppointment(@PathVariable("id") String id, Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (service.deleteAppointment(id)) {
			model.addAttribute("success", "Appointment deleted successfully");
		} else {
			model.addAttribute("error", "Unable to delete appointment due to an error");
		}

		return "redirect:/patientdashboard";
	}





	
	@GetMapping("/doctordashboard")
	public String getDoctorDashboardPage(Model model, HttpSession session) {

		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (!session.getAttribute("userType").equals("doctor")) {
			// If not authenticated as doctor, redirect to home
			return "redirect:/";
		}

		List<Appointment> appointments = service.findAllAppointments();
		model.addAttribute("appointments", appointments);
		model.addAttribute("drName", "Hello "+session.getAttribute("username"));
		return "doctordashboard";
	}




	@GetMapping("/patients")
	public String getAllPatients(Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (!session.getAttribute("userType").equals("doctor")) {
			// If not authenticated as doctor, redirect to home
			return "redirect:/";
		}

		List<User> patients = service.findAllPatients();
		Map<String, Long> appointmentCounts = service.getAppointmentCountsForPatients();

		model.addAttribute("patients", patients);
		model.addAttribute("appointmentCounts", appointmentCounts);
		return "patients";
	}



	@GetMapping("/bypatient/{id}")
	public String searchAppointmentsByPatientName(@PathVariable("id") String id, Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		List<Appointment> appointments = service.findAppointmentsByPatientId(id);
		model.addAttribute("patientName", appointments.getFirst().getUserName());
		model.addAttribute("appointments", appointments);
		return "bypatient";
	}






	@GetMapping("/appointment/approve/{id}")
	public String approveAppointment(@PathVariable("id") String id, Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (service.updateAppointmentStatus(id, "approved")) {
			model.addAttribute("success", "Appointment approved successfully");
		} else {
			model.addAttribute("error", "Unable to approve appointment due to an error");
		}

		return "redirect:/doctordashboard";
	}




	// Handles GET requests to reject an appointment
	@GetMapping("/appointment/reject/{id}")
	public String rejectAppointment(@PathVariable("id") String id, Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult)) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (service.updateAppointmentStatus(id, "rejected")) {
			model.addAttribute("success", "Appointment rejected successfully");
		} else {
			model.addAttribute("error", "Unable to reject appointment due to an error");
		}

		return "redirect:/doctordashboard";
	}





	/**
	 * Handles GET requests for logging out the user.
	 * @param session The HTTP session.
	 * @return A redirection to the home page.
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}



	/**
	 * Checks if the user is authenticated using session data.
	 * @param session The HTTP session.
	 * @return A redirection to the appointment page if the user is not authenticated.
	 */
	@GetMapping("/check")
	public String isAuthenticated(HttpSession session) {
		System.out.print("this is in check "+session.getAttribute("id")+"\n");
		if (session.getAttribute("id") == null) {
			return "redirect:/";
		}
		return null;
	}

}
