package com.github.leovd100.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	public CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<Category> findAll(){
		return repository.findAll();
	}
}
