package be.pxl.articles;

import be.pxl.articles.domain.api.CreatePostRequest;
import be.pxl.articles.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ComponentScan(basePackages = "be.pxl.articles")
public class PostTests {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private PostRepository postRepository;
    @Container
    private static final MariaDBContainer<?> mariaDbContainer = new MariaDBContainer<>("mariadb:latest");
    private static CreatePostRequest createPostRequest;

    @DynamicPropertySource
    static void registerMySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDbContainer::getUsername);
        registry.add("spring.datasource.password", mariaDbContainer::getPassword);
    }

    @BeforeEach
    public void init() {
        createPostRequest = CreatePostRequest.builder()
                .title("Test Title")
                .content("Test content lorem ipsum")
                .concept(false)
                .author("Auther The Testeth")
                .build();
    }

    @Test
    public void testCreatePost() throws Exception {
        String json = objectMapper.writeValueAsString(createPostRequest);

        int postCount = postRepository.findAll().size();

        mockMvc.perform(post("/posts/create")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isCreated());

        assertEquals(postCount + 1, postRepository.findAll().size());
    }

    @Test
    public void testGetPost() throws Exception {
        String json = objectMapper.writeValueAsString(createPostRequest);

        MvcResult result = mockMvc.perform(post("/posts/create")
                .contentType("application/json")
                .content(json)).andReturn();

        mockMvc.perform(get(Objects.requireNonNull(result.getResponse().getRedirectedUrl()))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditPostWhenConcept() throws Exception {
        createPostRequest.setConcept(true);
        String json = objectMapper.writeValueAsString(createPostRequest);

        MvcResult result = mockMvc.perform(post("/posts/create")
                .contentType("application/json")
                .content(json)).andReturn();

        mockMvc.perform(put(result.getResponse().getRedirectedUrl() + "/edit")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testEditPostInConceptFailsWhenAlreadyPublished() throws Exception {
        String json = objectMapper.writeValueAsString(createPostRequest);

        MvcResult result = mockMvc.perform(post("/posts/create")
                .contentType("application/json")
                .content(json)).andReturn();

        createPostRequest.setConcept(true);
        json = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(put(result.getResponse().getRedirectedUrl() + "/edit")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is4xxClientError());
    }
}
