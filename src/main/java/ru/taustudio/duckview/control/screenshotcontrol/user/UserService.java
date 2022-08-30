package ru.taustudio.duckview.control.screenshotcontrol.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;


@Service
public class UserService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	public void createUser(ScUser user){
		if (user.getPasswordString() != null){
			user.setPassword(passwordEncoder.encode(user.getPasswordString()));
		}
		userRepository.save(user);
	}
}
