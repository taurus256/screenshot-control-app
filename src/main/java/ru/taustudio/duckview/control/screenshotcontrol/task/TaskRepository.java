package ru.taustudio.duckview.control.screenshotcontrol.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScTask;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<ScTask, Long> {
	List<ScTask> findAllByUser(ScUser user);
}
