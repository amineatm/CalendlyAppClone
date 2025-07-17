package miu.edu.mpp.app.service;

import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.article.*;
import miu.edu.mpp.app.security.CurrentUser;

import java.util.List;

public interface ArticleService {
    ArticleDTOResponse<ArticleResponse>  createArticle(CurrentUser user, ArticleCreateRequest request);
    ArticleDTOResponse<ArticleResponse> updateArticleBySlug(CurrentUser user, String slug, ArticleCreateRequest request);

    ArticleListResponse listArticles(Long currentUserId, ArticleQueryParams params);

    ArticleListResponse getFeedForUser(Long userId, int limit, int offset);

    List<RoasterUserArticle> findRoasterUsers(int limit, int offset);

    ArticleWrapper favorite(Long userId, String slug);
    ArticleWrapper unFavorite(Long userId, String slug);

    ArticleDTOResponse<ArticleDto> getArticleBySlug(String slug, CurrentUser currentUser);

}
