package com.audition.service;

public interface IIntegrationUrlService {

    String commentsUrl(Integer postId, Integer page, Integer size);

    String postUrl(Integer id);

    String commentUrl(Integer id);

    String postsForUserUrl(Integer userId, Integer page, Integer size);

}
