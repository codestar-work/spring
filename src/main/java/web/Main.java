package web;
import java.util.*;
import java.security.*;
import org.hibernate.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
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
	
	@RequestMapping("/login")
	String showLogin() {
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	String checkLogin(String email, String password,
			HttpSession session) {
		Session database = factory.openSession();
		Query query = database.createQuery(
				"from Member where email = :x");
		query.setParameter("x", email);
		List<Member> result = query.list();
		Member member = result.get(0);
		
		String e = sha512(password);
		if (member.password.equals(e)) {
			session.setAttribute("member", member);
			return "redirect:/home";
		} else {
			return "redirect:/login?Incorrect Password";
		}
	}
	
	@RequestMapping("/home")
	String showHome(HttpSession session, Model model) {
		Member member = (Member)session.getAttribute("member");
		if (member == null) {
			return "redirect:/login";
		} else {
			model.addAttribute("member", member);
			return "home";
		}
	}
	
	@RequestMapping("/logout")
	String showLogout(HttpSession session) {
		session.removeAttribute("member");
		return "logout";
	}
	
	@RequestMapping("/new")
	String showNewPost(HttpSession session) {
		if (session.getAttribute("member") == null) {
			return "redirect:/login";
		} else {
			return "new";
		}
	}
	
	@RequestMapping(value="/new", method=RequestMethod.POST)
	String saveNewPost(String title, String detail,
			HttpSession session, MultipartFile file) {
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		} else {
			Topic t = new Topic();
			t.title = title;
			t.detail = detail;
			t.member = m.code;
			Session s = factory.openSession();
			s.save(t);
			return "redirect:/home";
		}
	}
	
	@RequestMapping("/list")
	String showAll(Model m, HttpSession session) {
		Session s = factory.openSession();
		Query q = s.createQuery("from Topic");
		m.addAttribute("data", q.list());
		Member member = (Member)session.getAttribute("member");
		if (member == null) {
			m.addAttribute("member", 0);
		} else {
			m.addAttribute("member", member.code);
		}
		return "list";
	}
	
	@RequestMapping("/edit/{code}")
	String editTopic(@PathVariable long code,
			HttpSession session, Model model) {
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		} else {
			Session s = factory.openSession();
			Query q = s.createQuery(
				"from Topic where code = :c and user = :u");
			q.setParameter("c", code);
			q.setParameter("u", m.code);
			List list = q.list();
			if (list.size() == 0) {
				return "edit-error";
			} else {
				Topic t = (Topic)list.get(0);
				model.addAttribute("topic", t);
				return "edit";
			}
		}
	}
	
	@RequestMapping(value="/edit/{code}", method=RequestMethod.POST)
	String saveTopic(@PathVariable long code, 
			HttpSession session, 
			String title, String detail) {
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		} else {
			Session s = factory.openSession();
			Query q = s.createQuery("from Topic where code = :c");
			q.setParameter("c", code);
			List list = q.list();
			if (list.size() == 0) {
				return "edit-error";
			} else {
				Topic t = (Topic)list.get(0);
				if (m.code == t.member) {
					t.title = title;
					t.detail = detail;
					s.beginTransaction();
					s.update(t);
					s.getTransaction().commit();
					return "redirect:/home";
				} else {
					return "edit-error";
				}
			}
		}
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