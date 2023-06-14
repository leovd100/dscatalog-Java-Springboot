package com.github.leovd100.dscatalog.tests;

import java.time.Instant;

import com.github.leovd100.dscatalog.dto.ProductDto;
import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product product =  new Product(1L, "Phone", "Good Phone", 800.00, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}
	
	public static ProductDto createProductDto() {
		Product product = createProduct();
		return new ProductDto(product, product.getCategories());
	}
}
