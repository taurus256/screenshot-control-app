package ru.taustudio.duckview.control.screenshotcontrol.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.taustudio.duckview.control.screenshotcontrol.task.TaskService;

@Controller
@RequestMapping("/")
public class StartController {
  @Autowired
  TaskService taskService;

  @GetMapping
  String callStart(Model model){

    model.addAttribute("applicationNames", taskService.getAppNamesFromContext());
    return "start";
  }
}
