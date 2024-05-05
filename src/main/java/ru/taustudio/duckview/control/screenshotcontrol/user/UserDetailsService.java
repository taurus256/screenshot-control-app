package ru.taustudio.duckview.control.screenshotcontrol.user;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.misc.MailSendingService;
import ru.taustudio.duckview.control.screenshotcontrol.token.TokenRepository;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TokenRepository tokenRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws AuthenticationException {
		ScUser user = userRepository.getScUserByName(username);

		if (user == null) {
			throw new UsernameNotFoundException("Пользователь " + username + " не найден");
		}

		if (!user.getEnabled()) {
			if (tokenRepository.findByUserAndCreateTimeGreaterThanEqual(user, LocalDateTime.now().minusDays(1)) != null) {
				throw new DisabledException(
						"Адрес вашей электронной почты ещё не подтвержден. Перейдите по ссылке, указанной в письме");
			} else {
				throw new EmailVerificationTokenHasExpiredException("Адрес электронной почты не был подтвержден в течение суток. Попробуйте создать пользователя заново");
			}
		}

		return user;
	}
}
