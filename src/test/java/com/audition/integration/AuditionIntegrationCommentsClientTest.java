package com.audition.integration;

import com.audition.BaseTest;
import com.audition.model.AuditionComment;
import com.audition.service.IIntegrationUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditionIntegrationCommentsClientTest extends BaseTest {

    private transient RestTemplate restTemplate;
    private transient AuditionIntegrationCommentsClient client;
    private transient IIntegrationUrlService urlService;

    @BeforeEach
    void init() {
        restTemplate = mock(RestTemplate.class);
        urlService = mock(IIntegrationUrlService.class);
        client = new AuditionIntegrationCommentsClient(restTemplate, urlService);
    }

    @Test
    void shouldGetCommentsGetComments() {
        // given
        final var postId = randomInt();
        final var id = randomInt();
        final var page = randomInt();
        final var size = randomInt();
        final var email = randomString();
        final var name = randomString();
        final var url = randomString();
        final var body = randomString();
        final var response = new AuditionComment[]{new AuditionComment(id, name, email, body)};

        when(urlService.commentsUrl(postId, page, size)).thenReturn(url);
        when(restTemplate.getForEntity(url, AuditionComment[].class))
                .thenReturn(new ResponseEntity(response, HttpStatus.OK));
        // when
        final var list = client.getComments(postId, page, size);

        // then
        assertEquals(list.size(), 1);
        final var comment = list.get(0);
        assertEquals(comment.getId(), id);
        assertEquals(comment.getBody(), body);
        assertEquals(comment.getEmail(), email);
        assertEquals(comment.getBody(), body);
    }

    @Test
    void shouldThrowRestClientExceptionGetComments() {
        // given
        final var id = randomInt();
        final var page = randomInt();
        final var size = randomInt();
        final var message = randomString();
        final var url = randomString();

        when(urlService.commentsUrl(id, page, size)).thenReturn(url);
        when(restTemplate.getForEntity(anyString(), any())).thenThrow(new RestClientException(message));

        // when
        final var exception = assertThrows(RestClientException.class, () -> {
            client.getComments(id, page, size);
        });

        // then
        assertEquals(message, exception.getMessage());
    }

    // ---

    @Test
    void shouldGetComment() {
        // given
        final var id = randomInt();
        final var email = randomString();
        final var name = randomString();
        final var url = randomString();
        final var body = randomString();
        final var response = new AuditionComment(id, name, email, body);

        when(urlService.commentUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(url, AuditionComment.class))
                .thenReturn(new ResponseEntity(response, HttpStatus.OK));
        // when
        final var comment = client.getComment(id);

        // then
        assertEquals(comment.getId(), id);
        assertEquals(comment.getBody(), body);
        assertEquals(comment.getEmail(), email);
    }

    @Test
    void shouldThrowRestClientExceptionGetComment() {
        // given
        final var id = randomInt();
        final var message = randomString();
        final var url = randomString();

        when(urlService.commentUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(anyString(), any())).thenThrow(new RestClientException(message));

        // when
        final var exception = assertThrows(RestClientException.class, () -> {
            client.getComment(id);
        });

        // then
        assertEquals(message, exception.getMessage());
    }
}