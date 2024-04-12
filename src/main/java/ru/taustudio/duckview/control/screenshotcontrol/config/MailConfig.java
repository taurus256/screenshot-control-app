package ru.taustudio.duckview.control.screenshotcontrol.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("${mail.server}")
  String mailServer;
  @Value("${mail.port}")
  Integer mailPort;
  @Value("${mail.user}")
  String mailUser;
  @Value("${mail.password}")
  String mailPassword;
  @Bean
  public JavaMailSender getJavaMailSender(){
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailServer);
    mailSender.setPort(mailPort);

    mailSender.setUsername(mailUser);
    mailSender.setPassword(mailPassword);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }

}
