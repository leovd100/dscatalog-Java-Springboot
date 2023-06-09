package com.github.leovd100.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.github.leovd100.dscatalog.entities.Product;
import com.github.leovd100.dscatalog.tests.Factory;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Slf4j
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistsId;
	private Long countTotalProducts;
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistsId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void deleteShouldThrowEmptyResultNotFouldExcpetionWhenIdDoesNotExists() {
		
		 Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {

			 repository.deleteById(nonExistsId);
		 });
	}
	
	@Test
	public void findShouldFindByIdWhenIdExists() {
		Optional<Product> product = repository.findById(existingId);
		Assertions.assertTrue(product.isPresent());
	}
	
	@Test
	public void findShouldThrowEmptyResultDataAccessExceptionWhenIdNonExists() {
		Optional<Product> product = repository.findById(nonExistsId);
		Assertions.assertFalse(product.isPresent());
	}
	
	
}
