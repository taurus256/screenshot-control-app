package ru.taustudio.duckview.control.screenshotcontrol.task;

import static ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.JobStatus.PREVIEW_IS_READY;
import static ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.JobStatus.SUCCESS;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.JobStatus;
import ru.taustudio.duckview.control.screenshotcontrol.job.JobService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ru.taustudio.duckview.control.screenshotcontrol.misc.ImageProcessingService;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private JobService jobService;
  @Qualifier("eurekaClient")
  @Autowired
  EurekaClient eurekaClient;
  @Autowired
  ImageProcessingService imageProcessingService;

  public ScTask createTask(ScTask task) {
    ScUser user = (ScUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user.getId() != null) {
      task.setUser(user); //set only if user comes from context
    }
    task.setUuid(UUID.randomUUID().toString());
    ScTask t = taskRepository.save(task);
    jobService.createJobs(t);
    return t;
  }

  public ScTask getTask(String taskUUID) {
    return taskRepository.getScTaskByUuid(taskUUID);
  }

  public List<ScTask> getTaskForCurrentUser() {
    ScUser user = (ScUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user.getId() == null) {
      return new ArrayList<ScTask>();
    }
    return taskRepository.findAllByUser(user);
  }

  public Set<String> getAppNamesFromContext() {
    Set<String> applicationNames = new HashSet<>();
    for (Application app : eurekaClient.getApplications().getRegisteredApplications()) {
      if (!"CONTROL-APP".equals(app.getName())) {
        applicationNames.addAll(Arrays.asList(app.getName().split(",")));
      }
    }
    return applicationNames;
  }

  public void startDiffGeneration(String taskUUID, String jobUUID) {
    //Проверяем, что такой job-образец у данного task вообще есть
    final String sampleUUID = taskRepository.getScTaskByUuid(taskUUID).getJobList().stream()
        .filter(job -> Objects.equals(
            job.getUuid(), jobUUID)).map(ScJob::getUuid).findFirst().orElseThrow();
    //Формируем список для сравнения с образцом
    final List<String> instanceUUIDs = taskRepository.getScTaskByUuid(taskUUID).getJobList().stream()
        .filter(job -> !Objects.equals(
            job.getUuid(), jobUUID)).map(ScJob::getUuid).collect(Collectors.toList());
    System.out.println("sampleId = " + sampleUUID);
    System.out.println("instances = " + instanceUUIDs);
    Set<String> previewReadySet = imageProcessingService.generateDiffs(sampleUUID, instanceUUIDs);
    instanceUUIDs.forEach(instanceUUID -> {
      if (previewReadySet.contains(instanceUUID)) {
        jobService.setJobStatusByUUID(instanceUUID, JobStatus.PREVIEW_IS_READY);
      } else {
        jobService.setJobStatusByUUID(instanceUUID, JobStatus.ERROR);
      }
    });
  }

  public Map<String, Object> getJobDataList(String taskUUID) {
    Map<String, Object> result = new HashMap<>();
    taskRepository.getScTaskByUuid(taskUUID).getJobList()
        .forEach(job -> result.put(job.getUuid(), Map.of("status", job.getStatus())));
    return result;
  }

  public Map<String, Object> getJobDataListV2(String taskUUID) {
    Map<String, Object> result = new HashMap<>();
    result.put("uuid", taskUUID);
    ScTask task = taskRepository.getScTaskByUuid(taskUUID);
    if (task != null){
      result.put("cards", taskRepository.getScTaskByUuid(taskUUID).getJobList(). stream()
              .map(job -> Map.of("id", job.getId(),
                      "uuid", job.getUuid(),
                      "os", job.getOperationSystem().getShortname(),
                      "browser", job.getRenderer().name(),
                      "status", job.getStatus().name(),
                      "links", generateLinks(job))
              )
              .collect(Collectors.toList()));
    }
    return result;
  }

  private Map<String, String> generateLinks(ScJob job){
    Map<String, String> res = new HashMap<>();
    if (SUCCESS.equals(job.getStatus()) || PREVIEW_IS_READY.equals(job.getStatus())){
      res.put("screenPreview", "/{jobUUID}/preview".replace("{jobUUID}", job.getUuid()));
      res.put("screenFull", "/{jobUUID}/show".replace("{jobUUID}", job.getUuid()));
    }
    if (PREVIEW_IS_READY.equals(job.getStatus())) {
      res.put("diffPreview", "/{jobUUID}/diff/preview".replace("{jobUUID}", job.getUuid()));
      res.put("diffFull", "/{jobUUID}/diff/show".replace("{jobUUID}", job.getUuid()));
    }
    return res;
  }

}
