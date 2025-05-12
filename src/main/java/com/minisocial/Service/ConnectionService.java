package com.minisocial.Service;

import com.minisocial.DTO.ConnectionRequestDTO;
import com.minisocial.DTO.ConnectionResponseDTO;

import java.util.List;

public interface ConnectionService {
    ConnectionResponseDTO sendRequest(ConnectionRequestDTO requestDTO);
    boolean acceptRequest(Long requestId);
    boolean rejectRequest(Long requestId);
    List<ConnectionResponseDTO> getConnectionsForUser(Long userId);
    boolean deleteConnection(Long connectionId);
}
