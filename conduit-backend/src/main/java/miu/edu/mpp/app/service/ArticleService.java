package miu.edu.mpp.app.service;

import miu.edu.mpp.app.dto.article.ArticleFeedResponse;
import miu.edu.mpp.app.dto.article.ArticleCreateRequest;
import miu.edu.mpp.app.dto.article.ArticleCreateResponse;
import miu.edu.mpp.app.dto.article.ArticleListResponse;
import miu.edu.mpp.app.dto.article.ArticleQueryParams;

public interface ArticleService {
    ArticleCreateResponse createArticle(ArticleCreateRequest request);
    ArticleListResponse listArticles(Long currentUserId, ArticleQueryParams params);
    ArticleFeedResponse getFeedForUser(Long userId, int limit, int offset);


}
