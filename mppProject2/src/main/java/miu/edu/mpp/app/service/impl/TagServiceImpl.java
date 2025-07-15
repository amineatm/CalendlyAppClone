package miu.edu.mpp.app.service.impl;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.tag.TagsResponse;
import miu.edu.mpp.app.repository.TagRepository;
import miu.edu.mpp.app.service.TagService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagsResponse getAllTags() {
        var names = tagRepository.findAll()
                .stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toList());
        return new TagsResponse(names);
    }
}