package cz.muni.fi.resource;

import cz.muni.fi.dto.CategoryDTO;
import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.dto.ProductCreateDTO;
import cz.muni.fi.dto.ProductDTO;
import cz.muni.fi.entity.Price;
import cz.muni.fi.entity.Product;
import cz.muni.fi.enums.Currency;
import cz.muni.fi.exception.EshopServiceException;
//import cz.muni.fi.feign.CategoryInterface;
import cz.muni.fi.repository.ProductRepository;
import cz.muni.fi.service.BeanMappingService;
import cz.muni.fi.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * REST Controller for Products
 */
@Path("/products") //TODO remove this after adding API GATEWAY
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    final static Logger logger = LoggerFactory.getLogger(ProductResource.class);

    @Inject
    private ProductService productService;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private BeanMappingService beanMappingService;

//    @Inject
//    private CategoryInterface categoryInterface;

    /**
     * returns all products
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/products
     *
     * @return list of all products
     */
//    @GET
//    public Response getProducts() {
//        logger.debug("rest getProducts()");
//
//        List<ProductDTO> productDTOs = new ArrayList<>();
//        for (Product product : productRepository.findAll().list()) {
//            ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
//            productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
//            productDTOs.add(productDTO);
//        }
//        return Response.ok(productDTOs).build();
//    }

    /**
     * returns the product with the given id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/products/1
     *
     * @param id of the product
     * @return product with given id, 404 if product with given id doesn't exist
     */
//    @GET
//    @Path("/{id}")
//    public Response getProduct(long id) {
//        logger.debug("rest getProduct({})", id);
//
//        Product product = productRepository.findById(id);
//        if (product != null) {
//            ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
//            productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
//            return Response.ok(productDTO).build();
//        } else {
//            return Response.status(404,"The requested resource was not found").build();
//        }
//    }

    /**
     * deletes a product with the given id
     * e.g.: curl -i -X DELETE http://localhost:8080/eshop-rest/products/11
     *
     * @param id of the product, 404 if product with given id doesn't exist
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(long id) {
        logger.debug("rest deleteProduct({})", id);

        if (productRepository.findById(id) != null) {
            productRepository.deleteById(id);
        } else {
            return Response.status(404,"The requested resource was not found").build();
        }
        return Response.ok().build();
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
//    @POST
//    @Path("/create")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response createProduct(ProductCreateDTO productInfo) {
//        logger.debug("rest createProduct()");
//
//        try {
//            Product product = beanMappingService.mapTo(productInfo, Product.class);
//            Price price = new Price();
//            price.setValue(productInfo.getPrice());
//            price.setCurrency(productInfo.getCurrency());
//            Date now = new Date();
//            price.setPriceStart(now);
//            product.setAddedDate(now);
//            product.setCurrentPrice(price);
//            product.addHistoricalPrice(price);
//            product.addCategoryId(productInfo.getCategoryId());
//            product = productService.createProduct(product);
//
//            ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
//            productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
//            return Response.ok(productDTO).build();
//        } catch (Exception ex) {
//            // we needed to throw the 422 exception here to reproduce behaviour of the original project
//            return Response.status(422,"the requested resource already exists").build();
//        }
//    }

    /**
     * update the price for one product
     * it is not allowed to change the price by more than 10%
     * e.g.: curl -X PUT -i -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' http://localhost:8080/eshop-rest/products/4
     *
     * @param id of product to be updated
     * @param newPrice NewPriceDTO with required fields for creation (value, and currency [available values: CZK, EUR, USD] can't be null)
     * @return the updated product, 500 if there is no product with given id or 406 if value of price is changed more than 10% or something else went wrong
     */
//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response changePrice(long id, NewPriceDTO newPrice) {
//        logger.debug("rest changePrice({})", id);
//
//        Product product = productRepository.findById(id);
//        if (product != null) {
//            return Response.status(500).build();
//        }
//        try {
//            productService.changePrice(product, newPrice);
//        } catch (EshopServiceException e) {
//            return Response.status(406).build();
//        }
//
//        product = productRepository.findById(id);
//        if (product != null) {
//            ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
//            productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
//            return Response.ok(productDTO).build();
//        } else {
//            return Response.status(406).build();
//        }
//    }

    /**
     * adds a new category to the product
     * e.g.: curl -X POST -i -H "Content-Type: application/json" --data '5' http://localhost:8080/eshop-rest/products/2/categories
     *
     * @param id of the product to be updated
     * @param categoryId id of existing category we want to add to the product
     *                 the original project used CategoryDTO, but parameter name was never used
     * @return the updated product as defined by ProductDTO, 406 if something went wrong
     */
//    @POST
//    @Path("/{id}/categories")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response addCategory(long id, long categoryId) {
//        logger.debug("rest addCategory({})", id);
//
//        try {
//            productService.addCategory(id, categoryId);
//            Product product = productRepository.findById(id);
//            if (product != null) {
//                ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
//                productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
//                return Response.ok(productDTO).build();
//            } else {
//                return Response.status(406).build();
//            }
//        } catch (Exception ex) {
//            return Response.status(406).build();
//        }
//    }

    /**
     * get the current price of the product with the given id
     * this method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays
     * e.g.: curl -i -X GET http://localhost:8080/products/2/currentPrice
     *
     * @param id of the product
     * @return current price of the product with the given id, 404 if product with given id doesn't exist
     */
    @GET
    @Path("/{id}/currentPrice")
    public Response getProductPriceByProductId(long id) {
        logger.debug("rest getProductPriceByProductId({})", id);

        Product product = productRepository.findById(id);
        if (product != null) {
            return Response.ok(product.getCurrentPrice()).build();
        } else {
            return Response.status(404, "The requested resource was not found").build();
        }
    }

    /**
     * get the currency rate for a given currency pair
     * this method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays
     * e.g.: curl -i -X GET http://localhost:8080/products/getCurrencyRate/CZK/EUR
     *
     * @param currency1 first currency of the pair [available values: CZK, EUR, USD]
     * @param currency2 second currency of the pair [available values: CZK, EUR, USD]
     * @return currency rate for given pair, 404 if given currency pair doesn't exist (see pairs in ProductServiceImpl)
     */
    @GET
    @Path("getCurrencyRate/{currency1}/{currency2}")
    public Response getCurrencyRate(Currency currency1, Currency currency2) {
        logger.debug("rest getCurrencyRate({}, {})", currency1, currency2);

        try {
            return Response.ok(productService.getCurrencyRate(currency1, currency2)).build();
        } catch (IllegalArgumentException e){
            return Response.status(404, "The requested resource was not found").build();
        }
    }

//    private Set<CategoryDTO> getCategoriesFromIds(Set<Long> categoriesId) {
//        Set<CategoryDTO> categories = new HashSet<>();
//        for (Long categoryId: categoriesId) {
//            categories.add(categoryInterface.getCategory(categoryId).getBody());
//        }
//        return categories;
//    }
}
