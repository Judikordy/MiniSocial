package com.minisocial.DTO;
import com.minisocial.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ConnectionDTO {

    private Long id;

    private ConnectionStatus status;

    private User requester;

    private List<User> receiver;
    private LocalDateTime timestamp;

    public ConnectionDTO() {}

    public ConnectionDTO(Long id, User requester, List<User> receiver, ConnectionStatus status, LocalDateTime timestamp) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
        this.timestamp = timestamp;
    }

    public User getRequester(){
        return requester;
    }

    public void setRequester(User requester){
        this.requester = requester;
    }


    public List<User> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<User> receiver){
        this.receiver = receiver;
    }

    public ConnectionStatus getStatus(){
        return this.status;
    }

    public void setStatus(ConnectionStatus status){
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public enum ConnectionStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", requester='" + requester + '\'' +
                ", receiver='" + receiver + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp + '\'' +
                "}";
    }
}