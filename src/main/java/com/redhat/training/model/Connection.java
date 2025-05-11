package com.redhat.training.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User receiver;
    private LocalDateTime timestamp;

    public Connection() {}

    public Connection(Long id, User requester, User receiver, ConnectionStatus status, LocalDateTime timestamp) {
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


    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver){
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

    @Override
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
