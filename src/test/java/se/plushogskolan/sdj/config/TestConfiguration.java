package se.plushogskolan.sdj.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import se.plushogskolan.sdj.service.TeamService;
import se.plushogskolan.sdj.service.UserService;

@Configuration
@EnableJpaRepositories("se.plushogskolan.sdj.repository")
@EnableTransactionManagement
@EnableJpaAuditing
public class TestConfiguration {
	@Bean
	public DataSource dataSource() {

		HikariConfig config = new HikariConfig();
		config.setDriverClassName("org.h2.Driver");
		config.setJdbcUrl("jdbc:h2:mem:datajpa");
        config.setUsername("root");
		config.setPassword("root");
		
		return new HikariDataSource(config);
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory factory) {
		return new JpaTransactionManager(factory);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setGenerateDdl(true);

		return adapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(jpaVendorAdapter());
		factory.setPackagesToScan("se.plushogskolan.sdj.model");

		return factory;
	}
	
	@Bean
	public TeamService teamService() {
        TeamService teamService = new TeamService();
        // set properties, etc.
        return teamService;
    }
	@Bean
	public UserService userService() {
        UserService userService = new UserService();
        // set properties, etc.
        return userService;
    }
}
