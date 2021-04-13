package com.example.recipebuddyapp.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

@Transactional 
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	List<Recipe> findByUser(User user);
}
