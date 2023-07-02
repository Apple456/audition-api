package com.audition.integration;

import com.audition.model.AuditionPost;
import com.audition.service.IIntegrationUrlService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class AuditionIntegrationPostsClient implements IAuditionIntegrationPostsClient {

    private final transient IIntegrationUrlService integrationUrlService;
    private final transient RestTemplate restTemplate;

    private final transient IAuditionIntegrationCommentsClient commentsClient;

    public AuditionIntegrationPostsClient(final RestTemplate restTemplate,
                                          final IAuditionIntegrationCommentsClient commentsClient,
                                          final IIntegrationUrlService integrationUrlService) {
        this.restTemplate = restTemplate;
        this.integrationUrlService = integrationUrlService;
        this.commentsClient = commentsClient;
    }

    @Override
    public List<AuditionPost> getPosts(final Integer userId, final Integer page, final Integer size) {
        final var url = integrationUrlService.postsForUserUrl(userId, page, size);
        final var responseEntity = restTemplate.getForEntity(url, AuditionPost[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public AuditionPost getPostById(final Integer id, final boolean loadComments, final Integer page, final Integer size) {
        final var responseEntity = restTemplate.getForEntity(integrationUrlService.postUrl(id), AuditionPost.class);
        final var auditPost = responseEntity.getBody();
        if (loadComments && Objects.nonNull(auditPost)) {
            auditPost.setAuditionComments(commentsClient.getComments(auditPost.getId(), page, size));
        }
        return auditPost;
    }
}