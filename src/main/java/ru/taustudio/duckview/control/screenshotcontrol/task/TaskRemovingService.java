package ru.taustudio.duckview.control.screenshotcontrol.task;

import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.deleteFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.JobStatus;

/* Сервис удаляет задачи и их файлы, созданные более 1 дня назад */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskRemovingService {
  private final TaskRepository taskRepository;

  @Scheduled(cron = "@daily")
  public void deleteOldTasks(){
    List<ScTask> taskList = taskRepository.findByCreateTimeLessThan(Instant.now().minus(1,
        ChronoUnit.DAYS));
    log.info("Start removing old tasks");
    for (ScTask t: taskList){
      t.getJobList().forEach(this::deleteJobFiles);
      taskRepository.delete(t);
      log.info("Task {} ({}) has been deleted", t.getUuid(), t.getUrl());
    }
  }

  void deleteJobFiles(ScJob job){
      try {
        if (JobStatus.SUCCESS.equals(job.getStatus())){
          deleteFile(job.getUuid());
          deleteFile(job.getUuid() + ".preview");
          if (JobStatus.PREVIEW_IS_READY.equals(job.getStatus())){
            deleteFile(job.getUuid() + ".diff");
            deleteFile(job.getUuid() + ".diff.preview");
          }
        }
      } catch (IOException e) {
        log.error("ERROR: Cannot delete preview files for {}", job.getUuid());
      }
  }
}