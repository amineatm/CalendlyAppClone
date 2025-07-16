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

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
    private final EntityManager entityManager;

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


    @Override
    @Transactional
    public RoasterResponse findRoasters(Map<String, String> query) {
        // ---------- 0. Build the SQL query ----------
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.id, u.username, ");
        sql.append("COUNT(DISTINCT a.id) AS totalArticlesWritten, ");
        sql.append("COALESCE(COUNT(uf.article_id), 0) AS totalLikesReceived, ");
        sql.append("MIN(a.created_at) AS firstArticleDate ");
        sql.append("FROM users u ");
        sql.append("LEFT JOIN articles a ON a.author_id = u.id ");
        sql.append("LEFT JOIN user_favorites uf ON uf.article_id = a.id ");
        sql.append("GROUP BY u.id, u.username ");

        // ---------- 1. Handle query parameters like limit and offset ----------
        if (query.containsKey("limit")) {
            sql.append("LIMIT ").append(query.get("limit"));
        }

        if (query.containsKey("offset")) {
            sql.append("OFFSET ").append(query.get("offset"));
        }

        // ---------- 2. Run the query ----------
        TypedQuery<Object[]> nativeQuery = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql.toString(), Object[].class);
        List<Object[]> result = nativeQuery.getResultList();  // getResultList() works here

        // ---------- 3. Map query results to RoasterUserArticleDto ----------
        List<RoasterUserArticle> roasters = result.stream()
                .map(obj -> new RoasterUserArticle(
                        ((Number) obj[0]).longValue(),         // user id
                        (String) obj[1],                      // username
                        ((Number) obj[2]).longValue(),        // total articles written
                        ((Number) obj[3]).longValue(),        // total likes received
                        (String) obj[4]                       // first article date
                ))
                .collect(Collectors.toList());

        // ---------- 4. Wrap the results in a RoasterResponse and return ----------
        return new RoasterResponse(roasters);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .following(false)  // You can modify this logic if needed
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
                .favorited(isFavorited)
                .author(author)
                .build();
    }

}