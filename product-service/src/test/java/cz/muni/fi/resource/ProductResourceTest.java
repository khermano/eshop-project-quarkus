package cz.muni.fi.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.dto.ProductCreateDTO;
import cz.muni.fi.dto.ProductDTO;
import cz.muni.fi.entity.Price;
import cz.muni.fi.entity.Product;
import cz.muni.fi.enums.Color;
import cz.muni.fi.enums.Currency;
import cz.muni.fi.repository.ProductRepository;
import cz.muni.fi.service.BeanMappingService;
import cz.muni.fi.service.ProductService;
import cz.muni.fi.client.CategoryClient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class ProductResourceTest {
	@InjectMock
	private ProductService productService;

	@InjectMock
	private ProductRepository productRepository;

	@Mock
	private BeanMappingService beanMappingService;

    private CategoryClient categoryClient;

	@Mock
	private PanacheQuery panacheQuery;

	@Inject
	private ProductResource productResource;

	private static List<Product> products;

	private static List<ProductDTO> productDTOs;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
    }

	@BeforeAll
	public static void productsAndDtoSetup() {
		Product productOne = new Product();
		productOne.setId(10L);
		productOne.setName("Raspberry PI");
		Price currentPrice = new Price();
		currentPrice.setCurrency(Currency.EUR);
		currentPrice.setPriceValue(new BigDecimal("34"));
		productOne.setCurrentPrice(currentPrice);
		productOne.setColor(Color.BLACK);
		productOne.addCategoryId(1L);

		Product productTwo = new Product();
		productTwo.setId(20L);
		productTwo.setName("Arduino");
		Price price = new Price();
		price.setCurrency(Currency.EUR);
		price.setPriceValue(new BigDecimal("44"));
		productTwo.setCurrentPrice(price);
		productTwo.setColor(Color.WHITE);
		productTwo.addCategoryId(1L);

		products = List.of(productOne, productTwo);

		ProductDTO mockedProductDTO = new ProductDTO();
		mockedProductDTO.setId(10L);
		mockedProductDTO.setName("Raspberry PI");

		ProductDTO mockedProductDTO2 = new ProductDTO();
		mockedProductDTO2.setId(20L);
		mockedProductDTO2.setName("Arduino");

		productDTOs = List.of(mockedProductDTO, mockedProductDTO2);
	}

	@Test
	public void getAllProducts() {
		doReturn(panacheQuery).when(productRepository).findAll();
		doReturn(products).when(panacheQuery).list();
		doReturn(productDTOs.get(0)).when(beanMappingService).mapTo(products.get(0), ProductDTO.class);
		doReturn(productDTOs.get(1)).when(beanMappingService).mapTo(products.get(1), ProductDTO.class);

		given()
				.when().get("/products")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("id", hasItems(10, 20))
				.body("name", hasItems("Raspberry PI", "Arduino"));
	}

	@Test
	public void getValidProduct() throws Exception {
		doReturn(products.get(0)).when(productRepository).findById(10L);
		doReturn(products.get(1)).when(productRepository).findById(20L);
		doReturn(productDTOs.get(0)).when(beanMappingService).mapTo(products.get(0), ProductDTO.class);
		doReturn(productDTOs.get(1)).when(beanMappingService).mapTo(products.get(1), ProductDTO.class);

		given()
				.when().get("/products/10")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("name", is("Raspberry PI"));

		given()
				.when().get("/products/20")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("name", is("Arduino"));
	}

	@Test
	public void getInvalidProduct() throws Exception {
		doReturn(null).when(productRepository).findById(1L);

		given()
				.when().get("/products/1")
				.then()
				.statusCode(404);
	}

	@Test
	public void deleteProduct() throws Exception {
		doReturn(new Product()).when(productRepository).findById(10L);

		given()
				.when().delete("/products/10")
				.then()
				.statusCode(200);
	}

	@Test
	public void deleteProductNonExisting() throws Exception {
		doReturn(null).when(productRepository).findById(20L);

		given()
				.when().delete("/products/20")
				.then()
				.statusCode(404);
	}

	@Test
	public void createProduct() throws Exception {
		ProductCreateDTO productCreateDTO = new ProductCreateDTO();
		productCreateDTO.setPrice(BigDecimal.valueOf(200));
		productCreateDTO.setCurrency(Currency.CZK);
		productCreateDTO.setCategoryId(1L);

		Product mockedProduct = new Product();
		mockedProduct.setId(1L);
		mockedProduct.addCategoryId(1L);

		doReturn(mockedProduct).when(beanMappingService).mapTo(productCreateDTO, Product.class);
		doReturn(mockedProduct).when(productService).createProduct(any(Product.class));
		doReturn(new ProductDTO()).when(beanMappingService).mapTo(mockedProduct, ProductDTO.class);

		given()
				.body(convertObjectToJsonBytes(productCreateDTO))
				.contentType(ContentType.JSON)
				.when().post("/products/create")
				.then()
				.statusCode(200);
	}

	@Test
	public void updateProduct() throws Exception {
		Product product = new Product();
		product.addCategoryId(1L);

		doReturn(product).when(productRepository).findById(10L);
		doNothing().when(productService).changePrice(any(Product.class), any(NewPriceDTO.class));
		doReturn(new ProductDTO()).when(beanMappingService).mapTo(product, ProductDTO.class);

		given()
				.body(convertObjectToJsonBytes(new Price()))
				.contentType(ContentType.JSON)
				.when().put("/products/10")
				.then()
				.statusCode(200);
	}

	@Test
	public void addCategory() throws Exception {
		Product mockedProduct = new Product();
		mockedProduct.setId(10L);
		mockedProduct.addCategoryId(1L);

		doReturn(mockedProduct).when(productRepository).findById(10L);
		doReturn(new ProductDTO()).when(beanMappingService).mapTo(mockedProduct, ProductDTO.class);

		given()
				.body(convertObjectToJsonBytes(1L))
				.contentType(ContentType.JSON)
				.when().post("/products/10/categories")
				.then()
				.statusCode(200);
	}

	private static String convertObjectToJsonBytes(Object object)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}
}
