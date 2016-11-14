package se.plushogskolan.sdj.repository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.sdj.model.User;
import se.plushogskolan.sdj.model.WorkItem;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long> {

	List<WorkItem> findAllByStatus(String status);

	List<WorkItem> findAllByUser(long userId);

	List<WorkItem> findAllByUser(User user);

	List<WorkItem> findByDescriptionContaining(String text);

	List<WorkItem> findByTitleContaining(String text);

	@Query("select w from WorkItem w where w.user.team.name=:teamName")
	List<WorkItem> findAllByTeamName(@Param("teamName") String teamName);
}
