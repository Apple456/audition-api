package com.audition.integration;

import com.audition.model.AuditionComment;

import java.util.List;

public interface IAuditionIntegrationCommentsClient {

    List<AuditionComment> getComments(Integer postId, Integer page, Integer size);

    AuditionComment getComment(Integer id);

}