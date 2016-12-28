package web;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class Main {

	@RequestMapping("/test") @ResponseBody
	int test() {
		return 555;
	}
	
	
}