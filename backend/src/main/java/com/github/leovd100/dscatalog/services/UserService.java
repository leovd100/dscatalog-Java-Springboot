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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.leovd100.dscatalog.dto.UserDto;
import com.github.leovd100.dscatalog.dto.UserInsertDTO;
import com.github.leovd100.dscatalog.dto.UserUpdateDTO;
import com.github.leovd100.dscatalog.entities.Role;
import com.github.leovd100.dscatalog.entities.User;
import com.github.leovd100.dscatalog.repositories.RoleRepository;
import com.github.leovd100.dscatalog.repositories.UserRepository;
import com.github.leovd100.dscatalog.services.exceptions.DataBaseException;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder crypt;
	
	@Transactional(readOnly = true)
	public List<UserDto> findAll(){
		 List<User> products = repository.findAll();
		 return products.stream().map(element -> new UserDto(element)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Page<UserDto> findAll(Pageable pageable){
		 Page<User> products = repository.findAll(pageable);
		 return products.map(element -> new UserDto(element));
	}
	
	@Transactional(readOnly = true)
	public UserDto findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
		return new UserDto(entity);
	}
	
	@Transactional
	public UserDto insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(crypt.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDto(entity);
	}

	@Transactional
	public UserDto update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id); 
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDto(entity);
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

	private void copyDtoToEntity(UserDto dto, User entity) {
		// TODO Auto-generated method stub
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		
		entity.getRoles().clear();
		
		dto.getRoles().forEach(roleDto -> {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		});
		
	}

	
}
