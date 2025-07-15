package miu.edu.mpp.app.service.impl;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.ArticleAuthor;
import miu.edu.mpp.app.domain.Tag;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.article.*;
import miu.edu.mpp.app.dto.user.UserResponse;
import miu.edu.mpp.app.error.exception.ResourceNotFoundException;
import miu.edu.mpp.app.repository.ArticleAuthorRepository;
import miu.edu.mpp.app.repository.ArticleRepository;
import miu.edu.mpp.app.repository.TagRepository;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.repository.spec.ArticleSpecifications;
import miu.edu.mpp.app.service.ArticleService;
import miu.edu.mpp.app.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    @Override
    @Transactional
    public ArticleCreateResponse createArticle(ArticleCreateRequest req) {

        // ---------- Tags ----------
        String tagsConcatenados = Optional.ofNullable(req.getTagList())
                .orElse(List.of())
                .stream()
                .collect(Collectors.joining(","));
//        List<Tag> tags = new ArrayList<>();
//        if (req.getTagList() != null) {
//            for (String name : req.getTagList()) {
//                Tag tag = tagRepository.findByName(name).orElseGet(() -> {
//                    Tag t = new Tag();
//                    t.setName(name);
//                    return tagRepository.save(t);
//                });
//                tags.add(tag);
//            }
//        }

        List<Tag> tags = Optional.ofNullable(req.getTagList())
                .orElse(Collections.emptyList())
                .stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> {
                            Tag t = new Tag();
                            t.setName(name);
                            return tagRepository.save(t);
                        }))
                .collect(Collectors.toList());
        // ---------- Author ----------
        User author = userRepository
                .findById(1L)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        // ---------- Article ----------
        Article article = new Article();
        article.setAuthor(author);
        article.setTagList(tagsConcatenados);
        article.setTitle(req.getTitle());
        article.setDescription(req.getDescription());
        article.setBody(req.getBody());
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setSlug(SlugUtil.slugify(req.getTitle())); // util simple
        article.setIsLocked(req.isIslocked());
        article.setTags(tags);

        articleRepository.save(article);

        // ---------- Collaborators ----------
        boolean collaboratorsAdded = false;
        if (req.getCollaboratorList() != null) {
            List<User> collaborators = userRepository
                    .findByEmailIn(req.getCollaboratorList());
            collaboratorsAdded = !collaborators.isEmpty();
            collaborators.forEach(u -> {
                ArticleAuthor aa = new ArticleAuthor();
                aa.setArticle(article);
                aa.setUser(u);
                articleAuthorRepository.save(aa);
            });
        }

        // ---------- Response ----------
        ArticleResponse
                ar = ArticleResponse.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .tagList(tags.stream().map(Tag::getName).collect(Collectors.toList()))
                .favoritesCount(0)
                .author(/* map principal author si aplica */ null)
                .favorited(false)
                .islocked(article.isLocked())
                .collaboratorsAdded(collaboratorsAdded)
                .build();

        return new ArticleCreateResponse(ar);
    }

    @Override
    public ArticleListResponse listArticles(Long currentUserId, ArticleQueryParams p) {
        // ---------- 0. usuario actual (para marcar 'favorited') ----------
        Set<Long> favoritesOfCurrent = Set.of();
        User userInfo;
        if (currentUserId != null) {
            userInfo = userRepository
                    .findById(1L)
                    .orElseThrow(() -> new RuntimeException("Author not found"));

//           favoritesOfCurrent.addAll(
//                   userInfo.getFavoriteArticles().stream().map(Article::getId).collect(Collectors.toSet())
//           );
        }


        Specification<Article> spec = Specification.where(null);

        if (p.getTag() != null) {
            spec = spec.and(ArticleSpecifications.hasTag(p.getTag()));
        }

        if (p.getAuthor() != null) {
            // si autor no existe => respuesta vacía rápido
            if (userRepository.findByUsername(p.getAuthor()).isEmpty()) {
                return new ArticleListResponse(List.of(), 0);
            }
            spec = spec.and(ArticleSpecifications.authorUsername(p.getAuthor()));
        }

        if (p.getFavorited() != null) {
            if (userRepository.findByUsername(p.getFavorited()).isEmpty()) {
                return new ArticleListResponse(List.of(), 0);
            }
            spec = spec.and(ArticleSpecifications.favoritedByUsername(p.getFavorited()));
        }

        // ---------- 2. consulta paginada ----------
        PageRequest pageReq = PageRequest.of(
                p.getOffset() / p.getLimit(),
                p.getLimit(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Article> page = articleRepository.findAll(spec, pageReq);

        // ---------- 3. mapeo a DTO ----------
        List<ArticleResponse> responses = page.getContent().stream()
                .map(a -> toArticleResponse(a, favoritesOfCurrent.contains(a.getId())))
                .collect(Collectors.toList());

        return new ArticleListResponse(responses, page.getTotalElements());
    }

    @Override
    public ArticleFeedResponse getFeedForUser(Long userId, int limit, int offset) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<User> following = user.getFollowing();
        if (following.isEmpty()) {
            return new ArticleFeedResponse(List.of(), 0);
        }

        Page<Article> articles = articleRepository.findByAuthorsIn(
                following,
                PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        List<ArticleResponse> responseList = articles.getContent().stream()
                .map(article -> ArticleResponse.builder()
                        .id(article.getId())
                        .slug(article.getSlug())
                        .title(article.getTitle())
                        .description(article.getDescription())
                        .body(article.getBody())
                        .createdAt(article.getCreatedAt())
                        .updatedAt(article.getUpdatedAt())
                        .tagList(article.getTags().stream().map(Tag::getName).toList())
//                        .favoritesCount(article.getFavoritedBy().size())
                        .author(toUserResponse(article.getAuthor()))
                        .favorited(false)
//                        .authors(List.of())
//                        .collaboratorList(List.of())
//                        .comments(List.of())
                        .build())
                .toList();

        return new ArticleFeedResponse(responseList, articles.getTotalElements());
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .following(false)
                .build();
    }


    private ArticleResponse toArticleResponse(Article a, boolean isFavorited) {
        UserResponse author = UserResponse.builder()
                .id(a.getAuthor().getId())
                .username(a.getAuthor().getUsername())
                .email(a.getAuthor().getEmail())
                .bio(a.getAuthor().getBio())
                .image(a.getAuthor().getImage())
                .build();

        return ArticleResponse.builder()
                .id(a.getId())
                .slug(a.getSlug())
                .title(a.getTitle())
                .description(a.getDescription())
                .body(a.getBody())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .tagList(Arrays.stream(a.getTagList().split(",")).toList())
//                .favoritesCount(a.getFavoritedBy().size())
                .favorited(isFavorited)
                .author(author)
//                .islocked(a.getIsLocked())
                .build();
    }
}