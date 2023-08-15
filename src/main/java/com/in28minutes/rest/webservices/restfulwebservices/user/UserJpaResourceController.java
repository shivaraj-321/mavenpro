package com.in28minutes.rest.webservices.restfulwebservices.user;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.in28minutes.rest.webservices.restfulwebservices.jparepository.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jparepository.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResourceController {

	@Autowired
	private UserDaoService service;
	
	private UserRepository repository;
	
	private PostRepository postRepository;

	public UserJpaResourceController(UserDaoService service, UserRepository repository, PostRepository postRepository) {
		this.service = service;
		this.repository=repository;
		this.postRepository=postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retriveAllUsers() {
		return repository.findAll();
	}
	//http://localhost:8084/users
	// EntityModel
	//WebMvcLinkBuilder
	
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retriveOneUser(@PathVariable int id) {
		Optional<User> user = repository.findById(id);
		if(user.isEmpty())
			throw new UserNotFoundException("id:" + id);
		
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
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
	
	@PostMapping("/jpa/users1")
	public void createUser1(@RequestBody User user) {
		service.save(user);
	} // this also save the user resource

	@PostMapping("/jpa/users2")
	public ResponseEntity<User> createUser2(@RequestBody User user) {
		service.save(user);
	
		return ResponseEntity.created(null).build();
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = repository.save(user);
		
		//to build location
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteusers(@PathVariable int id) {
		repository.deleteById(id);
		
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrivePostsForUser(@PathVariable int id){
		Optional<User> user = repository.findById(id);
		if(user.isEmpty())
			throw new UserNotFoundException("id : " + id);
		
		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public List<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post){
		Optional<User> user = repository.findById(id);
		if(user.isEmpty())
			throw new UserNotFoundException("id : " + id);
		
		post.setUser(user.get());
		postRepository.save(post);
		return user.get().getPosts();
	}
	
	
	}

