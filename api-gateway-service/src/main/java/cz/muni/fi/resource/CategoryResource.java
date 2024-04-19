package cz.muni.fi.resource;

import cz.muni.fi.stork.CategoryClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * REST Controller for Categories
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class CategoryResource {
    @Inject
    @RestClient
    private CategoryClient categoryClient;

    /**
     * Get list of Categories
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/categories
     *
     * @return list of Categories
     */
    @GET
    public Response getCategories() {
        return categoryClient.getCategories();
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
        return categoryClient.getCategory(id);
    }
}
