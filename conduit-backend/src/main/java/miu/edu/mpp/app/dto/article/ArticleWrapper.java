package miu.edu.mpp.app.dto.article;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleWrapper {
    private ArticleResponse article;
}
