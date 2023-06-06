package com.github.leovd100.dscatalog.resources;



import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDto> findById(@PathVariable("id") Long id){
		CategoryDto category = service.findById(id);
		return ResponseEntity.ok().body(category);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDto> insert(@RequestBody CategoryDto dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDto> update(@PathVariable("id") Long id, @RequestBody CategoryDto dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
