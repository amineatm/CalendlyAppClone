package miu.edu.mpp.app.service.impl;

import miu.edu.mpp.app.domain.Tag;
import miu.edu.mpp.app.dto.tag.TagsResponse;
import miu.edu.mpp.app.repository.TagRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    private final TagRepository tagRepository = mock(TagRepository.class);
    private final TagServiceImpl tagService = new TagServiceImpl(tagRepository);

    @Test
    void getAllTags_shouldReturnListOfTagNames() {
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tag1.setName("java");
        tag2.setName("spring");
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        List<Tag> tags = List.of(tag1, tag2);
        when(tagRepository.findAll()).thenReturn(tags);

        TagsResponse response = tagService.getAllTags();

        assertEquals(List.of("java", "spring"), response.getTags());
        verify(tagRepository, times(1)).findAll();
    }
}
