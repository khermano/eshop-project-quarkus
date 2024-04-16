package cz.muni.fi.resource;

import cz.muni.fi.client.UserClient;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * REST Controller for Users
 * In every method I need to check the response status if it is different from 200 and create a new Response to return,
 * otherwise I am getting 500 - ClientWebApplicationException with real HTTP status code and reason
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResource {

    @RestClient
    private UserClient userClient;

    /**
     * Returns all users
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users
     *
     * @return list of Users
     */
    @GET
    public Response getUsers() {
        Response response = userClient.getUsers();

        if (response.getStatus() != 200) {
            return Response.status(response.getStatus(), response.getStatusInfo().getReasonPhrase()).build();
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
        Response response = userClient.getUser(id);

        if (response.getStatus() != 200) {
            return Response.status(response.getStatus(), response.getStatusInfo().getReasonPhrase()).build();
        }
        return response;
    }
}
