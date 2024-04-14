package cz.muni.fi.resource;

import cz.muni.fi.entity.Category;
import cz.muni.fi.repository.CategoryRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Controller for Categories
 */
@Path("/categories") //TODO remove this after adding API GATEWAY
@Produces(MediaType.APPLICATION_JSON)
@Transactional
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
    public Response getCategories() {
        logger.debug("rest getCategories()");

        return Response.ok(categoryRepository.findAll().list()).build();
    }

    /**
     * Get Category specified by ID
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/categories/1
     * 
     * @param id identifier for the category
     * @return Category with given ID or 404
     */
    @GET
    @Path("{id}")
    public Response getCategory(long id) {
        logger.debug("rest getCategory({})", id);

        Category category = categoryRepository.findById(id);
        if (category != null) {
            return Response.ok(category).build();
        } else {
            return Response.status(404,"The requested resource was not found").build();
        }
    }
}
