package miu.edu.mpp.app.dto.article;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ArticleCreateWrapper {

    @NotNull
    @Valid
    private ArticleCreateRequest article;
}