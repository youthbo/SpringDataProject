package se.plushogskolan.sdj.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.plushogskolan.sdj.model.Team;
import se.plushogskolan.sdj.model.User;
import se.plushogskolan.sdj.model.WorkItem;
import se.plushogskolan.sdj.model.WorkItemStatus;
import se.plushogskolan.sdj.model.Status;
import se.plushogskolan.sdj.repository.TeamRepository;
import se.plushogskolan.sdj.repository.UserRepository;
import se.plushogskolan.sdj.repository.WorkItemRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final WorkItemRepository workItemRepository;

	@Autowired
	public UserService(UserRepository userRepository, TeamRepository teamRepository,
			WorkItemRepository workItemRepository) {
		this.userRepository = userRepository;
		this.teamRepository = teamRepository;
		this.workItemRepository = workItemRepository;
	}

	public User getUser(Long Id) {
		return userRepository.findOne(Id);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public List<User> getUserByFirstname(String firstname) {
		return userRepository.findByFirstname(firstname);
	}

	public List<User> getUserByLastname(String lastname) {
		return userRepository.findByLastname(lastname);
	}

	public List<User> getAllUsersInTeam(Team team) {
		return userRepository.findAllByTeam(team);
	}

	@Transactional
	public User createUser(User user) {
		if (user.getUsername().length() >= 10) {
			teamRepository.save(user.getTeam());
			List<User> users = userRepository.findAllByTeam(user.getTeam());
			if (users.size() < 10) {
				return userRepository.save(user);

			} else {
				throw new ServiceException(
						"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
			}

		} else
			throw new ServiceException("Username must be atleast 10 characters long!");
	}

	@Transactional
	public User updateUser(User user) {
		if (user.getUsername().length() >= 10) {
			teamRepository.save(user.getTeam());
			List<User> users = userRepository.findAllByTeam(user.getTeam());
			if (users.size() < 10) {
				return userRepository.save(user);

			} else {
				throw new ServiceException(
						"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
			}
		} else
			throw new ServiceException("Username must be atleast 10 characters long!");
	}

	@Transactional
	public User deactivateUser(User user) {
		user.setStatus(Status.INACTIVE);
		List<WorkItem> workItems = workItemRepository.findAllByUser(user);
		for (WorkItem workItem : workItems) {
			workItem.setStatus(WorkItemStatus.Unstarted.toString());
		}
		return userRepository.save(user);
	}

	@Transactional
	public User activateUser(User user) {
		user.setStatus(Status.ACTIVE);
		return userRepository.save(user);
	}

}