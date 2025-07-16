package miu.edu.mpp.app.service;

import miu.edu.mpp.app.dto.article.*;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    ArticleCreateResponse createArticle(ArticleCreateRequest request);

    ArticleListResponse listArticles(Long currentUserId, ArticleQueryParams params);

    ArticleFeedResponse getFeedForUser(Long userId, int limit, int offset);

    RoasterResponse findRoasters(Map<String, String> query);

}
