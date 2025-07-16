package miu.edu.mpp.app.controller;


import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.tag.TagsResponse;
import miu.edu.mpp.app.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<TagsResponse> getTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }
}
