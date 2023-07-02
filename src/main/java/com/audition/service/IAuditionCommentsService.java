package com.audition.service;

import com.audition.model.AuditionComment;

import java.util.List;

public interface IAuditionCommentsService {

    List<AuditionComment> getComments(Integer postId, Integer page, Integer size);

    AuditionComment getComment(Integer id);
}
