package miu.edu.mpp.app.dto.article;

public class RoasterUserArticle {

    private Long id;
    private String username;
    private Long totalArticlesWritten;
    private Long totalLikesReceived;
    private String firstArticleDate;

    public RoasterUserArticle() {
    }

    public RoasterUserArticle(Long id, String username, Long totalArticlesWritten, Long totalLikesReceived, String firstArticleDate) {
        this.id = id;
        this.username = username;
        this.totalArticlesWritten = totalArticlesWritten;
        this.totalLikesReceived = totalLikesReceived;
        this.firstArticleDate = firstArticleDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalArticlesWritten() {
        return totalArticlesWritten;
    }

    public void setTotalArticlesWritten(Long totalArticlesWritten) {
        this.totalArticlesWritten = totalArticlesWritten;
    }

    public Long getTotalLikesReceived() {
        return totalLikesReceived;
    }

    public void setTotalLikesReceived(Long totalLikesReceived) {
        this.totalLikesReceived = totalLikesReceived;
    }

    public String getFirstArticleDate() {
        return firstArticleDate;
    }

    public void setFirstArticleDate(String firstArticleDate) {
        this.firstArticleDate = firstArticleDate;
    }

    @Override
    public String toString() {
        return "RoasterUserArticleDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", totalArticlesWritten=" + totalArticlesWritten +
                ", totalLikesReceived=" + totalLikesReceived +
                ", firstArticleDate='" + firstArticleDate + '\'' +
                '}';
    }
}
