package ru.taustudio.duckview.control.screenshotcontrol.ajax;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taustudio.duckview.control.screenshotcontrol.task.TaskService;

@RestController
@RequestMapping("/rest")
public class AjaxController {
  @Autowired
  TaskService taskService;

  @GetMapping("/{taskUUID}/data")
  public Map<String,Object> getTaskData(@PathVariable String taskUUID){
    return taskService.getJobDataList(taskUUID);
  }
}
