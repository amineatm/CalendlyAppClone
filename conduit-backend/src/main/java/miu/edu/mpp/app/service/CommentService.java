package miu.edu.mpp.app.service;

import miu.edu.mpp.app.dto.article.CommentRequest;
import miu.edu.mpp.app.dto.article.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> addComment(String slug, CommentRequest request, Long userId);
    List<CommentResponse> getCommentsByArticleSlug(String slug);
}
