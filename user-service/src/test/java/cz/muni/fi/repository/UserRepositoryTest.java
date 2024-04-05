package cz.muni.fi.repository;

import cz.muni.fi.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

@QuarkusTest
@Transactional
public class UserRepositoryTest {
	@Inject
	private UserRepository userRepository;

	private User u1;
	private User u2;

	@BeforeEach
	public void createUsers() {
		u1 = new User();
		u2 = new User();

		u1.setGivenName("Filip");
		u1.setEmail("filip@fi.cz");
		u1.setJoinedDate(new Date());
		u1.setSurname("Filipovic");
		u1.setAddress("Brno");

		u2.setGivenName("Jirka");
		u2.setEmail("jirka@fi.cz");
		u2.setJoinedDate(new Date());
		u2.setSurname("Jirkovic");
        u2.setAddress("Praha");

		userRepository.persist(u1);
		userRepository.persist(u2);
	}
	@AfterEach
	public void deleteUsers() {
		userRepository.delete(u1);
		userRepository.delete(u2);
	}

	@Test
	public void findByEmail() {
		Assertions.assertNotNull(userRepository.findByEmail("filip@fi.cz"));
	}

	@Test
	public void findByNonExistentEmail() {
		Assertions.assertNull(userRepository.findByEmail("asdfasdfasd"));
	}
}