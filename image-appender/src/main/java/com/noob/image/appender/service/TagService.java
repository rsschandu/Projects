package com.noob.image.appender.service;

import com.noob.image.appender.entity.Tag;
import com.noob.image.appender.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag findTagByTagName(String tagName) {
        return tagRepository.findTagByTagName(tagName);
    }

    public List<Tag> findTagsInArray(String[] words) {
        return tagRepository.findTagsInArray(words);
    }

    public long countByTagName(String word) {
        return tagRepository.countByTagName(word);
    }

    public Tag getTagByTagName(String word) {
        return tagRepository.getTagByTagName(word);
    }

    public List<Tag> findByTagNameContaining(String tagName) {
        return tagRepository.findByTagNameContaining(tagName);
    }

    public Tag findTagByTagId(Long tagId) {
        return tagRepository.findTagByTagId(tagId);
    }

    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }

    public void save(Tag persistantTag) {
        tagRepository.save(persistantTag);
    }
}
