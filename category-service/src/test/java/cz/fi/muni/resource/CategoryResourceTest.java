package cz.fi.muni.resource;

import cz.muni.fi.entity.Category;
import cz.muni.fi.repository.CategoryRepository;
import cz.muni.fi.resource.CategoryResource;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import org.mockito.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class CategoryResourceTest {
    @InjectMock
    private CategoryRepository categoryRepository;

    @Mock
    private PanacheQuery panacheQuery;

    @Inject
    private CategoryResource categoryResource;

    private static List<Category> categories;

    @BeforeAll
    public static void categoriesSetup() {
        Category catOne = new Category();
        catOne.setId(1L);
        catOne.setName("Electronics");

        Category catTwo = new Category();
        catTwo.setId(2L);
        catTwo.setName("Home Appliances");

        categories =  List.of(catOne, catTwo);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCategories() {
        doReturn(panacheQuery).when(categoryRepository).findAll();
        doReturn(categories).when(panacheQuery).list();

        given()
                .when().get("/categories")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItems(1, 2))
                .body("name", hasItems("Electronics", "Home Appliances"));
    }

    @Test
    public void getValidCategory() {
        doReturn(categories.get(0)).when(categoryRepository).findById(1L);
        doReturn(categories.get(1)).when(categoryRepository).findById(2L);

        given()
                .when().get("/categories/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("Electronics"));

        given()
                .when().get("/categories/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("Home Appliances"));
    }

    @Test
    public void getInvalidCategory() {
        doReturn(null).when(categoryRepository).findById(1L);

        given()
                .when().get("/categories/1")
                .then()
                .statusCode(404);
    }
}
