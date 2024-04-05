package cz.muni.fi.controller;

import cz.muni.fi.entity.User;
import cz.muni.fi.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;

/**
 * REST Controller for Users
 */
@Path("/users") //TODO remove this after adding API GATEWAY
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserRepository userRepository;

    /**
     * Returns all users
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users
     *
     * @return list of Users
     */
    @GET
    public Collection<User> getUsers() {
        logger.debug("rest getUsers()");

        return userRepository.findAll().list();
    }

    /**
     * Getting user according to id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users/1
     * 
     * @param id of the user
     * @return User with given id
     */
    @GET
    @Path("{id}")
    public RestResponse<User> getUser(long id) {
        logger.debug("rest getUser({})", id);

        User user = userRepository.findById(id);
        if (user != null) {
            return ResponseBuilder.ok(user).build();
        }
        else {
            throw new NotFoundException("The requested resource was not found");
        }
    }
}
