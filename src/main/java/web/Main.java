package web;

import java.security.*;
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
		m.password = sha512(password);
		m.name = name;
		Session s = factory.openSession();
		s.save(m);
		return "redirect:/login";
	}
	
	@Autowired
	SessionFactory factory;

	public String sha512(String password){
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(password.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i=0; i< bytes.length ;i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1));
			}
			generatedPassword = sb.toString();
		} 
		catch (Exception e) { }
		return generatedPassword;
	}
	
}