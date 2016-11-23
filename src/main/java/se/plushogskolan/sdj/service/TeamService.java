package se.plushogskolan.sdj.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import se.plushogskolan.sdj.service.ServiceException;
import se.plushogskolan.sdj.model.Status;
import se.plushogskolan.sdj.model.Team;
import se.plushogskolan.sdj.model.User;
import se.plushogskolan.sdj.repository.TeamRepository;
import se.plushogskolan.sdj.repository.UserRepository;

@Service
public class TeamService {
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private UserRepository userRepository;

	public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
	}

	//used in testConfig file, for autowired in test
	public TeamService() {
	}

	@Transactional
	public Team createTeam(Team team) {
		try {
			if (team.getId() == null) {

				return teamRepository.save(team);

			} else {
				throw new ServiceException("Team with this teamname already exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not add team: " + team.getName(), e);
		}
	}

	public Iterable<Team> findAllTeams() {
			return teamRepository.findAll();
	}

	public Team findByName(String teamName) {
			Team team = teamRepository.findByName(teamName);
			return team;
	}

	@Transactional
	public void uppdateTeam(String oldName, String newName) {
		try {
			Team oldTeam = teamRepository.findByName(oldName);
			if (oldTeam != null) {
				Team newTeam = teamRepository.findByName(newName);
				if (newTeam == null) {
					oldTeam.setName(newName);
					teamRepository.save(oldTeam);
				} else {
					throw new ServiceException("Team with this teamname " + newName + " exists");

				}
			} else {
				throw new ServiceException("Team with this teamname: " + oldName + "NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not update team", e);
		}
	}

	@Transactional
	public void deactivateTeam(String teamName) {

		try {
			Team team = teamRepository.findByName(teamName);
			if (team != null) {
				team.setStatus(Status.INACTIVE.toString());
				teamRepository.save(team);
			} else {
				throw new ServiceException("Team with this teamname NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not deactivate team: " + teamName, e);
		}

	}

	@Transactional
	public void assigneUserToTeam(String teamName, Long userId) {
		try {
			Team team = teamRepository.findByName(teamName);
			User user = userRepository.findOne(userId);
			if ((team != null) && (user != null)) {
				List<User> users = userRepository.findAllByTeam(team);
				if (users.size() < 10) {
					user.setTeam(team);
					userRepository.save(user);

				} else {
					throw new ServiceException(
							"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
				}

			} else {
				throw new ServiceException("Team with this teamId NOT exists OR User NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not update team", e);
		}
	}

}
