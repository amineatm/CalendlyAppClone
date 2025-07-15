package miu.edu.mpp.app.controller;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.article.ArticleCreateResponse;
import miu.edu.mpp.app.dto.article.ArticleCreateWrapper;
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
}