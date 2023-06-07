package com.github.leovd100.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.entities.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String description;
	private Double price;
	private String imgUrl;
	private Instant date;
	
	private List<CategoryDto> categories = new ArrayList<>();
	
	
	public ProductDto() {
		
	}


	public ProductDto(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDto(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
	}
	
	public ProductDto(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDto(cat)));
	}
	
}
