package co.edu.icesi.ci.thymeval.controller.implementation;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.ci.thymeval.model.User;
import co.edu.icesi.ci.thymeval.service.UserServiceImpl;
import co.edu.icesi.ci.thymeval.validategroups.EditValidation;
import co.edu.icesi.ci.thymeval.validategroups.UserEndingInfo;
import co.edu.icesi.ci.thymeval.validategroups.UserStartInfo;

@Controller
public class UserControllerImpl{

	UserServiceImpl userService;

	@Autowired
	public UserControllerImpl(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	@GetMapping("/users/del/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userService.delete(user);
		model.addAttribute("users", userService.findAll());
		return "users/index";
	}
	
	@GetMapping("/users/")
	public String indexUser(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users/index";
	}
	
	@GetMapping("/users/add")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "users/add-user-1";
	}
	
	
	@PostMapping("/users/add")
	public String saveStartUser(@Validated(UserStartInfo.class) @ModelAttribute User user, 
			Model model, BindingResult result, 
			@RequestParam(value = "action", required = true) String action) {
		
		if (action.equals("Cancel")) {
			return "redirect:/users/";
		}else {
			if(result.hasErrors()) {
				return "/users/add-user-1";
			}
			User savedUser =  userService.save(user);
			
			model.addAttribute("user", savedUser);
			model.addAttribute("types", userService.getTypes());
			model.addAttribute("genders", userService.getGenders());
			
			return "/users/add-user-2";
		}	
	}
	
	@PostMapping("users/save/{id}")
	public String saveEndingUser(@Validated(UserEndingInfo.class) @ModelAttribute User user,
			Model model, BindingResult result, 
			@RequestParam(value="action", required = true) String action, 
			@PathVariable("id") long id) {
		
		if (action.equals("Cancel")) {
			userService.delete(user);
			return "redirect:/users/add-user-1";
		}else {
			if(result.hasErrors()) {
				model.addAttribute("types", userService.getTypes());
				model.addAttribute("genders", userService.getGenders());
				
				return "/users/add-user-2";
			}
			
			User userSave = userService.findById(id).get();
			
			userSave.setName(user.getName());
			userSave.setEmail(user.getEmail());
			userSave.setType(user.getType());
			userSave.setGender(user.getGender());
			userService.saveEndingUser(userSave);
			
			return "redirect:/users/";
		}	
	}

	@GetMapping("/users/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Optional<User> user = userService.findById(id);
		if (user == null)
			throw new IllegalArgumentException("Invalid user Id:" + id);
		model.addAttribute("user", user.get());
		model.addAttribute("genders", userService.getGenders());
		model.addAttribute("types", userService.getTypes());
		return "users/update-user";
	}
	
	@PostMapping("/users/edit/{id}")//%{username}%{password}
	public String updateUser(@PathVariable("id") long id,
			@RequestParam(value = "action", required = true) String action, 
			@Validated(EditValidation.class) @ModelAttribute User user, Model model, BindingResult result) {

		if(action.equals("Cancel")) {
			return "redirect:/users/";
		}else {
			System.out.println("pase 1");
			if(result.hasErrors()) {
				model.addAttribute("user", user);
				model.addAttribute("genders", userService.getGenders());
				model.addAttribute("types", userService.getTypes());
				
				return "/users/update-user";
			}else{
				User linkUser = userService.findById(id).get();
				System.out.println("pase obtiene user");
				if(linkUser.getPassword().equals(user.getPassword()) == false){
					result.addError(new FieldError("password", "password", "The old password is wrong"));
					model.addAttribute("user", user);
					model.addAttribute("genders", userService.getGenders());
					model.addAttribute("types", userService.getTypes());
					return "/users/update-user";
				}else {
					System.out.println(linkUser.getUsername());
					linkUser.setPassword(user.getNewPassword());
					linkUser.setName(user.getName());
					linkUser.setEmail(user.getEmail());
					linkUser.setUsername(user.getUsername());
					linkUser.setEmail(user.getEmail());
					linkUser.setType(user.getType());
					linkUser.setGender(user.getGender());
					userService.save(linkUser);
					
					model.addAttribute("users", userService.findAll());
					
					return "/users/index";				
				}
			}
			
		}
	}
	
//	@PostMapping("/users/edit/{id}")
//	public String updateUser(@PathVariable("id") long id,
//			@RequestParam(value = "action", required = true) String action, @Validated(EditValidation.class) User user, Model model, BindingResult bindingResult) {
//		if (action != null && !action.equals("Cancel")) {
//			User current = userService.findById(id).get();
//			if(!current.getPassword().equals(user.getPassword())){
//				bindingResult.addError(new FieldError("password", "password", "The pasword not match"));
//			}
//			if(current.getPassword() == null || user.getPassword() == null){
//				bindingResult.addError(new FieldError("password", "password", "This field can not by empty"));
//				bindingResult.addError(new FieldError("newpassword", "newpassword", "This field can not by empty"));
//			}
//			if(bindingResult.hasErrors()){
//				model.addAttribute("user", user);
//				model.addAttribute("genders", userService.getGenders());
//				model.addAttribute("types", userService.getTypes());
//				return "/users/update-user";
//
//
//			}else{
//				current.setType(user.getType());
//				current.setGender(user.getGender());
//				current.setEmail(user.getEmail());
//				current.setName(user.getName());
//				current.setPassword(user.getPassword());
//				current.setUsername(user.getUsername());
//				userService.save(current);
//			}
//			model.addAttribute("users", userService.findAll());
//		}
//		
//		return "redirect:/users/";
//	}	
}

