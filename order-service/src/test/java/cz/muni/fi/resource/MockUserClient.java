package cz.muni.fi.resource;

import cz.muni.fi.dto.UserDTO;
import cz.muni.fi.client.UserClient;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Alternative()
@Priority(1)
@ApplicationScoped
@RestClient
public class MockUserClient implements UserClient {
    @Override
    public Response getUser(long id) {
        return Response.ok(new UserDTO()).build();
    }
}
