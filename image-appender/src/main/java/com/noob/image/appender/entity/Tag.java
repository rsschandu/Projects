package com.noob.image.appender.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tagId;
    @Column(unique = true)
    private String tagName;
    private String tagType;
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Image> images;
}
