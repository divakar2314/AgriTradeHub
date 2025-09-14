package com.MyProject.AgritradeHub.Model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table
public class Users1 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String contactNumber;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private String aadharNumber;
	private String address;

	// ✅ CHANGED: String → byte[] + LOB (BLOB)
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePic;

	// ✅ NEW: store mime type (e.g., image/jpeg, image/png)
	private String profilePicContentType;

	private LocalDateTime regDate;

	private String panCard;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	public enum UserStatus {
		PENDING, APPROVED, DISABLED
	}

	public enum UserRole {
		ADMIN, FARMER, MERCHANT
	}

	// Getters & Setters (include new fields)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public String getProfilePicContentType() {
		return profilePicContentType;
	}

	public void setProfilePicContentType(String profilePicContentType) {
		this.profilePicContentType = profilePicContentType;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}
