package ru.taustudio.duckview.control.screenshotcontrol.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;

import javax.validation.Valid;

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	TaskService taskService;

	@GetMapping("/{taskUUID}")
	public String getTask(@PathVariable String taskUUID, Model model){
		model.addAttribute("task", taskService.getTask(taskUUID));
		return "task";
	}

	@GetMapping("/history")
	public String getTaskHistory(Model model){
		model.addAttribute("taskList", taskService.getTaskForCurrentUser());
		return "history";
	}

	@PostMapping
	public String addTask(@ModelAttribute("task") @Valid ScTask task, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors() || task.getUrl() == null || task.getUrl().isEmpty()) {
			return "start";
		}
		task.setUrl("http://" + task.getUrl());
		task = taskService.createTask(task);
		model.addAttribute("task", task);
		model.addAttribute("applicationNames", taskService.getAppNamesFromContext());
		return "redirect:/task/" + task.getUuid();
	}

	@GetMapping("/diff")
	public String generateDiffs(@RequestParam String taskUuid, @RequestParam Long sampleJobId){
		taskService.startDiffGeneration(taskUuid, sampleJobId);
		return "redirect:/task/" + taskUuid;
	}

}
