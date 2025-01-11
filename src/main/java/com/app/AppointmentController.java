package com.app;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	/**
	 * Handles GET requests for the appointment page.
	 * @param model The model to pass attributes to the view.
	 * @return The name of the appointment view.
	 */
	@GetMapping("/appointment")
	public String getAppointmentPage(Model model) {
		return "appointment";
	}

	/**
	 * Handles GET requests for the dashboard page.
	 * @param model The model to pass attributes to the view.
	 * @return The name of the dashboard view.
	 */
	@GetMapping("/dashboard")
	public String getDashboardPage(Model model, HttpSession session) {
		String authResult = isAuthenticated(session);
		if ("redirect:/".equals(authResult) ) {
			// If not authenticated, redirect immediately
			return authResult;
		}

		if (session.getAttribute("userType").equals("patient")) {
			// If authenticated as admin, show dashboard
			return "redirect:/";
		}
		List<Appointment> appointments = service.findAllAppointments();
		model.addAttribute("appointments", appointments);
		model.addAttribute("totalAppointments", repo.count());
		model.addAttribute("totalBooked", repo.countByStatus("booked"));
		model.addAttribute("totalPending", repo.countByStatus("pending"));
		return "dashboard";
	}

	/**
	 * Handles GET requests for the user registration page.
	 * @param model The model to pass attributes to the view.
	 * @param user The user model attribute.
	 * @return The name of the register view.
	 */
	@GetMapping("/user/create")
	public String getUserCreate(Model model, @ModelAttribute User user) {
		return "register";
	}

	/**
	 * Handles POST requests for user registration.
	 * @param model The model to pass attributes to the view.
	 * @param user The user model attribute.
	 * @return The name of the register view.
	 */
	@PostMapping("/user/create")
	public String getPostUser(Model model, @ModelAttribute User user) {
		boolean result = service.findByUserEmail(user.getEmail());
		if (result) {
			model.addAttribute("exist", "This " + user.getEmail() + " Already Exist");
		} else if (service.createUserAccount(user)) {
			model.addAttribute("success", "Your User Account created");
		} else {
			model.addAttribute("error", "Your User Account creationed Failed");
		}
		return "register";
	}




	/**
	 * Handles GET requests for the user login page.
	 * @return The name of the user login view.
	 */
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}


	/**
	 * Handles POST requests for user login.
	 * @param model The model to pass attributes to the view.
	 * @param user The user model attribute.
	 * @return A redirection to the appointment page if login is successful, otherwise the user login view.
	 */
	@PostMapping("/login")
	public String postLogin(Model model, @ModelAttribute User user , HttpSession session) {
		User data = service.userLogin(user.getEmail(), user.getPassword());
		boolean loginstatus = data != null;
		if (loginstatus) {
			session.setAttribute("id", data.getId());
			session.setAttribute("userType", "patient");
			return "redirect:/appointment";
		} else {
			model.addAttribute("error", "Login Failed");
			return "login";
		}
	}





	/**
	 * Handles GET requests for the user login page.
	 * @return The name of the user login view.
	 */
	@GetMapping("/user/login")
	public String getUserLogin() {
		return "userlogin";
	}

	/**
	 * Handles POST requests for user login.
	 * @param model The model to pass attributes to the view.
	 * @param user The user model attribute.
	 * @return A redirection to the appointment page if login is successful, otherwise the user login view.
	 */
	@PostMapping("/user/login")
	public String postUserLogin(Model model, @ModelAttribute User user , HttpSession session) {
		User data = service.userLogin(user.getEmail(), user.getPassword());
		boolean loginstatus = data != null;
		if (loginstatus) {
			session.setAttribute("id", data.getId());
			session.setAttribute("userType", "patient");
			return "redirect:/appointment";
		} else {
			model.addAttribute("error", "Login Failed");
			return "userlogin";
		}
	}

	/**
	 * Handles GET requests for the admin login page.
	 * @param model The model to pass attributes to the view.
	 * @return The name of the admin login view.
	 */
	@GetMapping("/admin/login")
	public String getAdminLogin(Model model) {
		return "adminlogin";
	}

	/**
	 * Handles POST requests for admin login.
	 * @param model The model to pass attributes to the view.
	 * @param admin The admin model attribute.
	 * @return A redirection to the dashboard page if login is successful, otherwise the admin login view.
	 */
	@PostMapping("/admin/login")
	public String postAdminLogin(Model model, @ModelAttribute Admin admin, HttpSession session) {
		Admin data = service.adminLogin(admin.getEmail(), admin.getPassword());
		boolean loginstatus = data != null;
		System.out.print(service.adminLogin(admin.getEmail(), admin.getPassword()));
		if (loginstatus) {
			session.setAttribute("id", data.getId());
			session.setAttribute("userType", "admin");
			return "redirect:/dashboard";
		} else {
			model.addAttribute("error", "Login Failed");
			return "adminlogin";
		}
	}

	/**
	 * Handles POST requests for creating an appointment.
	 * @param model The model to pass attributes to the view.
	 * @param appointment The appointment model attribute.
	 * @return The name of the appointment view with success or error message.
	 */
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
		if (service.saveData(appointment)) {
			model.addAttribute("success", "Your Appointment Request ID : " + appointment.getAppointmentId());
		} else {
			model.addAttribute("error", "Unable Book your Appointment due to Error");
		}
		return "appointment";
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
		System.out.print(session.getAttribute("id"));
		return null;
	}

}
