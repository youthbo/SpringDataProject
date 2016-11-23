package se.plushogskolan.sdj.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

import se.plushogskolan.sdj.config.TestConfiguration;
import se.plushogskolan.sdj.model.Team;
import se.plushogskolan.sdj.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@Transactional
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	@Autowired
	TeamService teamService;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before 
	public void setUp(){
		Team team = new Team("team 1");
		team = teamService.createTeam(team);
		for (int i = 0; i < 2; i++) {
			String firstname = "firstname"+i;
			String lastname = "lastname"+i;
			String username = "username0"+i;
			User user = new User(firstname, lastname, username,team);
			userService.createUser(user);
		}
	}
	
	@Test
	public void canAddUser(){
		User user = new User("firstname3", "lastname3", "username03",teamService.findByName("team 1"));
	    user=userService.createUser(user);
	    assertNotNull(user);	
	    
	}
	
	@Test 
	public void usernameShouldHasMoreThan10Char(){
		thrown.expect(ServiceException.class);
	    thrown.expectMessage("Username must be at least 10 characters long!");
		User user = new User("firstname3", "lastname3", "username3",teamService.findByName("team 1"));
	    user=userService.createUser(user);
	}
	
	@Test
	public void canFindAllUsers() {	
		assertEquals(userService.findAllUsers(0,10).size(),2);
		
	}
	
	@After
	public void clean(){
		
	}
	

}
