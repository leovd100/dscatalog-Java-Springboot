package com.github.leovd100.dscatalog.dto;

import com.github.leovd100.dscatalog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDto{

	private static final long serialVersionUID = 1L;
	
	private String password;

	UserInsertDTO() {
		super();
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
