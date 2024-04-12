package ru.taustudio.duckview.control.screenshotcontrol.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/token")
public class TokenController {
  @Autowired
  TokenService tokenService;
  @GetMapping("/{uuid}")
  public String processToken(@PathVariable String uuid){
    try {
      tokenService.processUserToken(uuid);
    } catch (TokenNotFoundException e) {
      return "darkview/front-end/pages/verification_fail.html";
    }
    return "darkview/front-end/pages/verification_success.html";
  }
}