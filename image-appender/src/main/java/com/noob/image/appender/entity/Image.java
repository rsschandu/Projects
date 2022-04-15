package com.noob.image.appender.entity;

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
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="image_id")
    private Long imageId;
    @Column(name="image_url",unique = true, length = 700)
    private String imageUrl;
    @Column(name = "image_location",unique = true, length = 700)
    private String imageLocation;
    @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Tag> tags;
}
