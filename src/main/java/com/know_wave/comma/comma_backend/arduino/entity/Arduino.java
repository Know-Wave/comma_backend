package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
public class Arduino extends BaseTimeEntity {

    protected Arduino() {
    }

    public Arduino(String name, int count, int originalCount, String description) {
        this.name = name;
        this.count = count;
        this.originalCount = originalCount;
        this.description = description;
    }

    public Arduino(Long id, String name, int count, int originalCount, String description, List<ArduinoCategory> categories, List<Like> likes, List<Comment> itemComments) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.originalCount = originalCount;
        this.description = description;
        this.categories = categories;
        this.likes = likes;
        this.itemComments = itemComments;
    }

    @Id @GeneratedValue
    @Column(name = "arduino_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    private int count = 0;

    @Column(nullable = false)
    @Min(0)
    private int originalCount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArduinoCategory> categories;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> itemComments;

    public void update(String name, int count, int originalCount, String description) {
        this.name = name;
        this.count = count;
        this.originalCount = originalCount;
        this.description = description;
    }

//    @OneToMany(mappedBy = "arduinoItem", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ArduinoOrder> orders;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getOriginalCount() {
        return originalCount;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCategories() {
        return categories.stream().map(category -> category.getCategory().getName()).toList();
    }
}
