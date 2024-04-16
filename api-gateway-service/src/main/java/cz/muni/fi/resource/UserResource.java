package cz.muni.fi.resource;

import cz.muni.fi.client.UserClient;
import cz.muni.fi.utils.Utils;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

/**
 * REST Controller for Users
 * In every method I need to try to catch ClientWebApplicationException and check if it is not containing
 * some HTTP status code that we are returning, otherwise the real status code is hidden behind status code 500
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResource {

    @RestClient
    private UserClient userClient;
    private final Utils utils = new Utils();

    /**
     * Returns all users
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users
     *
     * @return list of Users
     */
    @GET
    public Response getUsers() {
        Response response;

        try {
            response = userClient.getUsers();
        } catch (ClientWebApplicationException e) {
            if (e.getMessage().contains("status code")) {
                return Response.status(utils.parseMessage(e.getMessage())).build();
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
        Response response;

        try {
            response = userClient.getUser(id);
        } catch (ClientWebApplicationException e) {
            if (e.getMessage().contains("status code")) {
                return Response.status(utils.parseMessage(e.getMessage())).build();
            } else {
                throw e;
            }
        }
        return response;
    }
}
