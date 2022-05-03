package com.noob.image.appender.repository;

import com.noob.image.appender.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findTagByTagName(String tagName);

    @Query("Select t from Tag t where t.tagName in ?1")
    List<Tag> findTagsInArray(String[] words);

    long countByTagName(String name);

    Tag getTagByTagName(String word);

    List<Tag> findByTagNameContaining(String tagName);

    Tag findTagByTagId(Long tagId);
}
