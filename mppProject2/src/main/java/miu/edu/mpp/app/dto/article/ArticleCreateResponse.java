package miu.edu.mpp.app.dto.article;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleCreateResponse {
    private ArticleResponse article;
}
