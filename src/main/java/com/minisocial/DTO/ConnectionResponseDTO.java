package com.minisocial.DTO;

import com.minisocial.Model.Connection.ConnectionStatus;

import java.time.LocalDateTime;

public class ConnectionResponseDTO {
    private Long id;
    private String requesterUsername;
    private String receiverUsername;
    private ConnectionStatus status;
    private LocalDateTime timestamp;

    public ConnectionResponseDTO() {}

    public ConnectionResponseDTO(Long id, String requesterUsername, String receiverUsername,
                                 ConnectionStatus status, LocalDateTime timestamp) {
        this.id = id;
        this.requesterUsername = requesterUsername;
        this.receiverUsername = receiverUsername;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
