package ru.taustudio.duckview.control.screenshotcontrol.task;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
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
      for (String agent : app.getName().split(",")) {
        if (!"CONTROL-APP".equals(app.getName())) {
          applicationNames.add(agent);
        }
      }
    }
    return applicationNames;
  }

  public void startDiffGeneration(String taskUUID, Long jobId) {
    final String sampleId = taskRepository.getScTaskByUuid(taskUUID).getJobList().stream()
        .filter(job -> Objects.equals(
            job.getId(), jobId)).map(job -> job.getId().toString()).findFirst().orElseThrow();
    final List<String> instances = taskRepository.getScTaskByUuid(taskUUID).getJobList().stream()
        .filter(job -> !Objects.equals(
            job.getId(), jobId)).map(job -> job.getId().toString()).collect(Collectors.toList());
    System.out.println("sampleId = " + sampleId);
    System.out.println("instances = " + instances);
    imageProcessingService.generateDiffs(sampleId, instances);
  }

  public Map<String, Object> getJobDataList(String taskUUID) {
    Map<String, Object> result = new HashMap<>();
    taskRepository.getScTaskByUuid(taskUUID).getJobList()
        .forEach(job -> result.put(job.getUuid(), Map.of("status", job.getStatus())));
    return result;
  }
}
