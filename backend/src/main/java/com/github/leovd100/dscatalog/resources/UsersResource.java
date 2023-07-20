package com.github.leovd100.dscatalog.resources;



import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.leovd100.dscatalog.dto.UserDto;
import com.github.leovd100.dscatalog.dto.UserInsertDto;
import com.github.leovd100.dscatalog.dto.UserUpdateDTO;
import com.github.leovd100.dscatalog.services.UserService;



@RestController
@RequestMapping(value = "/users")
public class UsersResource {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/checkserver")
	public ResponseEntity<String> checkServer(){
		return ResponseEntity.ok("Server on");
	}
	
	@GetMapping
	public ResponseEntity<Page<UserDto>> findAll(Pageable pageable)
	{
		Page<UserDto> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDto> findById(@PathVariable("id") Long id){
		UserDto category = service.findById(id);
		return ResponseEntity.ok().body(category);
	}
	
	@PostMapping
	public ResponseEntity<UserDto> insert(@Valid @RequestBody UserInsertDto dto){
		UserDto newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDto> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO  dto){
		UserDto newdto = service.update(id, dto);
		return ResponseEntity.ok().body(newdto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
