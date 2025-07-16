package miu.edu.mpp.app.service;

import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.article.*;
import miu.edu.mpp.app.security.CurrentUser;

import java.util.List;

public interface ArticleService {
    ArticleCreateResponse createArticle(CurrentUser user, ArticleCreateRequest request);

    ArticleListResponse listArticles(Long currentUserId, ArticleQueryParams params);

    ArticleFeedResponse getFeedForUser(Long userId, int limit, int offset);

    List<RoasterUserArticle> findRoasterUsers(int limit, int offset);

    ArticleWrapper favorite(Long userId, String slug);
    ArticleWrapper unFavorite(Long userId, String slug);


}
