package cz.muni.fi.controller;

import cz.muni.fi.entity.User;
import cz.muni.fi.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.MockitoConfig;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class UserControllerTest {

    @InjectMock
    private UserRepository userRepository;

    @Mock
    private PanacheQuery panacheQuery;

    @Inject
    @MockitoConfig(convertScopes = true)
    private UserController usersController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers() {

        doReturn(panacheQuery).when(userRepository).findAll();
        doReturn(Collections.unmodifiableList(this.createUsers())).when(panacheQuery).list();


        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("id", hasItems(1, 2))
                .body("surname", hasItems("Smith", "Williams"));
    }

//    @Test
//    public void getValidUser() throws Exception {
//        List<Optional<User>> users = this.createUsers();
//
//        doReturn(users.get(0)).when(userRepository).findById(1L);
//        doReturn(users.get(1)).when(userRepository).findById(2L);
//
//        mockMvc.perform(get("/1"))
//                .andExpect(status().isOk())
//                .andExpect(
//                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.surname").value("Smith"));
//
//        mockMvc.perform(get("/2"))
//                .andExpect(status().isOk())
//                .andExpect(
//                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.surname").value("Williams"));
//    }
//
//    @Test
//    public void getInvalidUser() throws Exception {
//        doReturn(Optional.empty()).when(userRepository).findById(1L);
//
//        mockMvc.perform(get("/1"))
//                .andExpect(status().is4xxClientError());
//    }

    private List<User> createUsers() {
        User userOne = new User();
        userOne.setId(1L);
        userOne.setGivenName("John");
        userOne.setSurname("Smith");

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setGivenName("Mary");
        userTwo.setSurname("Williams");
        
        return Arrays.asList(userOne, userTwo);
    }
}