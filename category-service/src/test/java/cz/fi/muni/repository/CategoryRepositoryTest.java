package cz.fi.muni.repository;

import cz.muni.fi.entity.Category;
import cz.muni.fi.repository.CategoryRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

@QuarkusTest
@Transactional
public class CategoryRepositoryTest {
	@Inject
	private CategoryRepository categoryRepository;
	
	@Test
	public void findAll(){
		Category cat1 = new Category();
		Category cat2 = new Category();
		cat1.setName("cat1");
		cat2.setName("cat2");
		
		categoryRepository.persist(cat1);
		categoryRepository.persist(cat2);
		
		List<Category> categories  = categoryRepository.findAll().list();

		Assertions.assertTrue(categories.contains(cat1));
		Assertions.assertTrue(categories.contains(cat2));

		categoryRepository.delete(cat1);
		categoryRepository.delete(cat2);
	}
	
	@Test
	public void nullCategoryNameNotAllowed(){
		Category cat = new Category();
		cat.setName(null);

		Assertions.assertThrows(ConstraintViolationException.class, () -> categoryRepository.persist(cat));
	}
	
	@Test
	public void nameIsUnique(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.persist(cat);

		Category cat2 = new Category();
		cat2.setName("Electronics");

		Assertions.assertThrows(org.hibernate.exception.ConstraintViolationException.class, () -> categoryRepository.persist(cat2));
	}
	
	@Test()
	public void savesName(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.persist(cat);

		Assertions.assertNotNull(categoryRepository.findByName("Electronics"));
		Assertions.assertEquals(categoryRepository.findByName("Electronics").getName(), "Electronics");

		categoryRepository.delete(cat);
	}

	@Test()
	public void delete(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.persist(cat);

		Assertions.assertNotNull(categoryRepository.findById(cat.getId()));
		categoryRepository.delete(cat);
		Assertions.assertNull(categoryRepository.findById(cat.getId()));
	}
}
