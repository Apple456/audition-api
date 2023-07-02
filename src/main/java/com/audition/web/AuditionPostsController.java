package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.service.IAuditionPostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Posts Controller",
        description = "Retrieve posts"
)
@RestController
@Validated
@RequestMapping("posts")
public class AuditionPostsController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "100";
    public static final long MAX_PAGE_SIZE = 100L;

    private final transient IAuditionPostService service;

    //https://www.javaguides.net/2023/03/spring-boot-3-rest-api-documentation.html

    public AuditionPostsController(final IAuditionPostService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> posts(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Min(0) final Integer page,
                                                  @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) @Positive @Max(MAX_PAGE_SIZE) final Integer size,
                                                  @RequestParam(required = false) @Positive final Integer userId) {
        return service.getPosts(userId, page, size);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost post(@PathVariable("id") @Positive final Integer postId) {
        return service.getPostById(postId, false, Integer.valueOf(DEFAULT_PAGE), Integer.valueOf(DEFAULT_PAGE_SIZE));
    }

    @GetMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost postWithComments(@PathVariable("id") @Positive final Integer postId,
                                                       @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Min(0) final Integer page,
                                                       @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
                                                       @Positive @Max(MAX_PAGE_SIZE) final Integer size) {
        return service.getPostById(postId, true, page, size);
    }
}
