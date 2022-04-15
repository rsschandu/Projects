package com.noob.image.appender.controller;

import com.noob.image.appender.entity.Image;
import com.noob.image.appender.repository.ImageRepository;
import com.noob.image.appender.utils.ImageUtils;
import lombok.SneakyThrows;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ImageUtils imageUtils;

    @RequestMapping(value = "/")
    public String test(){
        List<Image> images = imageRepository.findAll();
        for(Image image: images)
        {
            String imageLocation = imageUtils.saveImage(image.getImageUrl(),image.getImageId());
            image.setImageLocation(imageLocation);
            imageRepository.save(image);
        }
//        Image image = imageRepository.getById(95L);
//        String imageLocation = imageUtils.saveImage(image.getImageUrl(),image.getImageId());
//        image.setImageLocation(imageLocation);
//        imageRepository.save(image);
        return "done";
    }
    @SneakyThrows
    @RequestMapping(value = "/url")
    public String url(){
        InputStream inputStream = new
                FileInputStream("D:\\opennlp-en-ud-ewt-pos-1.0-1.9.3.bin");
        POSModel model = new POSModel(inputStream);

        //Instantiating POSTaggerME class
        POSTaggerME tagger = new POSTaggerME(model);

        String sentence = "";
        sentence = sentence.replaceAll("[^A-Za-z0-9 ]"," ");

        //Tokenizing the sentence using WhitespaceTokenizer class
        WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;
        String[] tokens = whitespaceTokenizer.tokenize(sentence);

        //Generating tags
        String[] tags = tagger.tag(tokens);

        //Instantiating the POSSample class
        POSSample sample = new POSSample(tokens, tags);
        System.out.println(sample.toString());
        return sample.toString();
    }
}
