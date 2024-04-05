package cz.muni.fi.sampledata;

import cz.muni.fi.entity.User;
import cz.muni.fi.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@QuarkusTest
@Transactional
public class SampleDataLoadingTest {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingTest.class);

    @Inject
    private UserRepository userRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        List<User> users = userRepository.findAll().list();
        Assertions.assertFalse(users.isEmpty());
    }
}
