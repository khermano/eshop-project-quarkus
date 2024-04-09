package cz.muni.fi.resource;

import cz.muni.fi.entity.Category;
import cz.muni.fi.repository.CategoryRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * REST Controller for Categories
 */
@Path("/categories") //TODO remove this after adding API GATEWAY
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {
    final static Logger logger = LoggerFactory.getLogger(CategoryResource.class);

    @Inject
    private CategoryRepository categoryRepository;

    /**
     * Get list of Categories
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/categories
     *
     * @return list of Categories
     */
    @GET
    public List<Category> getCategories() {
        logger.debug("rest getCategories()");

        return categoryRepository.findAll().list();
    }

    /**
     * Get Category specified by ID
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/categories/1
     * 
     * @param id identifier for the category
     * @return Category with given ID
     */
    @GET
    @Path("{id}")
    public Category getCategory(long id) {
        logger.debug("rest getCategory({})", id);

        Category category = categoryRepository.findById(id);
        if (category != null) {
            return category;
        } else {
            throw new NotFoundException("The requested resource was not found");
        }
    }
}
