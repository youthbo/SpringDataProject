package se.plushogskolan.sdj.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.plushogskolan.sdj.model.WorkItem;
import se.plushogskolan.sdj.model.WorkItemStatus;
import se.plushogskolan.sdj.model.Issue;
import se.plushogskolan.sdj.repository.IssueRepository;
import se.plushogskolan.sdj.repository.WorkItemRepository;

@Service
public class IssueService {

	private IssueRepository issueRepository;
	private WorkItemRepository workItemRepository;

	@Autowired
	public IssueService(IssueRepository issueRepository, WorkItemRepository workItemRepository) {
		this.issueRepository = issueRepository;
		this.workItemRepository = workItemRepository;
	}

	@Transactional
	public Issue createIssue(Issue issue) {
		try {
		  if (issue.getId()==null){
			Issue newIssue = issueRepository.save(issue);
			return newIssue;
		  }else{
			  throw new ServiceException("Create issue " + issue.getDescription() + " failed. Issue already exists");
		  }
		} catch (Exception e) {
			throw new ServiceException("Create issue " + issue.getDescription() + " failed", e);
		}
        
	}

	@Transactional
	public void assignToWorkItem(Issue issue, WorkItem workItem) {
		WorkItem newWorkItem = workItem;
		Issue newIssue=issue;
		try {
			if (issue.getId()==null){
				newIssue=issueRepository.save(issue);
			}
			if (workItem.getId()==null){
				newWorkItem = workItemRepository.save(workItem);
			}
			if (newWorkItem.getStatus().equals("Done")) {
				newWorkItem.setIssue(newIssue);
				newWorkItem.setStatus(WorkItemStatus.Unstarted.toString());
				workItemRepository.save(newWorkItem);
			} else {
				throw new ServiceException("Assign issue to work item failed. Status of work item is not 'Done'");
			}
		} catch (Exception e) {
			throw new ServiceException("Could not assign issue to work item", e);
		}

	}

	@Transactional
	public Issue updateIssue(Issue issue, String new_description) {
		try {
			Issue newIssue=issue;
			if (issue.getId()==null){
				newIssue = issueRepository.save(issue);
			}
			Issue findIssue = issueRepository.findByDescription(new_description);
			if (!issueRepository.exists(issue.getId())) {
				throw new ServiceException(
						"Could not update issue.Issue:" + issue.getDescription() + " doesn't exist.");
			}
			if (findIssue == null) {
				newIssue.setDescription(new_description);
				issueRepository.save(newIssue);
				return newIssue;
			} else
				throw new ServiceException("Issue with name:" + new_description + " already exists.");
		} catch (Exception e) {
			throw new ServiceException("Could not update issue with id:" + issue.getId(), e);
		}
	}

	public List<WorkItem> getAllItemsWithIssue(Issue issue) {
		try {
			return issueRepository.findAllByIssue(issue);
		} catch (Exception e) {
			throw new ServiceException("Could not get all items with issue:" + issue.getDescription(), e);
		}
	}

	public Issue getIssueByName(String name) {
		try {
			return issueRepository.findByDescription(name);
		} catch (Exception e) {
			throw new ServiceException("Could not get issue with name:" + name, e);
		}
	}
	
	public Issue getIssueById(Long id){
		try {
			return issueRepository.findOne(id);
		} catch (Exception e) {
			throw new ServiceException("Could not get issue with id:" + id, e);
		}
	}

}
