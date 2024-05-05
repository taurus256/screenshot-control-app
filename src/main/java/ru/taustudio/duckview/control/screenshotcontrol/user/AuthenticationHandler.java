package ru.taustudio.duckview.control.screenshotcontrol.user;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthenticationHandler implements
    AuthenticationFailureHandler{

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    String redirectUrl = request.getContextPath() + "login?error=" + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8);
    response.sendRedirect(redirectUrl);
  }
}
