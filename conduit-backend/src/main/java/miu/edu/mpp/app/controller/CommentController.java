package miu.edu.mpp.app.controller;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.article.*;
import miu.edu.mpp.app.security.CurrentUser;
import miu.edu.mpp.app.security.UserContext;
import miu.edu.mpp.app.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{slug}/comments")
    public ResponseEntity<Map<String, List<CommentResponse>>> addComment(
            @PathVariable String slug,
            @RequestBody @Valid CommentRequestWrapper<CommentRequest> request
    ) {
        CurrentUser user = UserContext.get();
        List<CommentResponse> comments = commentService.addComment(slug, request.getComment(), user.getId());
        return ResponseEntity.ok(Map.of("comments", comments));
    }

    @GetMapping("{slug}/comments")
    public ResponseEntity<Map<String, List<CommentResponse>>> getCommentsBySlug(
            @PathVariable String slug
    ) {
        CurrentUser user = UserContext.get();

        List<CommentResponse> comments = commentService.getCommentsByArticleSlug(slug);
        return ResponseEntity.ok(Map.of("comments", comments));
    }

}