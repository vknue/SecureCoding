package hr.algebra.glowlog.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hr.algebra.glowlog.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.glowlog.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = true)
public class SerializationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSaveUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.USER);

        mockMvc.perform(post("/api/serialization/save-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateBadFile() throws Exception {
        mockMvc.perform(get("/api/serialization/create-bad-file"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoadUser() throws Exception {
        mockMvc.perform(get("/api/serialization/load-user"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidBinaryBlocked() throws Exception {
        mockMvc.perform(get("/api/serialization/test-invalid-binary"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSsrfProtection() throws Exception {
        mockMvc.perform(post("/api/serialization/test-ssrf")
                        .param("url", "http://google.com"))
                .andExpect(status().isOk());
    }
}