package com.example.subscription;

import com.example.subscription.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateUniqueEmail() {
        return "user+" + UUID.randomUUID() + "@example.com";
    }

    @Test
    void testCreateAndGetUser() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail(generateUniqueEmail());

        String json = objectMapper.writeValueAsString(user);

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andReturn().getResponse().getContentAsString();

        Long userId = ((Number) objectMapper.readValue(response, Map.class).get("id")).longValue();

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("user+" + UUID.randomUUID() + "@example.com");
        user.setSubscriptions(new ArrayList<>());

        String json = objectMapper.writeValueAsString(user);

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readValue(response, Map.class)
                .get("id") instanceof Integer
                ? ((Integer) ((Map<?, ?>) objectMapper.readValue(response, Map.class)).get("id")).longValue()
                : (Long) ((Map<?, ?>) objectMapper.readValue(response, Map.class)).get("id");

        User updatedUser = new User();
        updatedUser.setName("New Name");
        updatedUser.setEmail("user+" + UUID.randomUUID() + "@example.com");
        updatedUser.setSubscriptions(new ArrayList<>());

        mockMvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void testDeleteUser() throws Exception {

        User user = new User();
        user.setName("To Delete");
        user.setEmail("user+" + UUID.randomUUID() + "@example.com");
        user.setSubscriptions(new ArrayList<>()); // обязательно, чтобы избежать cascade-ошибки

        String json = objectMapper.writeValueAsString(user);

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readValue(response, Map.class)
                .get("id") instanceof Integer
                ? ((Integer) ((Map<?, ?>) objectMapper.readValue(response, Map.class)).get("id")).longValue()
                : (Long) ((Map<?, ?>) objectMapper.readValue(response, Map.class)).get("id");

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound());
    }
}
