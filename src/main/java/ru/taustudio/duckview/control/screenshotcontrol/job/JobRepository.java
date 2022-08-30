package ru.taustudio.duckview.control.screenshotcontrol.job;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScJob;

@Repository
public interface JobRepository extends JpaRepository<ScJob, Long> {
}
