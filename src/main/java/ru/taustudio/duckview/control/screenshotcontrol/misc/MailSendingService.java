package ru.taustudio.duckview.control.screenshotcontrol.misc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;

@Service
@Slf4j
public class MailSendingService {

  @Autowired
  JavaMailSender mailSender;

  @Value("${mail.address.from}")
  String mailAddressFrom;

  @Value("${mail.address.admin}")
  String mailAddressAdmin;

  public void sendEmailToUser(ScUser user, String subject, String text){
    if (StringUtils.isEmpty(user.getEmail())){
    log.error("User {} has no email address", user.getName());
      return;
    }
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(user.getEmail());
    email.setSubject(subject);
    email.setText(text);
    email.setFrom(mailAddressFrom);
    mailSender.send(email);
  }

  public void sendEmailToAdmin(String subject, String text){
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(mailAddressAdmin);
    email.setSubject(subject);
    email.setText(text);
    email.setFrom(mailAddressFrom);
    mailSender.send(email);
  }

}
