package com.github.leovd100.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.leovd100.dscatalog.dto.CategoryDto;
import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.repositories.CategoryRepository;
import com.github.leovd100.dscatalog.services.exceptions.DataBaseException;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	public CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDto> findAll(){
		 List<Category> categories = repository.findAll();
		 return categories.stream().map(element -> new CategoryDto(element)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		return new CategoryDto(obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found")));
	}
	
	@Transactional
	public CategoryDto insert(CategoryDto dto) {
		Category category = new Category(dto);
		category = repository.save(category);
		return new CategoryDto(category);
	}

	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {
		try {
			Category entity = repository.getOne(id); 
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDto(entity);
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
}
