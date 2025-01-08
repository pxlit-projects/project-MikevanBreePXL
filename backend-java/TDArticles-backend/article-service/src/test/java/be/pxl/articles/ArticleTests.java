package be.pxl.articles;

import be.pxl.articles.domain.Article;
import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.repository.ArticleRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ComponentScan(basePackages = "be.pxl.articles")
public class ArticleTests {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ArticleRepository articleRepository;
    @Container
    private static final MariaDBContainer<?> mariaDbContainer = new MariaDBContainer<>("mariadb:latest");
    private static CreateArticleRequest createArticleRequest;

    @DynamicPropertySource
    static void registerMySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDbContainer::getUsername);
        registry.add("spring.datasource.password", mariaDbContainer::getPassword);
    }

    @BeforeEach
    public void init() {
        createArticleRequest = CreateArticleRequest.builder()
                .title("Test Title")
                .content("Test content lorem ipsum")
                .concept(false)
                .author("Auther The Testeth")
                .build();
    }

    @Test
    public void testCreateArticle() throws Exception {
        String json = objectMapper.writeValueAsString(createArticleRequest);

        int articleCount = articleRepository.findAll().size();

        mockMvc.perform(post("/article/create")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isCreated());

        assertEquals(articleCount + 1, articleRepository.findAll().size());
    }

    @Test
    public void testGetArticle() throws Exception {
        String json = objectMapper.writeValueAsString(createArticleRequest);

        MvcResult result = mockMvc.perform(post("/article/create")
                .contentType("application/json")
                .content(json)).andReturn();

        mockMvc.perform(get(Objects.requireNonNull(result.getResponse().getRedirectedUrl()))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPublishedArticles() throws Exception {
        String json = objectMapper.writeValueAsString(createArticleRequest);

        mockMvc.perform(post("/article/create")
                .contentType("application/json")
                .content(json)).andReturn();


        MvcResult result = mockMvc.perform(get("/article")
                .contentType("application/json"))
                .andReturn();

        Article[] articles = objectMapper.readValue(result.getResponse().getContentAsString(), Article[].class);

        assertFalse(articles[0].isConcept());
    }

    @Test
    public void testEditArticleWhenConcept() throws Exception {
        createArticleRequest.setConcept(true);
        String json = objectMapper.writeValueAsString(createArticleRequest);

        MvcResult result = mockMvc.perform(post("/article/create")
                .contentType("application/json")
                .content(json)).andReturn();

        mockMvc.perform(put(result.getResponse().getRedirectedUrl() + "/edit")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testEditArticleInConceptFailsWhenAlreadyPublished() throws Exception {
        String json = objectMapper.writeValueAsString(createArticleRequest);

        MvcResult result = mockMvc.perform(post("/article/create")
                .contentType("application/json")
                .content(json)).andReturn();

        createArticleRequest.setConcept(true);
        json = objectMapper.writeValueAsString(createArticleRequest);
        mockMvc.perform(put(result.getResponse().getRedirectedUrl() + "/edit")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is4xxClientError());
    }
}
