package miu.edu.mpp.app.dto.article;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ArticleCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String body;

    private List<String> tagList;

    private List<String> collaboratorList;

    private boolean islocked;
}
