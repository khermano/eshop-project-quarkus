package cz.muni.fi.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "stork://users")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public interface UserClient {
    @GET
    Response getUsers();

    @GET
    @Path("/{id}")
    Response getUser(long id);
}
