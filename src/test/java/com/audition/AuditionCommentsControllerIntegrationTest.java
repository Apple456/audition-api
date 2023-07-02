package com.audition;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditionCommentsControllerIntegrationTest extends BaseIntegrationTest {

    //### path: /comments?postId=12&page=0&size=1
    @SneakyThrows
    @Test
    void shouldFailForWrongMediaType() {
        verifyGet(COMMENTS, MediaType.APPLICATION_ATOM_XML, HttpStatus.NOT_ACCEPTABLE);
    }

    @SneakyThrows
    @Test
    void shouldFailForWrongVerb() {
        // should be GET
        verifyPost(COMMENTS, MediaType.APPLICATION_JSON, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @SneakyThrows
    @Test
    void shouldFailForWrongRequest() {
        // negative id
        verifyGet(COMMENTS + "?postId=-1&page=1&size=1", MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @Test
    void shouldFailForNegativePageSize() {
        // negative page size
        verifyGet(COMMENTS + "?postId=1&page=1&size=-1", MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @Test
    void shouldFailForNegativePage() {
        // negative page
        verifyGet(COMMENTS + "?postId=1&page=-1&size=1", MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @Test
    void shouldReturnPostForUser() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(COMMENTS + "?postId=111&page=0&size=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verifyComment(true, result.getResponse().getContentAsString(), 46,
                "Jeremy.Harann@waino.me",
                "dignissimos et deleniti voluptate et quod",
                "exercitationem et id quae cum omnis");
    }

    //### path: /comments/{id}

    @SneakyThrows
    @Test
    void shouldReturnPostById() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(COMMENTS + "/777")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verifyComment(false, result.getResponse().getContentAsString(), 11,
                "Jayne_Kuhic@sydney.com",
                "quo vero reiciendis velit similique earum",
                "est natus enim nihil est dolore omnis");
    }

    @SneakyThrows
    @Test
    void shouldReturnErrorPostById() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(COMMENTS + "/888")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(500);
        final var problemDetail = problemDetailFromString(result.getResponse().getContentAsString());
        assertThat(problemDetail.getTitle()).isEqualTo("API Error Occurred");
        assertThat(problemDetail.getDetail()).contains("Server returned HTTP response code: 500");
        assertThat(problemDetail.getInstance()).isEqualTo(URI.create("/comments/888"));
    }


    @SneakyThrows
    @Test
    void shouldReturnErrorPostForUser() {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(COMMENTS + "?postId=1111&page=0&size=1") // stub missing
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();


        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        final var problemDetail = problemDetailFromString(result.getResponse().getContentAsString());
        assertThat(problemDetail.getTitle()).isEqualTo("Client error");
        assertThat(problemDetail.getDetail()).isEqualTo("Not Found");
        assertThat(problemDetail.getInstance()).isEqualTo(URI.create("/comments"));
    }
}