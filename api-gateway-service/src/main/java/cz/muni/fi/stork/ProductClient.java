package cz.muni.fi.stork;

import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.dto.ProductCreateDTO;
import cz.muni.fi.enums.Currency;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "stork://products")
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public interface ProductClient {
    @GET
    Response getProducts();

    @GET
    @Path("/{id}")
    Response getProduct(long id);

    @DELETE
    @Path("/{id}")
    Response deleteProduct(long id);

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createProduct(ProductCreateDTO productInfo);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response changePrice(long id, NewPriceDTO newPrice);

    @POST
    @Path("/{id}/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    Response addCategory(long id, long categoryId);

    @GET
    @Path("/{id}/currentPrice")
    Response getProductPriceByProductId(long id);

    @GET
    @Path("getCurrencyRate/{currency1}/{currency2}")
    Response getCurrencyRate(Currency currency1, Currency currency2);
}
