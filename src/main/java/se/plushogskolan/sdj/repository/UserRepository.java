package se.plushogskolan.sdj.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import se.plushogskolan.sdj.model.Team;
import se.plushogskolan.sdj.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	User findByUsername(String username);
	List<User> findByFirstname(String firstname);
	List<User> findByLastname(String lastname);
	List<User> findAllByTeam(Team team);

}
