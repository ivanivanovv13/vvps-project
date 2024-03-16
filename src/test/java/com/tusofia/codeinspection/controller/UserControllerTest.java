package com.tusofia.codeinspection.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tusofia.codeinspection.dto.UserDto;
import com.tusofia.codeinspection.model.Authority;
import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.repository.UserRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Gson gson;

    private MockMvc mvc;

    private UserDto userDto;

    private static final String EMAIL = "test@email.com";

    private static final String PASSWORD = "test";

    private User user;

    @BeforeEach
    void setUp() {
        userDto = createUserDto(EMAIL, PASSWORD);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test()
    void register_shouldReturn_statusOk() throws Exception {
        String json = this.gson.toJson(userDto);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    @After("register_shouldReturn_statusOk")
    void login_shouldReturn_statusOk() throws Exception {
        String json = this.gson.toJson(userDto);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void login_shouldReturn_statusUnauthorized() throws Exception {
        userDto.setEmail("wrong@email.com");
        String json = this.gson.toJson(userDto);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @After("register_shouldReturn_statusOk")
    void updateUser_shouldReturn_statusOk() throws Exception {
        String json = "{\n"
                + "    \"authority\":[\n"
                + "       {\n"
                + "        \"authority\":\"ADMIN\"\n"
                + "       }\n"
                + "    ]\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.patch("/admin/update-user/" + EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateUser_shouldReturn_statusNoFound() throws Exception {
        String json = "{\n"
                + "    \"authority\":[\n"
                + "       {\n"
                + "        \"authority\":\"ADMIN\"\n"
                + "       }\n"
                + "    ]\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.patch("/admin/update-user/wrong@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getAllUsers_shouldReturn_statusUnauthorized() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/admin/get-all-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @After("login_shouldReturn_statusOk()")
    void getUser_shouldReturn_statusOk() throws Exception {
        userRepository.save(createUser("test@abv.bg",PASSWORD,null));
        this.mvc
                .perform(MockMvcRequestBuilders.get("/admin/get-user/test@abv.bg")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @After("login_shouldReturn_statusOk()")
    void getUser_shouldReturn_statusNotFound() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/admin/get-user/wrong@email.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private UserDto createUserDto(String email, String password) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        return userDto;
    }

    private User createUser(String email, String password, Set<Authority> authorities) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setAuthority(authorities);
        return user;
    }
}
