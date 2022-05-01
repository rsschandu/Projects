package com.noob.image.appender.utils;

import com.noob.image.appender.entity.Image;
import com.noob.image.appender.entity.Tag;
import com.noob.image.appender.service.ImageService;
import com.noob.image.appender.service.TagService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
@Slf4j
public class DataUtils {
    @Autowired
    private ImageService imageService;
    @Autowired
    private TagService tagService;

    public String addImages(String paragraph) {
        String imagesHtml = "";
        String temp = paragraph.replaceAll("[^a-zA-Z0-9- ]", " ");
        temp = temp.trim().toLowerCase(Locale.ROOT);
        temp = temp.replaceAll(" +", " ");
        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
        String[] tokens = whitespaceTokenizer.tokenize(temp);
        SnowballStemmer ss = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
        List<String> words = new ArrayList<>();
        for (String token : tokens) {
            words.add(doStemming(token, ss));
        }
//        String[] words = temp.split(" ");
        List<Tag> tempTags = tagService.findTagsInArray(words.toArray(new String[words.size()]));
        List<Tag> tags = new ArrayList<>();
        for (String word : words) {
            for (Tag tag : tempTags) {
                if (word.equals(tag.getTagName())) {
                    tags.add(tag);
                }
            }
        }
        HashSet<Image> images = (HashSet<Image>) imageService.getImagesByTags(words);
        HashSet<Image> imagesToAdd = new HashSet<>();
        ArrayList<Tag> processedTags = new ArrayList<>();

        for (Tag tag : tags) {
//            log.info("{}",tag.getTagName());
            if (!tag.getTagType().equals("core")) {
                processedTags.add(tag);
            } else {
                Image image = getImageBasedOnTags(processedTags, tag);
                if (image != null) {
                    imagesToAdd.add(image);
                    processedTags.clear();
                }
            }
        }


//        HashSet<Image> tempImages = new HashSet<>(images);
//        HashSet<Image> lastImages = new HashSet<>();

//        for(Tag tag : tags){
//            processedTags.add(tag);
//            tempImages = getImagesContainingTag(tempImages,tag);
//            if(tempImages.isEmpty()&&!lastImages.isEmpty()&&hasCoreTag(processedTags))
//            {
//                imagesToAdd.add(getRandomImage(lastImages));
////                tempImages.addAll(images);
//                tempImages = getImagesContainingTag(images,tag);
//                processedTags.clear();
//
//            }
//            lastImages.clear();
//            lastImages.addAll(tempImages);
//        }
//        if(!lastImages.isEmpty()){
//            imagesToAdd.add(getRandomImage(lastImages));
//        }
        String imageUrl;
        for (Image image : imagesToAdd) {
            imageUrl = (image.getImageLocation() == null) ? image.getImageUrl() : "image/" + image.getImageLocation();
            imagesHtml += "<img src=\"" + imageUrl + "\" style=\"height:300px;width:auto;\" onclick=\"viewImage(" + image.getImageId() + ")\" title = \"" +
                    image.getImageId() + "\">";
        }
        return imagesHtml;
    }

    private Image getImageBasedOnTags(ArrayList<Tag> processedTags, Tag coreTag) {
        ArrayList<String> processedTagNames = new ArrayList<>();
        for (Tag tag : processedTags) {
            processedTagNames.add(tag.getTagName());
        }
        List<Image> images = imageService.getImagesByMatchingTags(processedTagNames, coreTag.getTagName());
//        for(Tag tag: processedTags)
//        {
//            log.info("{},",tag.getTagName());
//        }
//        log.info("------------------");
//        for(Image image: images)
//        {
//            log.info(image.getImageUrl());
//        }
        if (images.isEmpty()) {
            return null;
        }
        return getRandomImage(images);
    }

    private boolean hasCoreTag(ArrayList<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getTagType().equals("core")) {
                return true;
            }
        }
        return false;
    }

    private HashSet<Image> getImagesContainingTag(HashSet<Image> tempImages, Tag tag) {
        HashSet<Image> result = new HashSet<>();
        for (Image image : tempImages) {
            if (image.getTags().contains(tag)) {
                result.add(image);
            }
        }
        return result;
    }

    private Image getRandomImage(List<Image> tempImages) {

        Random random = new Random();
        int randomNumber = random.nextInt(tempImages.size());
        return tempImages.get(randomNumber);
    }

    private String doStemming(String token, Stemmer stemmer) {
        return stemmer.stem(token).toString();
    }

    @SneakyThrows
    public ArrayList<String> getData(String url) {
        String line;
        ArrayList<String> result = new ArrayList<>();
        String temp = "";
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.addRequestProperty("User-Agent", "Mozilla");
        urlConnection.setReadTimeout(5000);
        urlConnection.setConnectTimeout(5000);
        int count = 0;
        InputStream is = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        boolean start = false;
        while ((line = br.readLine()) != null) {
            if (!start) {
                if (line.contains("<pre>")) {
                    start = true;
                }
                continue;
            }
            if (line.contains("</pre>")) {
                break;
            }
            if (!line.isEmpty()) {
                temp += line + " ";
            } else {
                result.add(temp);
                result.add(addImages(temp));
                temp = "";
                count++;
            }
        }
//            log.info("target parah {}",result.get(406));
//            log.info(addImages(result.get(406)));
        return result;
    }
}
