package ru.taustudio.duckview.control.screenshotcontrol.user;

import org.springframework.security.core.AuthenticationException;

public class EmailVerificationTokenHasExpiredException extends AuthenticationException {

  public EmailVerificationTokenHasExpiredException(String msg) {
    super(msg);
  }
}
