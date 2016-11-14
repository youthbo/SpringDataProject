package se.plushogskolan.sdj.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.plushogskolan.sdj.model.Status;
import se.plushogskolan.sdj.model.User;
import se.plushogskolan.sdj.model.WorkItem;
import se.plushogskolan.sdj.model.WorkItemStatus;
import se.plushogskolan.sdj.repository.WorkItemRepository;

@Service
public class WorkItemService {
	
	private final WorkItemRepository workItemRepository;

	@Autowired
	public WorkItemService(WorkItemRepository workItemRepository) {
		this.workItemRepository = workItemRepository;
	}

	public List<WorkItem> findAllByTeamName(String name){
		return workItemRepository.findAllByTeamName(name);
	}
	
	@Transactional
	public List<WorkItem> findAllByStatus(WorkItemStatus status){
		return this.workItemRepository.findAllByStatus(status.toString());
	}

	@Transactional
	public List<WorkItem> findAllByUser(Long userId){
		return this.workItemRepository.findAllByUser(userId);
	}

	@Transactional
	public List<WorkItem> findAllByUser(User user){
		return this.workItemRepository.findAllByUser(user);
	}
	
	@Transactional
	public WorkItem create(WorkItem workItem){
		return	this.workItemRepository.save(workItem);
	}
	
	@Transactional
	public boolean updateStatus(Long id, WorkItemStatus status){
		WorkItem workItem=workItemRepository.findOne(id);
		if(workItem==null)
			return false;
		workItem.setStatus(status.toString());
		workItemRepository.save(workItem); 
		return true;
	}

	@Transactional
	public boolean delete(Long id){
		WorkItem workItem=workItemRepository.findOne(id);
		if(workItem==null)
			return false;
		workItemRepository.delete(workItem);
		return true;
	}
	
	public boolean delete(WorkItem workItem){
		return delete(workItem.getId());
	}
	
	public List<WorkItem> findByTitleContaining(String text){
		return workItemRepository.findByTitleContaining(text);
	}
	
	public List<WorkItem> findByDescriptionContaining(String text){
		return workItemRepository.findByDescriptionContaining(text);
	}

	@Transactional
	public void addWorkItemToUser(WorkItem workItem, User user){
		if( user.getStatus().equals(Status.ACTIVE.toString()) && checkNumberofWorkItems(user)){
			workItem.setUser(user);
			this.workItemRepository.save(workItem);
		}
		else{
			throw new ServiceException("Can not add a user to work item. User is not active or already has more than 5 work items.");
		}
	}

	/**
	 * This method checks to see if there are 5 work items or less assigned to a user.
	 * @param user
	 * @return
     */
	public boolean checkNumberofWorkItems(User user){
		if(this.workItemRepository.findAllByUser(user).size() < 5){
			return true;
		}
		else{
			return false;
		}
	}
}
