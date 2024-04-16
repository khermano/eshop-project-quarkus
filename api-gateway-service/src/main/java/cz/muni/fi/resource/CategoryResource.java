package cz.muni.fi.resource;

import cz.muni.fi.client.CategoryClient;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * REST Controller for Categories
 * In every method I need to check the response status if it is different from 200 and create a new Response to return,
 * otherwise I am getting 500 - ClientWebApplicationException with real HTTP status code and reason
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class CategoryResource {
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
        Response response = categoryClient.getCategories();

        if (response.getStatus() != 200) {
            return Response.status(response.getStatus(), response.getStatusInfo().getReasonPhrase()).build();
        }
        return response;
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
        Response response = categoryClient.getCategory(id);

        if (response.getStatus() != 200) {
            return Response.status(response.getStatus(), response.getStatusInfo().getReasonPhrase()).build();
        }
        return response;
    }
}
