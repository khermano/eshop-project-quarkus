package cz.muni.fi.stork;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "stork://categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryClient {
    @GET
    Response getCategories();

    @GET
    @Path("{id}")
    Response getCategory(long id);
}
