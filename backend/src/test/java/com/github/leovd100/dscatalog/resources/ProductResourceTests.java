package com.github.leovd100.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leovd100.dscatalog.dto.ProductDto;
import com.github.leovd100.dscatalog.services.ProductService;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;
import com.github.leovd100.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;

	@Autowired
	private ObjectMapper objectMapper;
	
	private PageImpl<ProductDto> page;
	private ProductDto productDto;
	private Long existId;
	private Long nonExistId;
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		nonExistId = 2L;
		
		productDto = Factory.createProductDto();
		page = new PageImpl<>(List.of(productDto));
		
		when(service.findAll(any())).thenReturn(page);
		when(service.findById(existId)).thenReturn(productDto);
		
		when(service.findById(nonExistId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.update(eq(existId), any())).thenReturn(productDto);
		when(service.update(eq(nonExistId), any())).thenThrow(ResourceNotFoundException.class);

	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() throws Exception{
		
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		
	}
	
	
	@Test
	public void findByShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void findByShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
}
