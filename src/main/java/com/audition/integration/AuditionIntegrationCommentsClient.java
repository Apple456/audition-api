package com.audition.integration;

import com.audition.model.AuditionComment;
import com.audition.service.IIntegrationUrlService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class AuditionIntegrationCommentsClient implements IAuditionIntegrationCommentsClient {

    private final transient RestTemplate restTemplate;
    private final transient IIntegrationUrlService urlService;

    public AuditionIntegrationCommentsClient(final RestTemplate restTemplate, final IIntegrationUrlService urlService) {
        this.restTemplate = restTemplate;
        this.urlService = urlService;
    }

    @Override
    public List<AuditionComment> getComments(final Integer postId, final Integer page, final Integer size) {
        final var url = urlService.commentsUrl(postId, page, size);
        final var responseEntity = restTemplate.getForEntity(url, AuditionComment[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public AuditionComment getComment(final Integer id) {
        final var url = urlService.commentUrl(id);
        final var responseEntity = restTemplate.getForEntity(url, AuditionComment.class);
        return Objects.requireNonNull(responseEntity.getBody());
    }
}