package com.github.leovd100.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.leovd100.dscatalog.dto.CategoryDto;
import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.repositories.CategoryRepository;
import com.github.leovd100.dscatalog.services.exceptions.EntityNotFoundException;

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
		return new CategoryDto(obj.orElseThrow(() -> new EntityNotFoundException("Entity Not Found")));
	}
}
