package com.example.recipebuddyapp.model;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional 
public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}
