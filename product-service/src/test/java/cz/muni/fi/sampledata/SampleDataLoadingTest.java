package cz.muni.fi.sampledata;

import cz.muni.fi.entity.Price;
import cz.muni.fi.entity.Product;
import cz.muni.fi.repository.ProductRepository;
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
    private ProductRepository productRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        List<Product> found = productRepository.findAll().list();
        Assertions.assertFalse(found.isEmpty(), "No products");

        Product product = productRepository.findById(1L);
        Assertions.assertNotNull(product);
        List<Price> priceHistory = product.getPriceHistory();
        Assertions.assertFalse(priceHistory.isEmpty(), "No prices for product 1");
    }
}
