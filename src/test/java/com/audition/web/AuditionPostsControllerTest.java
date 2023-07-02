package com.audition.web;

import com.audition.BaseTest;
import com.audition.service.IAuditionPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditionPostsControllerTest extends BaseTest {

    private transient AuditionPostsController controller;
    private transient IAuditionPostService service;

    @BeforeEach
    void init() {
        service = mock(IAuditionPostService.class);
        controller = new AuditionPostsController(service);
    }

    @Test
    void shouldGetPosts() {
        // given
        final var userIdCaptor = ArgumentCaptor.forClass(Integer.class);
        final var pageCaptor = ArgumentCaptor.forClass(Integer.class);
        final var sizeCaptor = ArgumentCaptor.forClass(Integer.class);

        final var page = randomInt();
        final var size = randomInt();
        final var userId = randomInt();

        // when
        controller.posts(page, size, userId);

        // then
        verify(service).getPosts(userIdCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
    }

    @Test
    void shouldGetSinglePost() {
        // given
        final var postIdCaptor = ArgumentCaptor.forClass(Integer.class);
        final var pageCaptor = ArgumentCaptor.forClass(Integer.class);
        final var sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        final var loadCommentsCaptor = ArgumentCaptor.forClass(Boolean.class);
        final var postId = randomInt();

        // when
        controller.post(postId);

        // then
        verify(service).getPostById(postIdCaptor.capture(), loadCommentsCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(postId, postIdCaptor.getValue());
        assertEquals(0, pageCaptor.getValue());
        assertEquals(100, sizeCaptor.getValue());
        assertFalse(loadCommentsCaptor.getValue());
    }

    @Test
    void shouldGetSinglePostWithComments() {
        // given
        final var postIdCaptor = ArgumentCaptor.forClass(Integer.class);
        final var pageCaptor = ArgumentCaptor.forClass(Integer.class);
        final var sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        final var loadCommentsCaptor = ArgumentCaptor.forClass(Boolean.class);
        final var postId = randomInt();
        final var page = randomInt();
        final var size = randomInt();

        // when
        controller.postWithComments(postId, page, size);

        // then
        verify(service).getPostById(postIdCaptor.capture(), loadCommentsCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(postId, postIdCaptor.getValue());
        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
        assertTrue(loadCommentsCaptor.getValue());
    }

}