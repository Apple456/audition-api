package com.audition;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditionPostControllerIntegrationTest extends BaseIntegrationTest {
    
    //### path: /posts
    @SneakyThrows
    @Test
    void shouldFailForWrongMediaType() {
        verifyGet(POSTS, MediaType.APPLICATION_ATOM_XML, HttpStatus.NOT_ACCEPTABLE);
    }

    @SneakyThrows
    @Test
    void shouldFailForWrongVerb() {
        verifyPost(POSTS, MediaType.APPLICATION_JSON, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @SneakyThrows
    @Test
    void shouldFailForWrongRequest() {
        // negative userId
        verifyGet(POSTS + "?userId=-1", MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @Test
    void shouldFailForNegativePageSize() {
        // negative page size
        verifyGet(POSTS + "?userId=1&page=1&size=-1\"", MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @Test
    void shouldReturnAuditionPostForUser() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(POSTS + "?userId=222&page=0&size=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verifyAuditPost(true, result.getResponse().getContentAsString(), 222, 91, "sunt aut facere repellat",
                "libero voluptate eveniet aperiam sed");
    }
}