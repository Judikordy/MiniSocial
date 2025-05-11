package com.redhat.training.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String content;
    LocalDateTime timestamp;

    public Post(String content, LocalDateTime timestamp){
        this.content = content;
        this.timestamp = timestamp;
    }

    public Post(){}

    @ManyToOne
    User author;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }
}
