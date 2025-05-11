package com.minisocial.DTO;

import com.minisocial.model.User;

import java.time.LocalDateTime;

public class PostDTO {

    Long id;
    String content;
    LocalDateTime timestamp;
    private User author;


    public PostDTO(String content, LocalDateTime timestamp){
        this.content = content;
        this.timestamp = timestamp;
    }

    public PostDTO(){}

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
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp + '\'' +
                ", author=" + author + '\'' +
                "}";
    }
}