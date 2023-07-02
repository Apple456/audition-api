package com.audition.service;

import com.audition.integration.IAuditionIntegrationPostsClient;
import com.audition.model.AuditionPost;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditionPostService implements IAuditionPostService {

    private final transient IAuditionIntegrationPostsClient auditionIntegrationClient;

    public AuditionPostService(final IAuditionIntegrationPostsClient auditionIntegrationClient) {
        this.auditionIntegrationClient = auditionIntegrationClient;
    }

    @Override
    public final List<AuditionPost> getPosts(final Integer userId, final Integer page, final Integer size) {
        return auditionIntegrationClient.getPosts(userId, page, size);
    }

    @Override
    public final AuditionPost getPostById(final Integer postId, final boolean loadComments, final Integer page, final Integer size) {
        return auditionIntegrationClient.getPostById(postId, loadComments, page, size);
    }
}