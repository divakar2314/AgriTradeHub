package com.MyProject.AgritradeHub.Controller;

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

import com.MyProject.AgritradeHub.Model.Users1;
import com.MyProject.AgritradeHub.Repository.UserRepository;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Farmer")
public class FarmerController {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;



	@GetMapping("/FarmerDashboard")
	public String ShowFarmerDashboard(Model model) {

		if (session.getAttribute("loggedInFarmer") == null) {
			return "redirect:/FarmerLogin";

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
		return "Farmer/FarmerDashboard";
	}

	// Serve profile picture directly from DB
		@GetMapping("/profilePic/{id}")
		public ResponseEntity<byte[]> getProfilePic(@PathVariable Long id) {
			Users1 farmer = userRepo.findById(id).orElse(null);
			if (farmer != null && farmer.getProfilePic() != null) {
				return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(farmer.getProfilePic());
			}
			return ResponseEntity.notFound().build();
		}

		// Upload profile picture(AJAX)

		@PostMapping("/uploadProfilePic")
		@ResponseBody
		public String uploadProfilePic(@RequestParam("file") MultipartFile file, HttpSession session) {
			try {
				Users1 farmer = (Users1) session.getAttribute("loggedInFarmer");
				if (farmer == null) {
					return "Error";
				}

				// Save file bytes to DB
				farmer.setProfilePic(file.getBytes());
				userRepo.save(farmer);

				return "Success";
			} catch (Exception e) {
				return "Error";
			}
		}

		// Edit Profile

	@GetMapping("/EditProfile")
	public String ShowEditProfile(Model model) {

		if (session.getAttribute("loggedInFarmer") == null) {
			return "redirect:/FarmerLogin";

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
		return "Farmer/EditProfile";
	}

	// Remove Profile
	@PostMapping("/removeProfilePic")
	public String removeProfilePic(HttpSession session, RedirectAttributes attributes) {
		Users1 farmer = (Users1) session.getAttribute("loggedInFarmer");

		if (farmer != null) {
			// Set profilePic to null in DB
			farmer.setProfilePic(null);
			userRepo.save(farmer); // update DB

			// Update session so UI updates immediately
			session.setAttribute("loggedInAdmin", farmer);
		}
		attributes.addFlashAttribute("msg", "Successfully updated");
		return "redirect:/Farmer/FarmerDashboard"; // or wherever your profile page is
	}
	
	// change Password
	
	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		
		if(session.getAttribute("loggedInFarmer")==null) {
			return "redirect:/FarmerLogin";
			
		}
		
		return "Farmer/ChangePassword";
	}
	
    
	@PostMapping("/ChangePassword")
	public String changePassword(HttpServletRequest request,RedirectAttributes attributes) {
		try {
			
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass =request.getParameter("confirmPassword");
			
			if(!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same");
				return "redirect:/Farmer/ChangePassword";
				
			}
			
			Users1 farmer = (Users1) session.getAttribute("loggedInFarmer");
			
			if(oldPass.equals(farmer.getPassword())) {
				farmer.setPassword(confirmPass);
				userRepo.save(farmer);
				session.removeAttribute("loggedInFarmer");
				attributes.addFlashAttribute("msg", "Password succesfully Change");
				return "redirect:/FarmerLogin";
				
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
				
				
			}
			
			return "redirect:/Farmer/ChangePassword";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
			return "redirect:/Farmer/ChangePassword";
		}
		
		
	}
	
	@GetMapping("/logout")
	public String Logout(RedirectAttributes attributes) {
		
		session.removeAttribute("loggedInFarmer");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/FarmerLogin";
	
		
	}
	
	
	

}
