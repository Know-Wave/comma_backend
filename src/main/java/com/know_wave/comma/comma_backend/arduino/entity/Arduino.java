package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
public class Arduino extends BaseTimeEntity {

    protected Arduino() {
    }

    public Arduino(String name, int originalCount, String description) {
        this.name = name;
        this.count = originalCount;
        this.originalCount = originalCount;
        this.description = description;
    }

    @Id @GeneratedValue
    @Column(name = "arduino_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    private int count;

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
