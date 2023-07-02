package com.audition.integration;

import com.audition.model.AuditionPost;

import java.util.List;

public interface IAuditionIntegrationPostsClient {

    List<AuditionPost> getPosts(Integer userId, Integer page, Integer size);

    AuditionPost getPostById(Integer id, boolean loadComments, Integer page, Integer size);

}