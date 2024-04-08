package cz.muni.fi.controller;

import cz.muni.fi.entity.User;
import cz.muni.fi.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.MockitoConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class UserResourceTest {

    @InjectMock
    private UserRepository userRepository;

    @Mock
    private PanacheQuery panacheQuery;

    @Inject
    @MockitoConfig(convertScopes = true)
    private UserResource usersController;

    private static List<User> users;

    @BeforeAll
    public static void usersSetup() {
        User userOne = new User();
        userOne.setId(1L);
        userOne.setGivenName("John");
        userOne.setSurname("Smith");

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setGivenName("Mary");
        userTwo.setSurname("Williams");

        users = List.of(userOne, userTwo);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers() {
        doReturn(panacheQuery).when(userRepository).findAll();
        doReturn(users).when(panacheQuery).list();


        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItems(1, 2))
                .body("surname", hasItems("Smith", "Williams"));
    }

    @Test
    public void getValidUser() {
        doReturn(users.get(0)).when(userRepository).findById(1L);
        doReturn(users.get(1)).when(userRepository).findById(2L);

        given()
                .when().get("/users/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("surname", is("Smith"));

        given()
                .when().get("/users/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("surname", is("Williams"));
    }

    @Test
    public void getInvalidUser() {
        doReturn(null).when(userRepository).findById(1L);

        given()
                .when().get("/users/1")
                .then()
                .statusCode(404);
    }
}