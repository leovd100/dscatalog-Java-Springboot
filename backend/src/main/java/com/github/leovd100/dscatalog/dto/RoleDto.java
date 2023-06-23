package com.github.leovd100.dscatalog.dto;

import java.io.Serializable;

import com.github.leovd100.dscatalog.entities.Role;

public class RoleDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String authority;
	
	public RoleDto() {}

	public RoleDto(Long id, String authority) {
	
		this.id = id;
		this.authority = authority;
	}
	
	public RoleDto(Role entity) {
	
		this.id = entity.getId();
		this.authority = entity.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	
	
}
