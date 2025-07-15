package miu.edu.mpp.app.controller;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.article.*;
import miu.edu.mpp.app.service.ArticleService;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleRest {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleCreateResponse> createArticle(
            @Valid @RequestBody ArticleCreateWrapper wrapper) {

        ArticleCreateResponse response =
                articleService.createArticle(wrapper.getArticle());

        return ResponseEntity.status(CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<ArticleListResponse> listArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        ArticleQueryParams params = ArticleQueryParams.builder()
                .tag(tag)
                .author(author)
                .favorited(favorited)
                .limit(limit)
                .offset(offset)
                .build();

        ArticleListResponse response = articleService.listArticles(1L, params);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<ArticleFeedResponse> getFeed(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        Long currentUserId = 1L;
        ArticleFeedResponse response = articleService.getFeedForUser(currentUserId, limit, offset);
        return ResponseEntity.ok(response);
    }
}