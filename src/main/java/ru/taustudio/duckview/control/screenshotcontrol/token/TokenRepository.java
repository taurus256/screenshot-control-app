package ru.taustudio.duckview.control.screenshotcontrol.token;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
  public Token findByUser(ScUser user);
  public Token findByUuidAndCreateTimeGreaterThanEqual(String uuid, LocalDateTime intervalStart);
  public Token findByUserAndCreateTimeGreaterThanEqual(ScUser user, LocalDateTime intervalStart);
}
