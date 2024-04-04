package ru.taustudio.duckview.control.screenshotcontrol.user;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<ScUser, Long> {
	@Override
	Optional<ScUser> findById(Long aLong);

	@Override
	Iterable<ScUser> findAll();

	@Override
	<S extends ScUser> S save(S entity);

	@Override
	void delete(ScUser entity);

	List<ScUser> findScUsersByName(String name);
	ScUser getScUserByName(String name);
}

