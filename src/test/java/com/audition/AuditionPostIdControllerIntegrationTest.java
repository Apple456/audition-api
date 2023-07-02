package com.audition;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditionPostIdControllerIntegrationTest extends BaseIntegrationTest {

    //### path: /posts/{id}/comments
    @SneakyThrows
    @Test
    void shouldReturnAuditionPostIdWithComments() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(POSTS + "/333/comments?page=0&size=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        final var auditPost = verifyAuditPost(result.getResponse().getContentAsString(), 333, 333, "a quo magni similique perferendis",
                "alias dolor cumque\nimpedit blanditiis");
        assertThat(auditPost.getAuditionComments()).hasSize(1);
        final var comment = auditPost.getAuditionComments().get(0);
        assertThat(comment.getEmail()).isEqualTo("Vivienne@willis.org");
        assertThat(comment.getName()).isEqualTo("Vivienne");
        assertThat(comment.getBody()).contains("consequatur est aut qui earum nisi officiis sed culpa");
    }

    //### path: /posts/{id}/comments
    @SneakyThrows
    @Test
    void shouldFailForWrongRequestMissingPostIdComments() {
        // no post id
        verifyGet(POSTS + "/comments", MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @Test
    void shouldFailForWrongMediaTypeWithComments() {
        verifyGet(POSTS + "/1/comments", MediaType.APPLICATION_ATOM_XML, HttpStatus.NOT_ACCEPTABLE);
    }

    @SneakyThrows
    @Test
    void shouldReturnPostForUserPostIdComments() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(POSTS + "/444/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verifyAuditPost(result.getResponse().getContentAsString(), 4, 4444, "a quo magni similique perferendis",
                "alias dolor cumque");
    }

    //### path: /posts/{id}
    @SneakyThrows
    @Test
    void shouldFailForWrongMediaType() {
        verifyGet(POSTS + "/1", MediaType.APPLICATION_ATOM_XML, HttpStatus.NOT_ACCEPTABLE);
    }

    @SneakyThrows
    @Test
    void shouldFailForWrongVerb() {
        verifyPost(POSTS + "/1", MediaType.APPLICATION_JSON, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @SneakyThrows
    @Test
    void shouldFailForNegative() {
        verifyPost(POSTS + "/-1", MediaType.APPLICATION_JSON, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @SneakyThrows
    @Test
    void shouldReturnAuditionPost() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(POSTS + "/555")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verifyAuditPost(result.getResponse().getContentAsString(), 5, 5555, "a quo magni similique perferendis",
                "alias dolor cumque\nimpedit blanditiis");
    }
}