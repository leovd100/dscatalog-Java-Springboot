package com.github.leovd100.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.github.leovd100.dscatalog.dto.ProductDto;
import com.github.leovd100.dscatalog.repositories.ProductRepository;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductTestIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existId;
	private Long nonExistId;
	
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existId = 1L;
		nonExistId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(existId);
		Assertions.assertEquals(countTotalProducts-1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.delete(nonExistId);		
		});
		
	}
	
	@Test
	public void findAllPagedShouldReturnPagedWhenPage0Size10() {
		
		PageRequest page = PageRequest.of(0, 10);
		
		Page<ProductDto> result = service.findAll(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0,result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExists() {
		
		PageRequest page = PageRequest.of(50, 10);
		
		Page<ProductDto> result = service.findAll(page);
		
		Assertions.assertTrue(result.isEmpty());
		
	}
	
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortedByName() {
		
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDto> result = service.findAll(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
		
	}
	
}
