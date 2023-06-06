package com.github.leovd100.dscatalog.dto;

import java.io.Serializable;

import com.github.leovd100.dscatalog.entities.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	
	public CategoryDto() {}


	public CategoryDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDto(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}
	
	
	
}
