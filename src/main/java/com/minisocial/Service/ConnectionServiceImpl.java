package com.minisocial.Service;

import com.minisocial.DTO.ConnectionRequestDTO;
import com.minisocial.DTO.ConnectionResponseDTO;
import com.minisocial.Model.Connection;
import com.minisocial.Model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ConnectionServiceImpl implements ConnectionService {

    @PersistenceContext(unitName = "MiniSocialPU")
    private EntityManager em;

    @Override
    public ConnectionResponseDTO sendRequest(ConnectionRequestDTO requestDTO) {
        User requester = em.find(User.class, requestDTO.getRequesterId());
        User receiver = em.find(User.class, requestDTO.getReceiverId());

        if (requester == null || receiver == null) {
            throw new IllegalArgumentException("Invalid user ID(s).");
        }

        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setReceiver(receiver);
        connection.setStatus(Connection.ConnectionStatus.PENDING);
        connection.setTimestamp(LocalDateTime.now());

        em.persist(connection);

        return toDTO(connection);
    }

    @Override
    public boolean acceptRequest(Long requestId) {
        Connection connection = em.find(Connection.class, requestId);
        if (connection == null || connection.getStatus() != Connection.ConnectionStatus.PENDING) {
            return false;
        }

        connection.setStatus(Connection.ConnectionStatus.ACCEPTED);
        connection.setTimestamp(LocalDateTime.now());
        return true;
    }

    @Override
    public boolean rejectRequest(Long requestId) {
        Connection connection = em.find(Connection.class, requestId);
        if (connection == null || connection.getStatus() != Connection.ConnectionStatus.PENDING) {
            return false;
        }

        connection.setStatus(Connection.ConnectionStatus.REJECTED);
        connection.setTimestamp(LocalDateTime.now());
        return true;
    }

    @Override
    public List<ConnectionResponseDTO> getConnectionsForUser(Long userId) {
        TypedQuery<Connection> query = em.createQuery(
                "SELECT c FROM Connection c WHERE (c.requester.id = :id OR c.receiver.id = :id) AND c.status = :status",
                Connection.class);
        query.setParameter("id", userId);
        query.setParameter("status", Connection.ConnectionStatus.ACCEPTED);

        List<Connection> connections = query.getResultList();
        return connections.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteConnection(Long connectionId) {
        Connection connection = em.find(Connection.class, connectionId);
        if (connection == null) {
            return false;
        }
        em.remove(connection);
        return true;
    }

    private ConnectionResponseDTO toDTO(Connection c) {
        return new ConnectionResponseDTO(
                c.getId(),
                c.getRequester().getUsername(),
                c.getReceiver().getUsername(),
                c.getStatus(),
                c.getTimestamp()
        );
    }
}
