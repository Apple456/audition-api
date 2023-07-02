package com.audition.web;

import com.audition.BaseTest;
import com.audition.service.IAuditionCommentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditionCommentsControllerTest extends BaseTest {

    private transient AuditionCommentsController controller;
    private transient IAuditionCommentsService service;

    @BeforeEach
    void init() {
        service = mock(IAuditionCommentsService.class);
        controller = new AuditionCommentsController(service);
    }

    @Test
    void shouldGetComments() {
        // given
        final var postIdCaptor = ArgumentCaptor.forClass(Integer.class);
        final var pageCaptor = ArgumentCaptor.forClass(Integer.class);
        final var sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        final var postId = randomInt();
        final var page = randomInt();
        final var size = randomInt();

        // when
        controller.comments(postId, page, size);

        // then
        verify(service).getComments(postIdCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(postId, postIdCaptor.getValue());
        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
    }

    @Test
    void shouldGetCommentById() {
        // given
        final var idCaptor = ArgumentCaptor.forClass(Integer.class);
        final var id = randomInt();

        // when
        controller.commentById(id);

        // then
        verify(service).getComment(idCaptor.capture());
        assertEquals(id, idCaptor.getValue());
    }
}