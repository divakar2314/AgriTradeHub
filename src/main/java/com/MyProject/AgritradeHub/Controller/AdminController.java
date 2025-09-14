package com.MyProject.AgritradeHub.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MyProject.AgritradeHub.Model.Enquiry;
import com.MyProject.AgritradeHub.Model.Users1;
import com.MyProject.AgritradeHub.Model.Users1.UserRole;
import com.MyProject.AgritradeHub.Repository.EnquiryRepository;
import com.MyProject.AgritradeHub.Repository.UserRepository;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepo;

// Admin Dashboard	

	@GetMapping("/AdminDashboard")
	public String ShowAdminDashboard(Model model) {

		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/AdminLogin";

		}
		/*
		 * model.addAttribute("totalDocotors", UserRepo.countByRole(UserRole.DOCTOR));
		 * model.addAttribute("totalPatients", UserRepo.countByRole(UserRole.PATIENT));
		 * model.addAttribute("totalAppointments", appointmentRepo.count());
		 * model.addAttribute("totalPrescription", prescriptionRepo.count());
		 * model.addAttribute("totalEnquiry", enquiryRepository.count());
		 * model.addAttribute("AppAppointment",
		 * appointmentRepo.countByStatus(AppointmentStatus.APPROVED));
		 * model.addAttribute("CancleAppointment",
		 * appointmentRepo.countByStatus(AppointmentStatus.REJECTED));
		 */
		return "Admin/AdminDashboard";
	}
	
	@GetMapping("/verifiedmerchant")
	public String Showverifiedmerchant(Model model) {

		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/AdminLogin";

		}
		List<Users1> merchantList = userRepo.findAllByRole(UserRole.MERCHANT);
		model.addAttribute("merchantList", merchantList);
		
		return "Admin/verifiedmerchant";
	}
	
	// New Merchant 
	
	@GetMapping("/newmerchant")
	public String Shownewmerchant(Model model) {

	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/AdminLogin";
	    }

	    List<Users1> newmerchantList = userRepo.findAllByRoleAndStatus(UserRole.MERCHANT, "PENDING");
	    model.addAttribute("newmerchantList",newmerchantList);

	    return "Admin/newmerchant";
	}

	
	@GetMapping("/verifiedfarmer")
	public String Showverifiedfarmer(Model model) {

		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/AdminLogin";

		}
		List<Users1> farmerList = userRepo.findAllByRole(UserRole.FARMER);
		model.addAttribute("farmerList", farmerList);
		
		return "Admin/verifiedfarmer";
	}
	
	
	


	// Serve profile picture directly from DB
	@GetMapping("/profilePic/{id}")
	public ResponseEntity<byte[]> getProfilePic(@PathVariable Long id) {
		Users1 admin = userRepo.findById(id).orElse(null);
		if (admin != null && admin.getProfilePic() != null) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(admin.getProfilePic());
		}
		return ResponseEntity.notFound().build();
	}

	// Upload profile picture(AJAX)

	@PostMapping("/uploadProfilePic")
	@ResponseBody
	public String uploadProfilePic(@RequestParam("file") MultipartFile file, HttpSession session) {
		try {
			Users1 admin = (Users1) session.getAttribute("loggedInAdmin");
			if (admin == null) {
				return "Error";
			}

			// Save file bytes to DB
			admin.setProfilePic(file.getBytes());
			userRepo.save(admin);

			return "Success";
		} catch (Exception e) {
			return "Error";
		}
	}

	// Edit Profile

	@GetMapping("/EditProfile")
	public String ShowEditProfile(Model model) {

		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/AdminLogin";

		}
		/*
		 * model.addAttribute("totalDocotors", UserRepo.countByRole(UserRole.DOCTOR));
		 * model.addAttribute("totalPatients", UserRepo.countByRole(UserRole.PATIENT));
		 * model.addAttribute("totalAppointments", appointmentRepo.count());
		 * model.addAttribute("totalPrescription", prescriptionRepo.count());
		 * model.addAttribute("totalEnquiry", enquiryRepository.count());
		 * model.addAttribute("AppAppointment",
		 * appointmentRepo.countByStatus(AppointmentStatus.APPROVED));
		 * model.addAttribute("CancleAppointment",
		 * appointmentRepo.countByStatus(AppointmentStatus.REJECTED));
		 */
		return "Admin/EditProfile";
	}

	// Remove Profile
	@PostMapping("/removeProfilePic")
	public String removeProfilePic(HttpSession session, RedirectAttributes attributes) {
		Users1 admin = (Users1) session.getAttribute("loggedInAdmin");

		if (admin != null) {
			// Set profilePic to null in DB
			admin.setProfilePic(null);
			userRepo.save(admin); // update DB

			// Update session so UI updates immediately
			session.setAttribute("loggedInAdmin", admin);
		}
		attributes.addFlashAttribute("msg", "Successfully updated");
		return "redirect:/Admin/AdminDashboard"; // or wherever your profile page is
	}
	
	// change Password
	
	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		
		return "Admin/ChangePassword";
	}
	
    
	@PostMapping("/ChangePassword")
	public String changePassword(HttpServletRequest request,RedirectAttributes attributes) {
		try {
			
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass =request.getParameter("confirmPassword");
			
			if(!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same");
				return "redirect:/Admin/ChangePassword";
				
			}
			
			Users1 admin = (Users1) session.getAttribute("loggedInAdmin");
			
			if(oldPass.equals(admin.getPassword())) {
				admin.setPassword(confirmPass);
				userRepo.save(admin);
				session.removeAttribute("loggedInAdmin");
				attributes.addFlashAttribute("msg", "Password succesfully Change");
				return "redirect:/AdminLogin";
				
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
				
				
			}
			
			return "redirect:/Admin/ChangePassword";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
			return "redirect:/Admin/ChangePassword";
		}
		
		
	}
	
	@GetMapping("/logout")
	public String Logout(RedirectAttributes attributes) {
		
		session.removeAttribute("loggedInAdmin");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/AdminLogin";
	
		
	}
	
	@GetMapping("/enquiry")
	public String ShowPatientEnquiry( Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
		}
		List<Enquiry> enquiryList= enquiryRepo.findAll();
		model.addAttribute("enquiryList", enquiryList);
		return "Admin/enquiry";
		
	}
	
	@GetMapping("/DeleteEnquiry")
	public String DeleteEnquiry(@RequestParam("id") long id) {
		enquiryRepo.deleteById(id);
		return "redirect:/Admin/enquiry";
		
	}
	
	
	
	
	
	

}
