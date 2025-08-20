package ru.taustudio.duckview.control.screenshotcontrol.misc;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
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
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.JobStatus;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.Resolution;
import ru.taustudio.duckview.control.screenshotcontrol.task.TaskService;
import ru.taustudio.duckview.control.screenshotcontrol.user.UserRepository;
import org.springframework.security.core.Authentication;

@Service
@Slf4j
public class MonitoringService {
 //TODO: что-то с этим сделать
  private static final String TEST_USER_NAME = "test_user";

  private static final String TEST_USER_PASS = "qeJnNP%YXFy$ZUv";
  public static final double MAX_WAIT_TIME_MS = 120000;
  public static final long STEP_WAIT_MS = 10000;
  public static final Set<JobStatus> FIRST_STAGE_STATES = Set.of(JobStatus.SUCCESS);
  public static final Set<JobStatus> DIFF_STAGE_STATES = Set.of(JobStatus.SUCCESS, JobStatus.PREVIEW_IS_READY);
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

  @Scheduled(fixedDelay = 3600000, initialDelay = 30000)
  public void selfTest(){
    try {
      ScTask task = createTestTask();
      log.info("Self-test started, task id is {}", task.getUuid());
      waitForResults(task, FIRST_STAGE_STATES);
      task = taskService.getTask(task.getUuid());
      if (task.getJobList().stream().anyMatch(job -> !JobStatus.SUCCESS.equals(job.getStatus()))) {
        log.error("Self-test ends with an error(s)");
        mailSendingService.sendEmailToAdmin("DarkView self test: FAIL (этап отрисовки)", "Тест завершен неуспешно, результат: " + selfUrl + "/task/" + task.getUuid());
        return;
      }
      taskService.startDiffGeneration(task.getUuid(), task.getJobList().get(0).getUuid());
      waitForResults(task, DIFF_STAGE_STATES);
      task = taskService.getTask(task.getUuid());
      if (task.getJobList().stream().allMatch(job -> DIFF_STAGE_STATES.contains(job.getStatus()))) {
        log.error("Self-test ends with success");
        mailSendingService.sendEmailToAdmin("DarkView self test: OK", "Тест успешно завершен, результат: " + selfUrl + "/task/" + task.getUuid());
      } else {
        log.error("Self-test ends with an error(s)");
        mailSendingService.sendEmailToAdmin("DarkView self test: FAIL (карта различий)", "Тест завершен неуспешно, результат: " + selfUrl + "/task/" + task.getUuid());
      }
    } catch (Exception t){
      mailSendingService.sendEmailToAdmin("DarkView self test: FAIL (внутренняя ошибка теста)", "Произошла внутренняя ошибка при выполнении теста "
          + t.getMessage() + "\n"
          + Arrays.stream(
          t.getStackTrace())
          .map(elem -> elem.getLineNumber() + ": " +  elem.getClassName() + ": " + elem.getMethodName() + "\n")
          .collect(Collectors.joining()));
    }
  }

  private ScTask createTestTask(){
    ScTask task = new ScTask();
    task.setUrl(selfUrl + "/static/test_page.html");
    task.setResolution(Resolution._1024);
    task.setWinChrome(true);
    task.setWinFirefox(true);
//    task.setWinOpera(true);
    task.setWinEdge(true);
    task.setLinFirefox(true);
//    task.setLinOpera(true);
    task.setMacChrome(true);
    task.setMacSafari(true);
    task.setMacFirefox(true);
    task.setIosIPHONE_PRO(true);
    setTestUser();
    taskService.createTask(task);
    return task;
  }


  private void waitForResults(ScTask task, Set<JobStatus> finishStates){
    double totalTime = 0;
    task = taskService.getTask(task.getUuid());
    while (!task.getJobList().stream().allMatch(job -> (finishStates.contains(job.getStatus()) ||
        JobStatus.ERROR.equals(job.getStatus()))) && totalTime < MAX_WAIT_TIME_MS)
    {
      try {
        Thread.sleep(STEP_WAIT_MS);
        totalTime += STEP_WAIT_MS;
        log.info("Waiting for test results...");
        task = taskService.getTask(task.getUuid());
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /** put test user into security context */
  private void setTestUser(){
    UsernamePasswordAuthenticationToken authReq
        = new UsernamePasswordAuthenticationToken(TEST_USER_NAME, TEST_USER_PASS);
    Authentication auth = authManager.authenticate(authReq);
    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(auth);
  }

}
