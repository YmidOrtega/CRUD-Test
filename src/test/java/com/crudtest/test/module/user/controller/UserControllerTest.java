package com.crudtest.test.module.user.controller;

import com.crudtest.test.module.user.service.UserRegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.crudtest.test.module.user.dto.AuthUserDTO;
import com.crudtest.test.module.user.dto.UserDefaultDTO;
import com.crudtest.test.module.user.dto.UserProfileCompletionDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<AuthUserDTO> authUserDtoJson;

    @Autowired
    private JacksonTester<UserDefaultDTO> userDefaultDtoJson;

    @Autowired
    private JacksonTester<UserProfileCompletionDTO> userProfileCompletionDtoJson;

    @MockitoBean
    private UserController userController;

    @Test
    @DisplayName("Debería devolver creaded")
    void shouldReturnCreated() throws Exception {
        String authDTO = authUserDtoJson.write(new AuthUserDTO("test@gmail.com", "teste1234.")).getJson();

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authDTO))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @DisplayName("Debería devolver BadRequest cuando el email está vacío")
    void shouldReturnBadRequestWhenEmailIsEmpty() throws Exception {
        String authUserDTO = """
                {
                    "email": "",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authUserDTO))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}