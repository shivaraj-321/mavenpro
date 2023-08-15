package com.in28minutes.rest.webservices.restfulwebservices.user;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResourceController {

	@Autowired
	private UserDaoService service;

	public UserResourceController(UserDaoService service) {
		this.service = service;
	}

	@GetMapping("/users")
	public List<User> retriveAllUsers() {
		return service.findAllUsers();
	}
	//http://localhost:8084/users
	// EntityModel
	//WebMvcLinkBuilder
	
	@GetMapping("/users/{id}")
	public EntityModel<User> retriveOneUser(@PathVariable int id) {
		User user = service.findUserById(id);
		if(user == null)
			throw new UserNotFoundException("id:" + id);
		
		EntityModel<User> entityModel = EntityModel.of(user);
		
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retriveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}
	
	/*
	@GetMapping("/users/{id}")
	public User retriveOneUser(@PathVariable int id) {
		User user = service.findUserById(id);
		if(user == null)
			throw new UserNotFoundException("id:" + id);
		
		return user;
	}
	*/
	
	@PostMapping("/users1")
	public void createUser1(@RequestBody User user) {
		service.save(user);
	} // this also save the user resource

	@PostMapping("/users2")
	public ResponseEntity<User> createUser2(@RequestBody User user) {
		service.save(user);
	
		return ResponseEntity.created(null).build();
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		
		//to build location
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
	@DeleteMapping("/users/{id}")
	public void deleteusers(@PathVariable int id) {
		service.deleteById(id);
		
	}
	}

