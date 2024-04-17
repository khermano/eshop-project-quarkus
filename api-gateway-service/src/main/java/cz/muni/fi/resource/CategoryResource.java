package cz.muni.fi.resource;

import cz.muni.fi.stork.CategoryClient;
import cz.muni.fi.utils.MyMessageParser;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Controller for Categories
 * In every method I need to try to catch ClientWebApplicationException and check if it is not containing
 * some HTTP status code that we are returning, otherwise the real status code is hidden behind status code 500
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class CategoryResource {
    final static Logger logger = LoggerFactory.getLogger(CategoryResource.class);

    @Inject
    @RestClient
    private CategoryClient categoryClient;

    private final MyMessageParser myMessageParser = new MyMessageParser();

    /**
     * Get list of Categories
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/categories
     *
     * @return list of Categories
     */
    @GET
    public Response getCategories() {
        Response response;
        try {
            response = categoryClient.getCategories();
        } catch (ClientWebApplicationException e) {
            if (e.getMessage().contains("status code")) {
                logger.warn("There was ClientWebApplicationException: " + e.getMessage());
                return Response.status(myMessageParser.parseMessage(e.getMessage())).build();
            } else {
                throw e;
            }
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
        Response response;
        try {
            response = categoryClient.getCategory(id);
        } catch (ClientWebApplicationException e) {
            if (e.getMessage().contains("status code")) {
                logger.warn("There was ClientWebApplicationException: " + e.getMessage());
                return Response.status(myMessageParser.parseMessage(e.getMessage())).build();
            } else {
                throw e;
            }
        }
        return response;
    }
}
