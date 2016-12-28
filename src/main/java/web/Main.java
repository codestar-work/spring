package web;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class Main {
	
	@RequestMapping("/")
	String showIndex() {
		return "index";
	}
	
	@RequestMapping("/register")
	String showRegister() {
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	String saveUser(String email, String password, String name) {
		
		return "redirect:/login";
	}
	
}