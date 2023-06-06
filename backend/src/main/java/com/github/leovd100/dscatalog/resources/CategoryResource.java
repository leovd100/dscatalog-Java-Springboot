package com.github.leovd100.dscatalog.resources;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.leovd100.dscatalog.dto.CategoryDto;
import com.github.leovd100.dscatalog.entities.Category;
import com.github.leovd100.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping("/checkserver")
	public ResponseEntity<String> checkServer(){
		return ResponseEntity.ok("Server on");
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> findAll(){
		List<CategoryDto> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
}
