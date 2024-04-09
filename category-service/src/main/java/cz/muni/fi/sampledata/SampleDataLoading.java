package cz.muni.fi.sampledata;

import cz.muni.fi.entity.Category;
import cz.muni.fi.repository.CategoryRepository;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class SampleDataLoading {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);

    @Inject
    private CategoryRepository categoryRepository;

    private void createCategory(String name) {
        Category c = new Category();
        c.setName(name);
        categoryRepository.persist(c);
    }

    @Startup
    public void loadCategorySampleData() {
        createCategory("Food");
        createCategory("Office");
        createCategory("Flowers");
        createCategory("Toys");
        createCategory("Presents");

        log.info("Loaded eShop categories.");
    }
}
