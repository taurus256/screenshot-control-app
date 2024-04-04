package ru.taustudio.duckview.control.screenshotcontrol.user;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.PatternMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;

@Controller
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	Pattern pattern = Pattern.compile("[A-Za-z._0-9\\-]*@[A-Za-z._0-9\\-]*");

	@PostMapping
	public String addUser(@ModelAttribute("user") ScUser user, Model model){
		try {
			System.out.println("user = " + user);
			userService.createUser(user);
		} catch (UserValidationException uex){
			model.addAttribute("message", uex.getMessage());
			return "darkview/front-end/pages/registration";
		}
		return "darkview/front-end/pages/main";
	}

}
