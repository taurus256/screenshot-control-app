package ru.taustudio.duckview.control.screenshotcontrol.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;

@Controller
@RequestMapping("api/scUsers")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	public String addUser(@ModelAttribute("user") ScUser user, Model model){
		System.out.println("user = " + user);
			userService.createUser(user);
		return "start";
	}


	@GetMapping
	public void test(){
		System.out.println("TEST");
	}
}
