package se.plushogskolan.sdj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.sdj.model.Issue;
import se.plushogskolan.sdj.model.WorkItem;

public interface IssueRepository extends CrudRepository<Issue, Long>{
    
	Issue findByDescription(String description);
    
    @Query("select w from WorkItem w where w.issue =:issue")
	List<WorkItem> findAllByIssue(@Param("issue")Issue issue);
}
