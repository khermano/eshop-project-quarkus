package cz.muni.fi.repository;

import cz.muni.fi.entity.Price;
import cz.muni.fi.entity.Product;
import cz.muni.fi.enums.Color;
import cz.muni.fi.enums.Currency;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@QuarkusTest
@Transactional
public class ProductRepositoryTest {
	@Inject
	public ProductRepository productRepository;

	@Inject
	public PriceRepository priceRepository;

	private Product p1;
	private Product p2;

	private Price priceLow;

	@BeforeEach
	public void createProducts() {
		p1 = new Product();
		p2 = new Product();

		priceLow = new Price();
		priceLow.setPriceStart(new Date());
		priceLow.setCurrency(Currency.CZK);
		priceLow.setValue(BigDecimal.TEN);

		priceRepository.persist(priceLow);

		p1.setName("p1");
		p2.setName("p2");

		p1.addCategoryId(1L);
		p1.setColor(Color.RED);
		p1.setCurrentPrice(priceLow);
		p2.addCategoryId(1L);

		productRepository.persist(p1);
		productRepository.persist(p2);
	}

	@AfterEach
	public void deleteProducts() {
		priceRepository.delete(priceLow);

		productRepository.delete(p1);
		productRepository.delete(p2);
	}


	@Test
	public void findAll() {
		List<Product> found = productRepository.findAll().list();
		Assertions.assertTrue(found.size() >= 2);
		Assertions.assertNotNull(p1);
		Assertions.assertTrue(found.contains(p1));
		Assertions.assertNotNull(p2);
		Assertions.assertTrue(found.contains(p2));
	}

	@Test
	public void findCategoryId() {
		Product found = productRepository.findById(p1.getId());
		Assertions.assertNotNull(found);
		Assertions.assertEquals(found.getCategoriesId().size(), 1);
		Assertions.assertEquals(found.getCategoriesId().iterator().next(), 1L);
	}


	@Test
	public void remove() {
		Product p3 = new Product();
		p3.setName("p3");
		productRepository.persist(p3);
		Long id = p3.getId();

		Assertions.assertNotNull(productRepository.findById(id));
		productRepository.delete(p3);
		Assertions.assertNull(productRepository.findById(id));
	}

	@Test
	public void find() {
		Product found = productRepository.findById(p1.getId());
		Assertions.assertNotNull(found);
		Assertions.assertEquals(found.getName(), "p1");
		Assertions.assertEquals(found.getColor(), Color.RED);
		Assertions.assertEquals(found.getCurrentPrice().getValue(), new BigDecimal("10.00"));
	}

	@Test
	public void mimeTypeCannotBeSetWithoutImage() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImageMimeType("X");
		Assertions.assertThrows(ConstraintViolationException.class, () -> productRepository.persist(p));
	}

	@Test
	public void imageCannotBeSetWithoutMimeType() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImage(new byte[] {});
		Assertions.assertThrows(ConstraintViolationException.class, () -> productRepository.persist(p));
	}

	@Test
	public void imageCanBeSetWithMimeType() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImageMimeType("X");
		p.setImage(new byte[] {});

		productRepository.persist(p);
		Assertions.assertNotNull(p.getId());
		productRepository.delete(p);
	}
}