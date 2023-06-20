package com.github.leovd100.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leovd100.dscatalog.dto.ProductDto;
import com.github.leovd100.dscatalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIt {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
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
	public void findAllShouldReturnSortedPageWhenSortedByName() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));

	}
	
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() throws Exception{
		
		ProductDto productDto = Factory.createProductDto();
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		String expectedName = productDto.getName();
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existId));
		result.andExpect(jsonPath("$.name").value(expectedName));
	}
	

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		
		ProductDto productDto = Factory.createProductDto();
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
}
