package cz.muni.fi.resource;

import cz.muni.fi.dto.CategoryDTO;
import cz.muni.fi.stork.CategoryClient;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Alternative()
@Priority(1)
@ApplicationScoped
@RestClient
public class MockCategoryClient implements CategoryClient {
    @Override
    public Response getCategory(long id) {
        return Response.ok(new CategoryDTO()).build();
    }
}
