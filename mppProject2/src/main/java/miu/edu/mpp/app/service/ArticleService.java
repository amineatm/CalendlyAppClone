package miu.edu.mpp.app.service;

import miu.edu.mpp.app.dto.article.ArticleCreateRequest;
import miu.edu.mpp.app.dto.article.ArticleCreateResponse;

public interface ArticleService {
    ArticleCreateResponse createArticle(ArticleCreateRequest request);

}
