package com.minisocial.DTO;

import java.time.LocalDateTime;

public class Post {

    Long id;
    String content;
    LocalDateTime timestamp;
    User author;


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

    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp + '\'' +
                "}";
    }
}
