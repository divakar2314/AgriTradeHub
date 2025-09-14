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
@RequestMapping("/Merchant")
public class MerchantController {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;



	@GetMapping("/MerchantDashboard")
	public String ShowMerchantDashboard(Model model) {

		if (session.getAttribute("loggedInMerchant") == null) {
			return "redirect:/MerchantLogin";

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
		return "Merchant/MerchantDashboard";
	}

	// Serve profile picture directly from DB
	@GetMapping("/profilePic/{id}")
	public ResponseEntity<byte[]> getProfilePic(@PathVariable Long id) {
		Users1 merchant = userRepo.findById(id).orElse(null);
		if (merchant != null && merchant.getProfilePic() != null) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(merchant.getProfilePic());
		}
		return ResponseEntity.notFound().build();
	}

	// Upload profile picture(AJAX)

	@PostMapping("/uploadProfilePic")
	@ResponseBody
	public String uploadProfilePic(@RequestParam("file") MultipartFile file, HttpSession session) {
		try {
			Users1 merchant = (Users1) session.getAttribute("loggedInMerchant");
			if (merchant == null) {
				return "Error";
			}

			// Save file bytes to DB
			merchant.setProfilePic(file.getBytes());
			userRepo.save(merchant);

			return "Success";
		} catch (Exception e) {
			return "Error";
		}
	}

	// Edit Profile

	@GetMapping("/EditProfile")
	public String ShowEditProfile(Model model) {

		if (session.getAttribute("loggedInMerchant") == null) {
			return "redirect:/MerchantLogin";

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
		return "Merchant/EditProfile";
	}

	// Remove Profile
	@PostMapping("/removeProfilePic")
	public String removeProfilePic(HttpSession session, RedirectAttributes attributes) {
		Users1 merchant = (Users1) session.getAttribute("loggedInMerchant");

		if (merchant != null) {
			// Set profilePic to null in DB
			merchant.setProfilePic(null);
			userRepo.save(merchant); // update DB

			// Update session so UI updates immediately
			session.setAttribute("loggedInAdmin", merchant);
		}
		attributes.addFlashAttribute("msg", "Successfully updated");
		return "redirect:/Merchant/MerchantDashboard"; // or wherever your profile page is
	}
	
	// change Password
	
	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		
		if(session.getAttribute("loggedInMerchant")==null) {
			return "redirect:/MerchantLogin";
			
		}
		
		return "Merchant/ChangePassword";
	}
	
    
	@PostMapping("/ChangePassword")
	public String changePassword(HttpServletRequest request,RedirectAttributes attributes) {
		try {
			
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass =request.getParameter("confirmPassword");
			
			if(!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same");
				return "redirect:/Merchant/ChangePassword";
				
			}
			
			Users1 merchant = (Users1) session.getAttribute("loggedInMerchant");
			
			if(oldPass.equals(merchant.getPassword())) {
				merchant.setPassword(confirmPass);
				userRepo.save(merchant);
				session.removeAttribute("loggedInMerchant");
				attributes.addFlashAttribute("msg", "Password succesfully Change");
				return "redirect:/MerchantLogin";
				
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
				
				
			}
			
			return "redirect:/Merchant/ChangePassword";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
			return "redirect:/Merchant/ChangePassword";
		}
		
		
	}
	
	@GetMapping("/logout")
	public String Logout(RedirectAttributes attributes) {
		
		session.removeAttribute("loggedInMerchant");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/MerchantLogin";
	
		
	}
	
	
	

}
