package miu.edu.mpp.app.service.impl;

import miu.edu.mpp.app.domain.*;
import miu.edu.mpp.app.dto.article.ArticleCreateRequest;
import miu.edu.mpp.app.dto.article.ArticleCreateResponse;
import miu.edu.mpp.app.dto.article.ArticleDTOResponse;
import miu.edu.mpp.app.dto.article.ArticleResponse;
import miu.edu.mpp.app.repository.*;
import miu.edu.mpp.app.security.CurrentUser;
import miu.edu.mpp.app.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.persistence.EntityManager;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ArticleServiceImplTest {

    @Mock private ArticleRepository articleRepository;
    @Mock private TagRepository tagRepository;
    @Mock private UserRepository userRepository;
    @Mock private ArticleAuthorRepository articleAuthorRepository;
    @Mock private EntityManager entityManager;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createArticle_withTagsAndCollaborators_shouldCreateSuccessfully() {
        // Arrange
        Long userId = 1L;
        CurrentUser currentUser = new CurrentUser(userId, "user@example.com", "ROLE_USER");

        ArticleCreateRequest request = new ArticleCreateRequest();
        request.setTitle("New Article");
        request.setDescription("Description");
        request.setBody("Content");
        request.setTagList(List.of("java", "spring"));
        request.setCollaboratorList(List.of("collab@example.com"));

        Tag tag1 = new Tag(1L, "java");
        Tag tag2 = new Tag(2L, "spring");

        User author = new User();
        author.setId(userId);
        author.setEmail("user@example.com");

        User collaborator = new User();
        collaborator.setId(2L);
        collaborator.setEmail("collab@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(author));
        when(tagRepository.findByName("java")).thenReturn(Optional.of(tag1));
        when(tagRepository.findByName("spring")).thenReturn(Optional.of(tag2));
        when(userRepository.findByEmailIn(List.of("collab@example.com"))).thenReturn(List.of(collaborator));
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> {
            Article saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        // Act
        ArticleDTOResponse<ArticleResponse> response = articleService.createArticle(currentUser, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getArticle()).isNotNull();
        assertThat(response.getArticle().getSlug()).contains("new-article");
        assertThat(response.getArticle().getTitle()).isEqualTo("New Article");
        assertThat(response.getArticle().getTagList()).containsExactlyInAnyOrder("java", "spring");

        verify(articleRepository).save(any(Article.class));
        verify(articleAuthorRepository).save(any(ArticleAuthor.class));
    }

    @Test
    void createArticle_withNoTagsOrCollaborators_shouldCreateWithoutExtras() {
        // Arrange
        Long userId = 1L;
        CurrentUser currentUser = new CurrentUser(userId, "user@example.com", "ROLE_USER");

        ArticleCreateRequest request = new ArticleCreateRequest();
        request.setTitle("Simple Article");
        request.setDescription("None");
        request.setBody("Body");
        request.setTagList(null);
        request.setCollaboratorList(null);
        request.setIslocked(false);

        User author = new User();
        author.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(author));
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> {
            Article saved = invocation.getArgument(0);
            saved.setId(101L);
            return saved;
        });

        // Act
        ArticleDTOResponse<ArticleResponse>  response = articleService.createArticle(currentUser, request);

        // Assert
        assertThat(response.getArticle().getTitle()).isEqualTo("Simple Article");
        assertThat(response.getArticle().getTagList()).isNotNull();

        verify(articleRepository).save(any(Article.class));
        verify(articleAuthorRepository, never()).save(any(ArticleAuthor.class));
    }
}
