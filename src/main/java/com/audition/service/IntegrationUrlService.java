package com.audition.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IntegrationUrlService implements IIntegrationUrlService {

    private static final String START = "&_start=";
    private static final String LIMIT = "&_limit=";
    private static final String USER_ID = "&userId=";

    private final transient String baseUrl;
    private final transient String auditionPosts;
    private final transient String auditionComments;

    public IntegrationUrlService(
            final @Value("${application.config.posts.baseUrl}") String baseUrl,
            final @Value("${application.config.posts.auditionPosts}") String auditionPosts,
            final @Value("${application.config.posts.auditionComments}") String auditionComments
    ) {
        this.baseUrl = baseUrl;
        this.auditionComments = auditionComments;
        this.auditionPosts = auditionPosts;
    }

    @Override
    public String commentsUrl(final Integer postId, final Integer page, final Integer size) {
        return postUrl(postId) + "/comments" + paginationParameters(page, size);
    }

    @Override
    public String postUrl(final Integer id) {
        return singleEntityUrl(auditionPosts, id);
    }

    private String singleEntityUrl(final String entity, final Integer id) {
        return baseUrl + entity + "/" + id;
    }

    @Override
    public String commentUrl(final Integer id) {
        return singleEntityUrl(auditionComments, id);
    }

    @Override
    public String postsForUserUrl(final Integer userId, final Integer page, final Integer size) {
        return baseUrl + auditionPosts + paginationParameters(page, size) + userParameter(userId);
    }

    private String paginationParameters(final Integer page, final Integer size) {
        final var sb = new StringBuilder("?");
        if (Objects.nonNull(page)) {
            sb.append(START).append(page);
        }
        if (Objects.nonNull(size)) {
            sb.append(LIMIT).append(size);
        }
        return sb.toString();
    }

    private String userParameter(final Integer userId) {
        return Objects.nonNull(userId) ? USER_ID + userId : "";
    }
}
