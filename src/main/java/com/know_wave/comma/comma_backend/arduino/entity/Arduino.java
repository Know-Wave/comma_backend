package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
public class Arduino extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "arduino_id")
    private Long id;

    private String name;

    @Min(0)
    private int count;

    @Min(0)
    private int originalCount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy="arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArduinoCategory> categories;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> itemComments;

//    @OneToMany(mappedBy = "arduinoItem", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ArduinoOrder> orders;

}
