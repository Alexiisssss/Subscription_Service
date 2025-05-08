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

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateEmail() {
        return "user+" + UUID.randomUUID() + "@example.com";
    }

    @Test
    void testAddAndGetSubscription() throws Exception {
        User user = new User();
        user.setName("Test Sub");
        user.setEmail(generateEmail());

        String userJson = objectMapper.writeValueAsString(user);

        String userResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = ((Number) objectMapper.readValue(userResponse, Map.class).get("id")).longValue();

        String subJson = "{\"name\": \"Netflix\"}";

        String subResponse = mockMvc.perform(post("/users/" + userId + "/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Netflix"))
                .andReturn().getResponse().getContentAsString();

        Long subId = ((Number) objectMapper.readValue(subResponse, Map.class).get("id")).longValue();

        mockMvc.perform(get("/users/" + userId + "/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Netflix"));

        mockMvc.perform(delete("/users/" + userId + "/subscriptions/" + subId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/" + userId + "/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}
