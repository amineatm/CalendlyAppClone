package miu.edu.mpp.app.service.impl;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.Comment;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.article.CommentRequest;
import miu.edu.mpp.app.dto.article.CommentResponse;
import miu.edu.mpp.app.dto.user.UserRo;
import miu.edu.mpp.app.error.exception.ResourceNotFoundException;
import miu.edu.mpp.app.repository.ArticleRepository;
import miu.edu.mpp.app.repository.CommentRepository;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentResponse> addComment(String slug, CommentRequest request, Long userId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        User authorComment = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setBody(request.getBody());
        comment.setArticle(article);
        comment.setAuthor(authorComment);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return commentRepository.findByArticle(article).stream()
                .map(c -> CommentResponse.builder()
                        .id(c.getId())
                        .body(c.getBody())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .article(c.getArticle().getId())
                        .author(getUserById(c.getAuthor()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByArticleSlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        return commentRepository.findByArticle(article).stream()
                .map(c -> CommentResponse.builder()
                        .id(c.getId())
                        .body(c.getBody())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .article(c.getArticle().getId())
                        .author(getUserById(c.getAuthor()))
                        .build())
                .collect(Collectors.toList());
    }

    public UserRo getUserById(User  user) {
        return UserRo.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }
}