package ru.taustudio.duckview.control.screenshotcontrol.user;

import com.netflix.discovery.EurekaClient;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.misc.MailSendingService;
import ru.taustudio.duckview.control.screenshotcontrol.task.TaskRepository;
import ru.taustudio.duckview.control.screenshotcontrol.token.TokenService;


@Service
@Slf4j
public class UserService {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MailSendingService mailSendingService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	TokenService tokenService;

	@Qualifier("eurekaClient")
	@Autowired
	EurekaClient eurekaClient;
	Pattern pattern = Pattern.compile("[A-Za-z._0-9\\-]*@[A-Za-z._0-9\\-]*");

	String CURRENT_APP_URL = "http://darkview.ru:8081";

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
		String tokenUuid = tokenService.createTokenForUser(user);
		mailSendingService.sendEmailToUser(user, "DarkView: подтверждение регистрации",
				"Чтобы продолжить регистрацию, перейдите по данной ссылке: " + getTokenLink(tokenUuid) + ".");
	}

	private String getTokenLink(String uuid){
		return CURRENT_APP_URL + "/token/" + uuid;
	}
}
