package cz.muni.fi.resource;

import cz.muni.fi.entity.User;
import cz.muni.fi.repository.UserRepository;
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
 * REST Controller for Users
 */
@Path("/users") //TODO remove this after adding API GATEWAY
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResource {
    final static Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    /**
     * Returns all users
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users
     *
     * @return list of Users
     */
    @GET
    public Response getUsers() {
        logger.debug("rest getUsers()");

        return Response.ok(userRepository.findAll().list()).build();
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

        User user = userRepository.findById(id);
        if (user != null) {
            return Response.ok(user).build();
        }
        else {
            return Response.status(404,"The requested resource was not found").build();
        }
    }
}
