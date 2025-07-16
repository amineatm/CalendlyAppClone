package miu.edu.mpp.app.dto.article;

public interface RoasterUserArticle {
    Long getId();
    String getUsername();
    Long getTotalArticlesWritten();
    Long getTotalLikesReceived();
    String getFirstArticleDate(); // or LocalDate if your DB returns that
}
