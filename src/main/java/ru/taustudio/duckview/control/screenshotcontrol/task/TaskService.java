package ru.taustudio.duckview.control.screenshotcontrol.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.job.JobService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private JobService jobService;

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

	public ScTask getTask(Long taskId) {
		return taskRepository.getReferenceById(taskId);
	}

	public List<ScTask> getTaskForCurrentUser(){
		ScUser user = (ScUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getId() == null) return new ArrayList<ScTask>();
		return taskRepository.findAllByUser(user);
	}
}
