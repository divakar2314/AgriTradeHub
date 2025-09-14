package com.MyProject.AgritradeHub.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MyProject.AgritradeHub.Model.Enquiry;
import com.MyProject.AgritradeHub.Model.Users1;
import com.MyProject.AgritradeHub.Model.Users1.UserRole;
import com.MyProject.AgritradeHub.Model.Users1.UserStatus;
import com.MyProject.AgritradeHub.Repository.EnquiryRepository;
import com.MyProject.AgritradeHub.Repository.UserRepository;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepo;
	
	
	@GetMapping("/")
	public String ShowIndex() {
		return "index";
	}
	
	@GetMapping("/AboutUs")
	public String ShowAboutUs() {
		return "AboutUs.html";
	}
	
	@GetMapping("/Services")
	public String ShowServices() {
		return "Services";
	}
	
	
	
	@GetMapping("/Registration")
	public String ShowRegistration(Model model) {
		Users1 userDto = new Users1();
		model.addAttribute("userDto", userDto);
		return "Registration";
	}
	
	@PostMapping("/Registration")
	public String Registration(@ModelAttribute("userDto") Users1 newUser, RedirectAttributes attributes) {
		try {
			newUser.setRole(UserRole.FARMER);
			newUser.setStatus(UserStatus.PENDING);
			newUser.setRegDate(LocalDateTime.now());
			userRepo.save(newUser);
			attributes.addFlashAttribute("msg", "Registration Successful!");
			return "redirect:/Registration";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " +e.getMessage());
			return "redirect:/Registration";
		}
	}
	
	@GetMapping("/FarmerLogin")
	public String ShowFarmerLogin() {
		return "FarmerLogin";
	}
	
	// Farmer Login Postmapping
	
	@PostMapping("/FarmerLogin")
	public String FarmerLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "User doesn't Exists");
				return "redirect:/FarmerLogin";

			}
			Users1 farmer = userRepo.findByEmail(email);

			if (password.equals(farmer.getPassword()) && farmer.getRole().equals(UserRole.FARMER)) {
				if (farmer.getStatus().equals(UserStatus.PENDING)) {
					attributes.addFlashAttribute("msg", "Registration Pending, Wait for Admin Approval");
				} else if (farmer.getStatus().equals(UserStatus.DISABLED)) {
					attributes.addFlashAttribute("msg", "Login Disabled 🚫, Please contact Adminstration");

				} else {
					
					  
					
					 session.setAttribute("loggedInFarmer", farmer); 
					 return "redirect:/Farmer/FarmerDashboard";
					 
					 
				}

			}
			
			
			else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
				return "redirect:/FarmerLogin";
			}

			return "redirect:/FarmerLogin";

		} catch (Exception e) {
			return "redirect:/FarmerLogin";
		}
	}
	
	
	
	@GetMapping("/MerchantLogin")
	public String ShowMerchantLogin() {
		return "MerchantLogin";
	}
	
	// MerchantLogin Postmapping
	
	@PostMapping("/MerchantLogin")
	public String MerchantLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "User doesn't Exists");
				return "redirect:/MerchantLogin";

			}
			Users1 merchant = userRepo.findByEmail(email);

			if (password.equals(merchant.getPassword()) && merchant.getRole().equals(UserRole.MERCHANT)) {
				if (merchant.getStatus().equals(UserStatus.PENDING)) {
					attributes.addFlashAttribute("msg", "Registration Pending, Wait for Admin Approval");
				} else if (merchant.getStatus().equals(UserStatus.DISABLED)) {
					attributes.addFlashAttribute("msg", "Login Disabled 🚫, Please contact Adminstration");

				} else {
					
					  
					
					session.setAttribute("loggedInMerchant", merchant);
					 return "redirect:/Merchant/MerchantDashboard";
					 
				}

			}
			
			
			else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
				return "redirect:/MerchantLogin";
			}

			return "redirect:/MerchantLogin";

		} catch (Exception e) {
			return "redirect:/MerchantLogin";
		}
	}
	
	
	
	//AdminLogin Getmapping
	
	@GetMapping("/AdminLogin")
	public String showAdminLogin() {
		return "AdminLogin";
	}
	
	// AdminLogin Postmapping 
	
	@PostMapping("/AdminLogin")
	public String AdminLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {

		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "Admin doesn't Exists");
				return "redirect:/AdminLogin";
			}
			Users1 admin = userRepo.findByEmail(email);
			if (password.equals(admin.getPassword()) && admin.getRole().equals(UserRole.ADMIN)) {
			  
				
				 session.setAttribute("loggedInAdmin", admin); 
				 return "redirect:/Admin/AdminDashboard";
				 
				
				//attributes.addFlashAttribute("msg", "Valid users login successfully");
			  
			

			} else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
			}
			
			return "redirect:/AdminLogin";

			

		} catch (Exception e) {
			return "redirect:/AdminLogin";
		}

	}
	
	
	
	
	
	
	
	// Merchant Registration 
	
	@GetMapping("/MerchantRegistration")
	public String ShowMerchantRegistration(Model model) {
		Users1 userDto = new Users1();
		model.addAttribute("userDto", userDto);
		return "MerchantRegistration";
	}
	
	
	@PostMapping("/MerchantRegistration")
	public String MerchantRegistration(@ModelAttribute("userDto") Users1 newUser, RedirectAttributes attributes) {
		try {
			newUser.setRole(UserRole.MERCHANT);
			newUser.setStatus(UserStatus.PENDING);
			newUser.setRegDate(LocalDateTime.now());
			userRepo.save(newUser);
			attributes.addFlashAttribute("msg", "Registration Successful!");
			return "redirect:/MerchantRegistration";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " +e.getMessage());
			return "redirect:/MerchantRegistration";
		}
	}
	
	
	//Contact Us from process start

		@GetMapping("/ContactUs")
		public String ShowContactUs(Model model) {
			Enquiry enquiry = new Enquiry();
			model.addAttribute("enquiry", enquiry);
			return "ContactUs";
		}

		@PostMapping("/ContactUs")
		public String SubmitEnquiry(@ModelAttribute("enquiry") Enquiry enquiry, RedirectAttributes attributes) {
			try {
				enquiry.setEnquiryDate(LocalDateTime.now());
				enquiryRepo.save(enquiry);
				attributes.addFlashAttribute("msg", "Enquiry form Successfully Submitted");
				
				return "redirect:/ContactUs";

			} catch (Exception e) {
				attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
				return "redirect:/ContactUs";
			}
		}
		//Contact Us process end
	
	

}
