package cz.muni.fi.sampledata;

import cz.muni.fi.repository.OrderRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests data loading.
 */
@QuarkusTest
@Transactional
public class SampleDataLoadingTest {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingTest.class);

    @Inject
    private OrderRepository orderRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        Assertions.assertFalse(orderRepository.findAll().list().isEmpty(), "No orders");
    }
}
