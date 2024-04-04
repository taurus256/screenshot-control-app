package ru.taustudio.duckview.control.screenshotcontrol.user;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;


@Service
public class UserService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;
	Pattern pattern = Pattern.compile("[A-Za-z._0-9\\-]*@[A-Za-z._0-9\\-]*");

	public void createUser(ScUser user) throws UserValidationException {
		if (StringUtils.isEmpty(user.getName())){
			throw new UserValidationException("Имя пользователя не задано");
		}
		if (!userRepository.findScUsersByName(user.getName()).isEmpty()){
			throw new UserValidationException("Пользователь с таким именем уже существует");
		}
		if (StringUtils.isEmpty(user.getEmail())){
			throw new UserValidationException("Почтовый адрес не указан");
		}
		if (!pattern.matcher(user.getEmail()).matches()){
			throw new UserValidationException("Почтовый адрес задан некорректно");
		}
		if (StringUtils.isEmpty(user.getPasswordString())){
			throw new UserValidationException("Пароль не задан");
		}
		if (StringUtils.isEmpty(user.getPasswordRepeat())){
			throw new UserValidationException("Повтор пароля не задан");
		}
		if (!StringUtils.equals(user.getPasswordString(), user.getPasswordRepeat())){
			throw new UserValidationException("Введенные пароли не совпадают");
		}
		if (user.getPasswordString() != null){
			user.setPassword(passwordEncoder.encode(user.getPasswordString()));
		}
		userRepository.save(user);
	}
}
