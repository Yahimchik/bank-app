package com.example.bankapp.controller;

import com.example.bankapp.dto.email.EmailDataRequestDTO;
import com.example.bankapp.dto.email.EmailDataUpdateRequestDTO;
import com.example.bankapp.entities.User;
import com.example.bankapp.security.UserDetailsServiceImpl;
import com.example.bankapp.security.UserPrincipal;
import com.example.bankapp.security.jwt.JwtTokenProvider;
import com.example.bankapp.service.EmailDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class EmailDataControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private EmailDataService emailDataService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .build();

        when(userDetailsService.loadUserById(testUser.getId()))
                .thenReturn(new UserPrincipal(testUser.getId(), "testUser", "password"));

        jwtToken = jwtTokenProvider.generateAccessToken(testUser);
    }

    @Test
    void testAddEmail() throws Exception {
        EmailDataRequestDTO request = new EmailDataRequestDTO();
        request.setEmail("test@example.com");

        mockMvc.perform(post("/api/v1/email")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(emailDataService).addEmail(eq(testUser.getId()), eq("test@example.com"));
    }

    @Test
    void testDeleteEmail() throws Exception {
        EmailDataRequestDTO request = new EmailDataRequestDTO();
        request.setEmail("test@example.com");

        mockMvc.perform(delete("/api/v1/email")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(emailDataService).deleteEmail(eq(testUser.getId()), eq("test@example.com"));
    }

    @Test
    void testUpdateEmail() throws Exception {
        EmailDataUpdateRequestDTO request = new EmailDataUpdateRequestDTO();
        request.setOldEmail("old@example.com");
        request.setNewEmail("new@example.com");

        mockMvc.perform(put("/api/v1/email")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(emailDataService).updateEmail(eq(testUser.getId()), eq("old@example.com"), eq("new@example.com"));
    }
}
