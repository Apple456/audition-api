package com.audition.service;

import com.audition.BaseTest;
import com.audition.integration.IAuditionIntegrationPostsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditionPostServiceTest extends BaseTest {

    private transient AuditionPostService auditionService;
    private transient IAuditionIntegrationPostsClient auditionIntegrationClient;

    @BeforeEach
    void init() {
        auditionIntegrationClient = mock(IAuditionIntegrationPostsClient.class);
        auditionService = new AuditionPostService(auditionIntegrationClient);
    }

    @Test
    void shouldGetListOfPosts() {
        // given
        final var userIdCaptor = ArgumentCaptor.forClass(Integer.class);
        final var pageCaptor = ArgumentCaptor.forClass(Integer.class);
        final var sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        final var userId = randomInt();
        final var page = randomInt();
        final var size = randomInt();

        // when
        auditionService.getPosts(userId, page, size);

        // then
        verify(auditionIntegrationClient).getPosts(userIdCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
    }

    @Test
    void shouldGetListOfPostById() {
        // given
        final var postIdCaptor = ArgumentCaptor.forClass(Integer.class);
        final var pageCaptor = ArgumentCaptor.forClass(Integer.class);
        final var sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        final var loadCommentsCaptor = ArgumentCaptor.forClass(Boolean.class);
        final var postId = randomInt();
        final var page = randomInt();
        final var size = randomInt();
        final var loadComments = randomInt() % 2 == 0;

        // when
        auditionService.getPostById(postId, loadComments, page, size);

        // then
        verify(auditionIntegrationClient).getPostById(postIdCaptor.capture(), loadCommentsCaptor.capture(),
                pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(postId, postIdCaptor.getValue());
        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
        assertEquals(loadComments, loadCommentsCaptor.getValue());
    }

}
