package com.noob.image.appender.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class ImageUtils {
    @Value("${base.storage.location}")
    private String baseLocation;
    public String saveImage(String url, Long imageId)
    {

        try{
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            String extension = FilenameUtils.getExtension(imageUrl.getPath());
            if(extension == null || extension.isEmpty())
            {
                return null;
            }
//            log.info(String.valueOf(extension.isEmpty()));
            String location= baseLocation + String.valueOf(imageId)+"."
                    + extension;
//            log.info("opening inputstream");
//            InputStream imageReader = new BufferedInputStream(
//                    imageUrl.openStream());
            InputStream imageReader = new BufferedInputStream(
                    connection.getInputStream());
//            log.info("opening outputstream");
            OutputStream imageWriter = new BufferedOutputStream(
                    new FileOutputStream(location));
//            Image image = ImageIO.read(imageReader);

            int readByte;

            while ((readByte = imageReader.read()) != -1)
            {
                imageWriter.write(readByte);
            }
            imageReader.close();
            imageWriter.close();
            connection.disconnect();
            log.info("image is stored at: {}",location);
            return String.valueOf(imageId)+"." + extension;
        }
        catch(Exception e)
        {
//            log.info("exception cauthgt{}",e);
            return null;
        }



    }
}
