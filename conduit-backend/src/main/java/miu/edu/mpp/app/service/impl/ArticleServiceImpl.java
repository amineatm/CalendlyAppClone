package miu.edu.mpp.app.service.impl;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.ArticleAuthor;
import miu.edu.mpp.app.domain.Tag;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.article.ArticleCreateRequest;
import miu.edu.mpp.app.dto.article.ArticleCreateResponse;
import miu.edu.mpp.app.dto.article.ArticleResponse;
import miu.edu.mpp.app.repository.ArticleAuthorRepository;
import miu.edu.mpp.app.repository.ArticleRepository;
import miu.edu.mpp.app.repository.TagRepository;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.service.ArticleService;
import miu.edu.mpp.app.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
}