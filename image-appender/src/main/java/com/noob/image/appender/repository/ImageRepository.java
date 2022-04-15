package com.noob.image.appender.repository;

import com.noob.image.appender.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    Image findImageByImageUrl(String imageUrl);

//    @Query("Select t.images from Tag t where t.tagName in ?1")
    @Query("SELECT i FROM Image i LEFT JOIN i.tags t WHERE t.tagName IN :tagNames GROUP BY i HAVING COUNT( t) = :tagNamesSize")
Set<Image> getImagesByTags(@Param("tagNames")List<String> tagNames,@Param("tagNamesSize") Long size);


    @Query(value = "SELECT result.image_id as image_id ,result.image_location as image_location,result.image_url as image_url FROM\n" +
            "(SELECT *, COUNT(*) AS tag_count\n" +
            "FROM (select * from image where image_id in \n" +
            "(select images_image_id from image_tags where tags_tag_id in \n" +
            "(select tag_id from tag where tag_name = :coreTag))) as img\n" +
            "JOIN image_tags\n" +
            "ON image_tags.images_image_id = img.image_id\n" +
            "WHERE image_tags.tags_tag_id IN (select tag_id from tag where tag_name in (:tags))\n" +
            "GROUP BY img.image_id\n" +
            "ORDER BY tag_count DESC) AS result where result.tag_count = :matchedTagCount ;",nativeQuery = true)
    List<Image> getImagesByMatchingTags(@Param("tags") List<String> processedTags,@Param("coreTag") String tag,
                                        @Param("matchedTagCount") Long matchedTagCount);

    @Query(value = "SELECT MAX(tag_count) FROM\n" +
            "(SELECT *, COUNT(*) AS tag_count\n" +
            "FROM (select * from image where image_id in \n" +
            "(select images_image_id from image_tags where tags_tag_id in \n" +
            "(select tag_id from tag where tag_name = :coreTag))) as img\n" +
            "JOIN image_tags\n" +
            "ON image_tags.images_image_id = img.image_id\n" +
            "WHERE image_tags.tags_tag_id IN (select tag_id from tag where tag_name in (:tags))\n" +
            "GROUP BY img.image_id\n" +
            "ORDER BY tag_count DESC) AS result ;",nativeQuery = true)
    Long getCountOfImagesByMatchingTags(@Param("tags") List<String> processedTags,@Param("coreTag") String tag);


}
