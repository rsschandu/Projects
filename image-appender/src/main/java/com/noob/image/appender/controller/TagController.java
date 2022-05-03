package com.noob.image.appender.controller;

import com.noob.image.appender.entity.Tag;
import com.noob.image.appender.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tags")
@Slf4j
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("")
    public List<Tag> getTags(@RequestParam("tagName") String tagName) {
        List<Tag> Tags = new ArrayList<>();
        Tags = tagService.findByTagNameContaining(tagName);
        return Tags;
    }

    @GetMapping("/delete")
    public void deleteTag(@RequestParam("tagId") Long tagId) {
        Tag tag = tagService.findTagByTagId(tagId);
        if (tag != null) {
            tagService.delete(tag);
        }
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.OK)
    public void saveTag(@RequestBody Tag tag) {
        Tag persistantTag = tagService.findTagByTagName(tag.getTagName());
        if (persistantTag != null) {
            persistantTag.setTagType(tag.getTagType());
        } else {
            persistantTag = tag;
        }
        tagService.save(persistantTag);
    }
}
