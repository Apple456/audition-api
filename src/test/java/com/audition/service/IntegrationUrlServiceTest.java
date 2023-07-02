package com.audition.service;

import com.audition.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationUrlServiceTest extends BaseTest {

    private transient IntegrationUrlService service;
    private transient String url;
    private transient String posts;
    private transient String comments;

    @BeforeEach
    void init() {
        url = randomString();
        posts = randomString();
        comments = randomString();
        service = new IntegrationUrlService(url, posts, comments);
    }

    @Test
    void shouldCreateCommentsUrl() {
        //given
        final var postId = randomInt();
        final var page = randomInt();
        final var size = randomInt();

        //when
        final var res = service.commentsUrl(postId, page, size);

        //then
        assertEquals(url + posts + "/" + postId + "/comments?&_start=" + page + "&_limit=" + size, res);
    }

    @Test
    void shouldCreateCommentsUrlNoPagination() {
        //given
        final var postId = randomInt();

        //when
        final var res = service.commentsUrl(postId, null, null);

        //then
        assertEquals(url + posts + "/" + postId + "/comments?", res);
    }

    @Test
    void shouldCreatePostUrl() {
        //given
        final var postId = randomInt();

        //when
        final var res = service.postUrl(postId);

        //then
        assertEquals(url + posts + "/" + postId, res);
    }

    @Test
    void shouldCreatePostForUserUrl() {
        //given
        final var id = randomInt();
        final var page = randomInt();
        final var size = randomInt();

        //when
        final var res = service.postsForUserUrl(id, page, size);

        //then
        assertEquals(url + posts + "?&_start=" + page + "&_limit=" + size + "&userId=" + id, res);
    }

    @Test
    void shouldCreatePostUrlNoPagination() {
        //given
        final var id = randomInt();

        //when
        final var res = service.postsForUserUrl(id, null, null);

        //then
        assertEquals(url + posts + "?" + "&userId=" + id, res);
    }

    @Test
    void shouldCreatePostUrlNoUserId() {
        //given
        final var page = randomInt();
        final var size = randomInt();

        //when
        final var res = service.postsForUserUrl(null, page, size);

        //then
        assertEquals(url + posts + "?&_start=" + page + "&_limit=" + size, res);
    }

    @Test
    void shouldCreateCommentUrl() {
        // given
        final var id = randomInt();

        //when
        final var res = service.commentUrl(id);

        //then
        assertEquals(url + comments + "/" + id, res);
    }
}
