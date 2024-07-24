package ru.taustudio.duckview.control.screenshotcontrol.misc;

import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.Resolution;
import ru.taustudio.duckview.control.screenshotcontrol.task.TaskService;
import ru.taustudio.duckview.control.screenshotcontrol.user.UserRepository;
import org.springframework.security.core.Authentication;

@Service
@Slf4j
public class MonitoringService {

  @Value("${self.url}")
  String selfUrl;

  @Autowired
  MailSendingService mailSendingService;

  @Autowired
  @Lazy
  TaskService taskService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  AuthenticationManager authManager;

  public void alertOnError(ScTask task, ScJob job, String errorDescr){
    mailSendingService.sendEmailToAdmin(
        String.format("Ошибка рендеринга %s (%s) ", task.getUrl(), job.getRenderer()),
        String.format("Error '%s' when rendering '%s' on '%s', link: %s ", errorDescr,
            task.getUrl(), job.getRenderer().toString(), selfUrl + "/task/" + task.getUuid()));
    log.error("Error '{}' when rendering '{}' on '{}', link: {} ",  errorDescr,
        task.getUrl(), job.getRenderer().toString(), selfUrl + "/task/" + task.getUuid());
  }

//  @Scheduled(fixedDelay = 1000)
  public void monitoringTest(){
    ScTask task = new ScTask();
    task.setUrl(selfUrl + "/static/test_pakge.html");
    task.setResolution(Resolution._1024);
    task.setWinChrome(true);
    task.setWinFirefox(true);
    task.setWinOpera(true);
    task.setWinEdge(true);
    task.setLinChrome(true);
    task.setLinChrome(true);
    task.setLinFirefox(true);
    setTestUser();
    taskService.createTask(task);
  }

  /** put test user into security context */
  private void setTestUser(){
    UsernamePasswordAuthenticationToken authReq
        = new UsernamePasswordAuthenticationToken("test_darkview_user", "tayra18");
    Authentication auth = authManager.authenticate(authReq);
    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(auth);
  }

}
