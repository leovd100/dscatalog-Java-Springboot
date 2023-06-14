package com.github.leovd100.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.leovd100.dscatalog.dto.ProductDto;
import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.entities.Product;
import com.github.leovd100.dscatalog.repositories.CategoryRepository;
import com.github.leovd100.dscatalog.repositories.ProductRepository;
import com.github.leovd100.dscatalog.services.exceptions.DataBaseException;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<ProductDto> findAll(){
		 List<Product> products = repository.findAll();
		 return products.stream().map(element -> new ProductDto(element)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Page<ProductDto> findAll(Pageable pageable){
		 Page<Product> products = repository.findAll(pageable);
		 return products.map(element -> new ProductDto(element));
	}
	
	@Transactional(readOnly = true)
	public ProductDto findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
		return new ProductDto(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDto insert(ProductDto dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDto(entity);
	}

	@Transactional
	public ProductDto update(Long id, ProductDto dto) {
		try {
			Product entity = repository.getOne(id); 
			//entity.setName(dto.getName());
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDto(entity);
		}catch(EntityNotFoundException e ) {
			throw new ResourceNotFoundException("Id not found");
		}
	}

	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found");
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}

	private void copyDtoToEntity(ProductDto dto, Product entity) {
		// TODO Auto-generated method stub
		entity.setDate(dto.getDate());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		
		dto.getCategories().stream().forEach(e -> {
			Category category = categoryRepository.getOne(e.getId());
			entity.getCategories().add(category);
		});
		
	}

	
}
