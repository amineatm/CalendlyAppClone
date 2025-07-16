package miu.edu.mpp.app.controller;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.article.*;
import miu.edu.mpp.app.security.CurrentUser;
import miu.edu.mpp.app.security.UserContext;
import miu.edu.mpp.app.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @PostMapping
    public ResponseEntity<ArticleCreateResponse> createArticle(
            @Valid @RequestBody ArticleCreateWrapper wrapper) {
        CurrentUser user = UserContext.get();

        ArticleCreateResponse response =
                articleService.createArticle(user, wrapper.getArticle());

        return ResponseEntity.status(CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ArticleListResponse> listArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        CurrentUser user = UserContext.get();


        ArticleQueryParams params = ArticleQueryParams.builder()
                .tag(tag)
                .author(author)
                .favorited(favorited)
                .limit(limit)
                .offset(offset)
                .build();

        ArticleListResponse response = articleService.listArticles(user.getId(), params);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<ArticleFeedResponse> getFeed(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        CurrentUser user = UserContext.get();
        ArticleFeedResponse response = articleService.getFeedForUser(user.getId(), limit, offset);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roaster")
    public ResponseEntity<Map<String, Object>> getRoasterUsers(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        var roaster = articleService.findRoasterUsers(limit, offset);
        return ResponseEntity.ok(Map.of("roaster", roaster));
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<ArticleResponse> favoriteArticle(@PathVariable String slug) {
        CurrentUser user = UserContext.get();
        ArticleResponse response = articleService.favorite(user.getId(), slug).getArticle();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<?> unfavorite(@PathVariable String slug) {
        CurrentUser user = UserContext.get();
        articleService.unFavorite(user.getId(), slug);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{slug}")
    public ResponseEntity<ArticleDTOResponse<ArticleDto>> getArticleBySlug(
            @PathVariable String slug
            ) {
        CurrentUser user = UserContext.get();
        ArticleDTOResponse<ArticleDto> response = articleService.getArticleBySlug(slug, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{slug}/comments")
    public ResponseEntity<List> getArticleBySlugComments(
            @PathVariable String slug
            ) {
        CurrentUser user = UserContext.get();
        return ResponseEntity.ok(List.of());
    }

}