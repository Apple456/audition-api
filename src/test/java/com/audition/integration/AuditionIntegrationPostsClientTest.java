package com.audition.integration;

import com.audition.BaseTest;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.IIntegrationUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditionIntegrationPostsClientTest extends BaseTest {

    private transient RestTemplate restTemplate;
    private transient AuditionIntegrationPostsClient client;
    private transient IIntegrationUrlService urlService;
    private transient IAuditionIntegrationCommentsClient commentsClient;

    @BeforeEach
    void init() {
        restTemplate = mock(RestTemplate.class);
        urlService = mock(IIntegrationUrlService.class);
        commentsClient = mock(IAuditionIntegrationCommentsClient.class);
        client = new AuditionIntegrationPostsClient(restTemplate, commentsClient,
                urlService);
    }

    // ### getPosts
    @Test
    void shouldGetPostsWhenResponseIs200() {
        // given
        final var userId = randomInt();
        final var id = randomInt();
        final var page = randomInt();
        final var size = randomInt();
        final var tile = randomString();
        final var url = randomString();
        final var body = randomString();
        final var response = new AuditionPost[]{new AuditionPost(userId, id, tile, body, null)};

        when(urlService.postsForUserUrl(userId, page, size)).thenReturn(url);
        when(restTemplate.getForEntity(url, AuditionPost[].class))
                .thenReturn(new ResponseEntity(response, HttpStatus.OK));
        // when
        final var list = client.getPosts(userId, page, size);

        // then
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getId(), id);
        assertEquals(list.get(0).getUserId(), userId);
        assertEquals(list.get(0).getTitle(), tile);
        assertEquals(list.get(0).getBody(), body);
    }

    @Test
    void shouldThrowRestClientExceptionGetPosts() {
        // given
        final var userId = randomInt();
        final var page = randomInt();
        final var size = randomInt();
        final var exceptionMessage = randomString();
        final var url = randomString();
        when(urlService.postsForUserUrl(userId, page, size)).thenReturn(url);

        when(restTemplate.getForEntity(url, AuditionPost[].class))
                .thenThrow(new RestClientException(exceptionMessage));

        // when
        final var exception = assertThrows(RestClientException.class, () -> {
            client.getPosts(userId, page, size);
        });

        // then
        assertEquals(exceptionMessage, exception.getMessage());
    }

    // ### getPostById
    @Test
    void shouldGetPostByIdNoLoadComments() {
        // given
        final var id = randomInt();
        final var userId = randomInt();
        final var title = randomString();
        final var body = randomString();
        final var url = randomString();
        final var response = new AuditionPost(userId, id, title, body, null);
        when(urlService.postUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(url, AuditionPost.class))
                .thenReturn(new ResponseEntity(response, HttpStatus.OK));
        // when
        final var post = client.getPostById(id, false, null, null);

        // then
        assertEquals(id, post.getId());
        assertEquals(userId, post.getUserId());
        assertEquals(title, post.getTitle());
        assertEquals(body, post.getBody());
    }

    @Test
    void shouldGetPostByIdLoadComments() {
        // given
        final var id = randomInt();
        final var commentId = randomInt();
        final var userId = randomInt();
        final var response = new AuditionPost(userId, id, randomString(),
                randomString(), null);
        response.setAuditionComments(List.of(new AuditionComment()));

        final var page = randomInt();
        final var size = randomInt();
        final var url = randomString();

        when(urlService.postUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(url, AuditionPost.class))
                .thenReturn(new ResponseEntity(response, HttpStatus.OK));
        when(commentsClient.getComments(id, page, size))
                .thenReturn(List.of(new AuditionComment(commentId, randomString(),
                        randomString(), randomString())));

        // when
        final var post = client.getPostById(id, true, page, size);

        // then
        assertEquals(id, post.getId());
        assertEquals(userId, post.getUserId());
        assertEquals(1, post.getAuditionComments().size());
        assertEquals(commentId, post.getAuditionComments().get(0).getId());
    }

    @Test
    void shouldThrowRestClientExceptionPostById() {
        // given
        final var id = randomInt();
        final var url = randomString();
        final var exceptionMessage = randomString();

        when(urlService.postUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new RestClientException(exceptionMessage));
        // when
        final var exception = assertThrows(RestClientException.class, () -> {
            client.getPostById(id, true, randomInt(), randomInt());
        });

        // then
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    void shouldThrowRestClientExceptionPostByIdLoadingComments() {
        // given
        final var id = randomInt();
        final var page = randomInt();
        final var size = randomInt();
        final var url = randomString();
        final var exceptionMessage = randomString();
        final var response = new AuditionPost(randomInt(), id, "", "", null);

        when(urlService.postUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(url, AuditionPost.class))
                .thenReturn(new ResponseEntity(response, HttpStatus.OK));
        when(commentsClient.getComments(id, page, size))
                .thenThrow(new RestClientException(exceptionMessage));

        // when
        final var exception = assertThrows(RestClientException.class, () -> {
            client.getPostById(id, true, page, size);
        });

        // then
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    void shouldThrowPostNotFoundExceptionGetPostById() {
        // given
        final var id = randomInt();
        final var url = randomString();
        when(urlService.postUrl(id)).thenReturn(url);
        when(restTemplate.getForEntity(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // when
        final var exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getPostById(id, true, randomInt(), randomInt());
        });

        // then
        assertEquals("404 NOT_FOUND", exception.getMessage());
    }
}