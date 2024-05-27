package ru.taustudio.duckview.control.screenshotcontrol.misc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;

@Service
@Slf4j
public class MonitoringService {

  @Value("${self.url}")
  String selfUrl;

  @Autowired
  MailSendingService mailSendingService;

  public void alertOnError(ScTask task, ScJob job, String errorDescr){
    mailSendingService.sendEmailToAdmin(
        String.format("Ошибка рендеринга %s (%s) ", task.getUrl(), job.getRenderer()),
        String.format("Error '%s' when rendering '%s' on '%s', link: %s ", errorDescr,
            task.getUrl(), job.getRenderer().toString(), selfUrl + "/task/" + task.getUuid()));
    log.error("Error '{}' when rendering '{}' on '{}', link: {} ",  errorDescr,
        task.getUrl(), job.getRenderer().toString(), selfUrl + "/task/" + task.getUuid());
  }

}
