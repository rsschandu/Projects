package com.noob.image.appender.controller;

import com.noob.image.appender.entity.Image;
import com.noob.image.appender.entity.Tag;
import com.noob.image.appender.service.ImageService;
import com.noob.image.appender.service.TagService;
import com.noob.image.appender.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/images")
@Slf4j
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ImageUtils imageUtils;


    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public void saveImage(@RequestBody Image[] images)
    {
        Set<Tag> tags = new HashSet<>();
        Tag persistentTag;
        Image persistentImage;
        for(Image image: images)
        {
            tags.clear();
            tags.addAll(image.getTags());
            for(Tag tag: tags) {

                image.getTags().remove(tag);
                persistentTag = tagService.findTagByTagName(tag.getTagName());
                if (persistentTag == null) {
                    persistentTag = tagService.saveTag(tag);
                }else{
                    persistentTag.setTagType(tag.getTagType());
                    tagService.saveTag(persistentTag);
                }
                image.getTags().add(persistentTag);
            }
            persistentImage = imageService.findImageByImageUrl(image.getImageUrl());
            if(persistentImage == null) {
                persistentImage = imageService.saveImage(image);
                image.setImageLocation(imageUtils.saveImage(
                        persistentImage.getImageUrl(),persistentImage.getImageId()
                ));
            } else {
                persistentImage.setTags(image.getTags());
            }
            imageService.saveImage(persistentImage);
        }
    }

    @GetMapping("/tags")
    public Set<Tag> getTags(@RequestParam("tags") List<String> tagNames)
    {
        Set<Tag> tags = new HashSet<>();
        Tag tag;
        for(String tagName: tagNames)
        {
            tag = tagService.findTagByTagName(tagName);

            if(tag!=null) {
                tag.setImages(null);
                tags.add(tag);
            }
        }

        return tags;
    }

    @GetMapping("")
    public Set<Image> getImages(@RequestParam("tags") List<String> tagNames)
    {
        log.info(tagNames.toString());
        Tag tag;
        Set<Image> images = new HashSet<>();
        images = imageService.getImagesByTags(tagNames);
        return images;
    }

    @PostMapping("/tags/update/")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateTags(@RequestBody Tag[] tags)
    {
        log.info("inside update tags of image controller");
        for(Tag tag: tags)
        {
            tagService.saveTag(tag);
        }
    }


}



