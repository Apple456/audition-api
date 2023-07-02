package com.audition.service;

import com.audition.BaseTest;
import com.audition.integration.IAuditionIntegrationCommentsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditionCommentsServiceTest extends BaseTest {

    private transient AuditionCommentsService auditionService;
    private transient IAuditionIntegrationCommentsClient client;

    @BeforeEach
    void init() {
        client = mock(IAuditionIntegrationCommentsClient.class);
        auditionService = new AuditionCommentsService(client);
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
        auditionService.getComments(postId, page, size);

        // then
        verify(client).getComments(postIdCaptor.capture(),
                pageCaptor.capture(), sizeCaptor.capture());
        assertEquals(postId, postIdCaptor.getValue());
        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
    }

    @Test
    void shouldGetComment() {
        // given
        final var idCaptor = ArgumentCaptor.forClass(Integer.class);

        final var id = randomInt();

        // when
        auditionService.getComment(id);

        // then
        verify(client).getComment(idCaptor.capture());
        assertEquals(id, idCaptor.getValue());
    }
}
