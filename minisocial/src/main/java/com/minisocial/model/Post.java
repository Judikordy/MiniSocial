package com.minisocial.model;

import com.minisocial.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User author;

    String content;
    LocalDateTime timestamp;

    public Post(String content, LocalDateTime timestamp){
        this.content = content;
        this.timestamp = timestamp;
    }

    public Post(){}

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

    public void setAuthor(User author){
        this.author = author;
    }

    public User getAuthor(){
        return author;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp + '\'' +
                ", author=" + author + '\'' +
                "}";
    }
}