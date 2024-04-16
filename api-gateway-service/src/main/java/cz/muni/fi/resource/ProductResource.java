package cz.muni.fi.resource;

import cz.muni.fi.client.ProductClient;
import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.dto.ProductCreateDTO;
import cz.muni.fi.enums.Currency;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * REST Controller for Products
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class ProductResource {
    @RestClient
    private ProductClient productClient;

    /**
     * returns all products
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/products
     *
     * @return list of all products
     */
    @GET
    public Response getProducts() {
        return productClient.getProducts();
    }

    /**
     * returns the product with the given id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/products/1
     *
     * @param id of the product
     * @return product with given id, 404 if product with given id doesn't exist
     */
    @GET
    @Path("/{id}")
    public Response getProduct(long id) {
        return productClient.getProduct(id);
    }

    /**
     * deletes a product with the given id
     * e.g.: curl -i -X DELETE http://localhost:8080/eshop-rest/products/11
     *
     * @param id of the product, 404 if product with given id doesn't exist
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(long id) {
        return productClient.deleteProduct(id);
    }

    /**
     * create a new product
     * e.g.: curl -X POST -i -H "Content-Type: application/json" --data
     * '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}'
     * http://localhost:8080/eshop-rest/products/create
     * 
     * @param productInfo ProductCreateDTO with required fields for creation
     *                    (name, price, currency, categoryId can't be null)
     *                    you either fill both image and imageType or none of them (see @AllOrNothing)
     * @return the created product or 422 when invalid data provided or anything went wrong
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductCreateDTO productInfo) {
        return productClient.createProduct(productInfo);
    }

    /**
     * update the price for one product
     * it is not allowed to change the price by more than 10%
     * e.g.: curl -X PUT -i -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' http://localhost:8080/eshop-rest/products/4
     *
     * @param id of product to be updated
     * @param newPrice NewPriceDTO with required fields for creation (value, and currency [available values: CZK, EUR, USD] can't be null)
     * @return the updated product, 500 if there is no product with given id or 406 if value of price is changed more than 10% or something else went wrong
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePrice(long id, NewPriceDTO newPrice) {
        return productClient.changePrice(id, newPrice);
    }

    /**
     * adds a new category to the product
     * e.g.: curl -X POST -i -H "Content-Type: application/json" --data '5' http://localhost:8080/eshop-rest/products/2/categories
     *
     * @param id of the product to be updated
     * @param categoryId id of existing category we want to add to the product
     *                 the original project used CategoryDTO, but parameter name was never used
     * @return the updated product as defined by ProductDTO, 406 if something went wrong
     */
    @POST
    @Path("/{id}/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCategory(long id, long categoryId) {
        return productClient.addCategory(id, categoryId);
    }

    /**
     * get the current price of the product with the given id
     * this method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/products/2/currentPrice
     *
     * @param id of the product
     * @return current price of the product with the given id, 404 if product with given id doesn't exist
     */
    @GET
    @Path("/{id}/currentPrice")
    public Response getProductPriceByProductId(long id) {
        return productClient.getProductPriceByProductId(id);
    }

    /**
     * get the currency rate for a given currency pair
     * this method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR
     *
     * @param currency1 first currency of the pair [available values: CZK, EUR, USD]
     * @param currency2 second currency of the pair [available values: CZK, EUR, USD]
     * @return currency rate for given pair, 404 if given currency pair doesn't exist (see pairs in ProductServiceImpl)
     */
    @GET
    @Path("getCurrencyRate/{currency1}/{currency2}")
    public Response getCurrencyRate(Currency currency1, Currency currency2) {
        return productClient.getCurrencyRate(currency1, currency2);
    }
}
