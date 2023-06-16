package com.github.leovd100.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.leovd100.dscatalog.dto.ProductDto;
import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.entities.Product;
import com.github.leovd100.dscatalog.repositories.CategoryRepository;
import com.github.leovd100.dscatalog.repositories.ProductRepository;
import com.github.leovd100.dscatalog.services.exceptions.DataBaseException;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;
import com.github.leovd100.dscatalog.tests.Factory;

import javax.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServicesTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long existId;
	private Long nonExistId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDto productDto;
	
	
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		nonExistId = 1000L;
		dependentId = 4L;
		category = Factory.createCategory();
		product = Factory.createProduct();
		productDto = Factory.createProductDto();
		page = new PageImpl<>(List.of(product));



		when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		when(repository.findById(existId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistId)).thenReturn(Optional.empty());
		when(repository.getOne(existId)).thenReturn(product);
		when(categoryRepository.getOne(existId)).thenReturn(category);
		when(repository.findAll()).thenReturn(List.of(product));


		doThrow(ResourceNotFoundException.class).when(repository).findById(nonExistId);
		doNothing().when(repository).deleteById(existId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		doThrow(EntityNotFoundException.class).when(repository).getOne(nonExistId);
		doThrow(EntityNotFoundException.class).when(categoryRepository).getOne(nonExistId);
	}

	@Test
	void saveShouldReturnProductDtoWhenSaveEntity(){
		ProductDto result = service.insert(productDto);
		Assertions.assertEquals(result.getDate(), productDto.getDate());
		Assertions.assertEquals(result.getName(), productDto.getName());
	}


	@Test
	void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productDto.setDate(Instant.now());
			productDto.setName("Fridge");
			ProductDto productResult = service.update(nonExistId, productDto);
		});

	}
	
	@Test
	public void updateShouldReturnAProductDtoWhenExistsId() {

		productDto.setDate(Instant.now());
		productDto.setName("Fridge");
		ProductDto productResult = service.update(existId, productDto);

		Assertions.assertEquals(productResult.getDate(),productDto.getDate());
		Assertions.assertEquals(productResult.getName(), productDto.getName());
	}
	
	
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		ProductDto productDto = service.findById(existId);
		Assertions.assertNotNull(productDto);
		verify(repository, times(1)).findById(existId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistId);
		});
		verify(repository, times(1)).findById(nonExistId);
	}
	
	
	@Test
	public void findAllShouldReturnAListWithoutRequestParameters(){
		List<ProductDto> result = service.findAll();
		Assertions.assertFalse(result.isEmpty());
	}


	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDto> result = service.findAll(pageable);
		
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenIdExist() {
		
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
		
		verify(repository, times(1)).deleteById(dependentId);
	}
	
	
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistId);
		});
		
		verify(repository, times(1)).deleteById(nonExistId);
	}
	
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existId);
		});
		
		verify(repository, times(1)).deleteById(existId);
	}
	

}
