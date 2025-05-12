package com.minisocial.DTO;

public class ConnectionRequestDTO {
    private Long requesterId;
    private Long receiverId;

    public ConnectionRequestDTO() {}

    public ConnectionRequestDTO(Long requesterId, Long receiverId) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
