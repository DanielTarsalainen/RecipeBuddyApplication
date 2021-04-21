package com.example.recipebuddyapp.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.recipebuddyapp.model.Recipe;
import com.example.recipebuddyapp.model.RecipeRepository;
import com.example.recipebuddyapp.model.User;
import com.example.recipebuddyapp.model.UserRepository;

@Controller
public class RecipeController {

	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private UserRepository userRepository;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = { "/addrecipe" })
	public String addRecipe() {
		return "addrecipe";
	}
	
	@GetMapping(value = { "/addrecipe2" })
	public String addRecipe2() {
		return "addrecipe2";
	}

	@PostMapping("/saveRecipeDetails")
	public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("name") String name,
			@RequestParam("category") String category, @RequestParam("description") String description, Model model,
			HttpServletRequest request, final @RequestParam("image") MultipartFile file) throws IOException {

		if (request.getParameter("id") != null) {
			recipeRepository.deleteById(Long.parseLong(request.getParameter("id")));
		}

		String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
		log.info("uploadDirectory:: " + uploadDirectory);
		String fileName = file.getOriginalFilename();
		String filePath = Paths.get(uploadDirectory, fileName).toString();
		log.info("FileName: " + file.getOriginalFilename());
		if (fileName == null || fileName.contains("..")) {
			model.addAttribute("invalid", "Sorry! Invalid path sequence in filename \" + fileName");
			return new ResponseEntity<>("Sorry! Invalid path sequence in filename" + fileName, HttpStatus.BAD_REQUEST);
		}
		Date createDate = new Date();

		try {
			File dir = new File(uploadDirectory);
			if (!dir.exists()) {
				log.info("Congrats! Folder Created");
				dir.mkdirs();
			}
			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			stream.write(file.getBytes());
			stream.close();
		} catch (Exception e) {
			log.info("in catch");
			e.printStackTrace();
		}

		byte[] imageData = file.getBytes();
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		User userNow = userRepository.findByUsername(username);

		Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setCategory(category);
		recipe.setDescription(description);
		recipe.setImage(imageData);
		recipe.setCreateDate(createDate);
		recipe.setUser(userNow);
		recipeRepository.save(recipe);
		return null;
	}

	@GetMapping("/recipe/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Recipe> recipe)
			throws ServletException, IOException {
		recipe = recipeRepository.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(recipe.get().getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/recipeDetails")
	String showRecipeDetails(@RequestParam("id") Long id, Optional<Recipe> recipe, Model model) {
		try {
			if (id != 0) {
				recipe = recipeRepository.findById(id);

				if (recipe.isPresent()) {
					model.addAttribute("id", recipe.get().getId());
					model.addAttribute("name", recipe.get().getName());
					model.addAttribute("category", recipe.get().getCategory());
					model.addAttribute("description", recipe.get().getDescription());
					model.addAttribute("date", recipe.get().getCreateDate());
					return "recipedetails";
				}
				return "redirect:/home";
			}
			return "redirect:/home";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/home";
		}
	}
	
	@GetMapping("/recipeDetails2")
	String showRecipeDetails2(@RequestParam("id") Long id, Optional<Recipe> recipe, Model model) {
		try {
			if (id != 0) {
				recipe = recipeRepository.findById(id);

				if (recipe.isPresent()) {
					model.addAttribute("id", recipe.get().getId());
					model.addAttribute("name", recipe.get().getName());
					model.addAttribute("category", recipe.get().getCategory());
					model.addAttribute("description", recipe.get().getDescription());
					model.addAttribute("date", recipe.get().getCreateDate());
					return "recipedetails2";
				}
				return "redirect:/ownrecipes";
			}
			return "redirect:/ownrecipes";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/ownrecipes";
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleterecipe(@PathVariable("id") Long Id, Model model) {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		User userNow = userRepository.findByUsername(username);
		model.addAttribute("recipe", recipeRepository.findByUser(userNow));
		recipeRepository.deleteById(Id);
		return "redirect:/";

	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	String editRecipe(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recipe", recipeRepository.findById(id));
		return "editrecipe";
	}

	@GetMapping({"/showAllRecipes" })
	String show(Model map, Model model) {
		List<Recipe> recipes = recipeRepository.findAll();
		map.addAttribute("recipes", recipes);
		
		LocalTime timeNow = LocalTime.now();

		int hours = timeNow.getHour() + 3;
		String message = null;

		if (hours >= 7 && hours < 12) {
			message = "Good morning";
		} else if (hours >= 12 && hours < 17) {
			message = "Good afternoon";
		} else if (hours >= 17 && hours < 22) {
			message = "Good evening";
		} else if (hours >= 22 || hours < 7) {
			message = "Good night";
		}

		model.addAttribute("message", message);

		return "recipelist";
	}

	@RequestMapping(value = {"/", "/showOwnRecipes" })
	String showAll(Model map, Model model) {
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		User userNow = userRepository.findByUsername(username);
				
		model.addAttribute("recipe", recipeRepository.findByUser(userNow));

		LocalTime timeNow = LocalTime.now();

		int hours = timeNow.getHour() + 3;
		String message = null;

		if (hours >= 7 && hours < 12) {
			message = "Good morning";
		} else if (hours >= 12 && hours < 17) {
			message = "Good afternoon";
		} else if (hours >= 17 && hours < 22) {
			message = "Good evening";
		} else if (hours >= 22 || hours < 7) {
			message = "Good night";
		}
		model.addAttribute("message", message);

		return "ownrecipes";
	}
}
