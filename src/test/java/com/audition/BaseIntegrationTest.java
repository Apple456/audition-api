package com.audition;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BaseIntegrationTest {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected static final String POSTS = "/posts";
    protected static final String COMMENTS = "/comments";

    @Autowired
    protected transient MockMvc mvc;

    @SneakyThrows
    protected void verifyGet(final String path, final MediaType mediaType, final HttpStatus expected) {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .get(path)
                        .accept(mediaType))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(expected.value());
    }

    @SneakyThrows
    protected void verifyPost(final String path, final MediaType mediaType, final HttpStatus expected) {
        final var result = mvc.perform(MockMvcRequestBuilders
                        .post(path)
                        .accept(mediaType))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(expected.value());
    }

    protected AuditionPost auditionPostFromString(final boolean array, final String content) throws JsonProcessingException {
        if (array) {
            return OBJECT_MAPPER.readValue(content, AuditionPost[].class)[0];
        } else {
            return OBJECT_MAPPER.readValue(content, AuditionPost.class);
        }
    }

    protected ProblemDetail problemDetailFromString(final String content) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(content, ProblemDetail.class);
    }

    protected AuditionComment commentFromString(final boolean array, final String content) throws JsonProcessingException {
        if (array) {
            return OBJECT_MAPPER.readValue(content, AuditionComment[].class)[0];
        } else {
            return OBJECT_MAPPER.readValue(content, AuditionComment.class);
        }
    }

    protected AuditionPost verifyAuditPost(final String content, final int userId, final int postId,
                                           final String title, final String body) throws JsonProcessingException {
        return verifyAuditPost(false, content, userId, postId, title, body);
    }

    protected AuditionPost verifyAuditPost(final boolean array, final String content, final int userId, final int postId,
                                           final String title, final String body) throws JsonProcessingException {
        final var post = auditionPostFromString(array, content);
        assertThat(post.getUserId()).isEqualTo(userId);
        assertThat(post.getId()).isEqualTo(postId);
        assertThat(post.getTitle()).contains(title);
        assertThat(post.getBody()).contains(body);
        return post;
    }

    protected AuditionComment verifyComment(final boolean array, final String content, final int commentId, final String email,
                                            final String name, final String body) throws JsonProcessingException {
        final var comment = commentFromString(array, content);
        assertThat(comment.getId()).isEqualTo(commentId);
        assertThat(comment.getEmail()).isEqualTo(email);
        assertThat(comment.getName()).isEqualTo(name);
        assertThat(comment.getBody()).contains(body);
        return comment;
    }
}