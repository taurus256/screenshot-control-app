package ru.taustudio.duckview.control.screenshotcontrol.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ScUser user = userRepository.findScUsersByName(username);

		return user;
	}
}
