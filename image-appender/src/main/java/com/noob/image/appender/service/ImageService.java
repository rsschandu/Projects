package com.noob.image.appender.service;

import com.noob.image.appender.entity.Image;
import com.noob.image.appender.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public Image findImageByImageUrl(String imageUrl) {
        return imageRepository.findImageByImageUrl(imageUrl);
    }

    public Set<Image> getImagesByTags(List<String> tagNames) {
        return imageRepository.getImagesByTags(tagNames,new Long(tagNames.size()));
    }

    public List<Image> getImagesByMatchingTags(ArrayList<String> processedTags, String coreTag) {
        Long count = imageRepository.getCountOfImagesByMatchingTags(processedTags,coreTag);

        if(count!=null)
        {
//            log.info("count {} - tags {} - {}",count,processedTags.toString(),coreTag);
            return imageRepository.getImagesByMatchingTags(processedTags,coreTag,count);
        }
        return new ArrayList<>();
    }
}
