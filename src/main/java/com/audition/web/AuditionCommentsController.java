package com.audition.web;

import com.audition.model.AuditionComment;
import com.audition.service.IAuditionCommentsService;
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
        name = "Comments Controller",
        description = "Retrieve comments"
)
@RestController
@Validated
@RequestMapping("comments")
public class AuditionCommentsController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "100";
    public static final long MAX_PAGE_SIZE = 100L;

    private final transient IAuditionCommentsService service;

    public AuditionCommentsController(final IAuditionCommentsService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionComment> comments(@RequestParam @Positive final Integer postId,
                                                        @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Min(0) final Integer page,
                                                        @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
                                                        @Positive @Max(MAX_PAGE_SIZE) final Integer size) {
        return service.getComments(postId, page, size);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionComment commentById(@PathVariable @Positive final Integer id) {
        return service.getComment(id);
    }
}
