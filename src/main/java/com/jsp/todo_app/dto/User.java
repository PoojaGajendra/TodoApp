package com.jsp.todo_app.dto;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jsp.todo_app.helper.AES;

import lombok.Data;

@Entity
@Data
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	private String name;
	private String email;
	private String password;
	private String gender;
	private long mobile;
	private LocalDate dob;

	

	public String getPassword() {
		//to decrypt the passeord and get form DB
		return AES.decrypt(password, "123");
	}

	public void setPassword(String password) {
		//to encrypt the passeord and save in DB
		this.password = AES.encrypt(password, "123");
	}

	

	

}
