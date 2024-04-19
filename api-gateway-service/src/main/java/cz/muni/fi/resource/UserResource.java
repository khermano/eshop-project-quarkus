package cz.muni.fi.resource;

import cz.muni.fi.stork.UserClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * REST Controller for Users
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResource {
    @Inject
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
        return userClient.getUsers();
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
        return userClient.getUser(id);
    }
}
