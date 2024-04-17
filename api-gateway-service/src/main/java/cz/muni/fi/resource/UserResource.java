package cz.muni.fi.resource;

import cz.muni.fi.stork.UserClient;
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
 * REST Controller for Users
 * In every method I need to try to catch ClientWebApplicationException and check if it is not containing
 * some HTTP status code that we are returning, otherwise the real status code is hidden behind status code 500
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResource {
    final static Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Inject
    @RestClient
    private UserClient userClient;

    private final MyMessageParser myMessageParser = new MyMessageParser();

    /**
     * Returns all users
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users
     *
     * @return list of Users
     */
    @GET
    public Response getUsers() {
        logger.debug("rest getUsers()");

        Response response;
        try {
            response = userClient.getUsers();
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
     * Getting user according to id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users/1
     * 
     * @param id of the user
     * @return User with given id or 404
     */
    @GET
    @Path("{id}")
    public Response getUser(long id) {
        logger.debug("rest getUser({})", id);

        Response response;
        try {
            response = userClient.getUser(id);
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
