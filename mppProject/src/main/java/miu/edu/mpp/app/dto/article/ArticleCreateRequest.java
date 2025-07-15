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

    // Tags que vienen en el JSON
    private List<String> tagList;

    // Correos de colaboradores
    private List<String> collaboratorList;

    // Flag de locking
    private boolean islocked;
}
