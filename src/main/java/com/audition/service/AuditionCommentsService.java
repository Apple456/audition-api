package com.audition.service;

import com.audition.integration.IAuditionIntegrationCommentsClient;
import com.audition.model.AuditionComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditionCommentsService implements IAuditionCommentsService {

    private final transient IAuditionIntegrationCommentsClient client;

    public AuditionCommentsService(final IAuditionIntegrationCommentsClient client) {
        this.client = client;
    }

    @Override
    public List<AuditionComment> getComments(final Integer postId, final Integer page, final Integer size) {
        return client.getComments(postId, page, size);
    }

    @Override
    public AuditionComment getComment(final Integer id) {
        return client.getComment(id);
    }
}