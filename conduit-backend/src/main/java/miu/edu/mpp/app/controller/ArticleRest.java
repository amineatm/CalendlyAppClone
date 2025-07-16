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
    public RoasterResponse findRoasters(@RequestParam Map<String, String> query) {
        return articleService.findRoasters(query);
    }
}