package ru.taustudio.duckview.control.screenshotcontrol.token;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.entity.Token;
import ru.taustudio.duckview.control.screenshotcontrol.user.UserRepository;
import ru.taustudio.duckview.control.screenshotcontrol.user.UserValidationException;

@Service
public class TokenService {

  @Autowired
  TokenRepository tokenRepository;

  @Autowired
  UserRepository userRepository;

  /***
   * @return UUID of created token
   */
  public String createTokenForUser(ScUser user) throws UserValidationException {
    Token token =  tokenRepository.findByUser(user);
    if (token != null){
      throw new UserValidationException("Для пользователя " + user.getUsername() + " уже создан токен подтверждения адреса электронной почты");
    }
    return tokenRepository.save(Token.builder()
            .uuid(UUID.randomUUID().toString())
            .createTime(LocalDateTime.now())
            .user(user)
        .build()).getUuid();
  }

  public void processUserToken(String tokenUuid) throws TokenNotFoundException{
    Token token = tokenRepository.findByUuidAndCreateTimeGreaterThanEqual(tokenUuid,
        LocalDateTime.now().minusDays(1));
    if (token == null){
      throw new TokenNotFoundException();
    }
    ScUser user = token.getUser();
    user.setEnabled(true);
    userRepository.save(user);
  }
}
