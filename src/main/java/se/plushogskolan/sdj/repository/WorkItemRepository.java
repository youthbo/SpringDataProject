package se.plushogskolan.sdj.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.sdj.model.User;
import se.plushogskolan.sdj.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long> {

	List<WorkItem> findAllByStatus(String status);

	List<WorkItem> findAllByUser(long userId);

	List<WorkItem> findAllByUser(User user);

	List<WorkItem> findByDescriptionContaining(String text);

	List<WorkItem> findByTitleContaining(String text);

	@Query("select w from WorkItem w where w.user.team.name=:teamName")
	List<WorkItem> findAllByTeamName(@Param("teamName") String teamName);
	
	@Query("select w from WorkItem w where w.status='Done' and w.updatedDate>:start and w.updatedDate<:end")
	Page<WorkItem> findFinishedWorkItem(@Param("start")ZonedDateTime start,@Param("end")ZonedDateTime end,Pageable pageable);
}
