package web;

import org.hibernate.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

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
		Member m = new Member();
		m.email = email;
		m.password = password;
		m.name = name;
		Session s = factory.openSession();
		s.save(m);
		return "redirect:/login";
	}
	
	@Autowired
	SessionFactory factory;
}