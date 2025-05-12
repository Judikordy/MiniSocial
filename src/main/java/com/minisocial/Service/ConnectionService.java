package com.minisocial.Service;

import com.minisocial.Model.Connection;
import com.minisocial.Model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ConnectionService {

    @PersistenceContext(unitName = "MiniSocialPU")
    private EntityManager em;

    // Create a new connection
    public Connection createConnection(User requester, List<User> receivers) {
        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setReceiver(receivers);
        connection.setStatus(Connection.ConnectionStatus.PENDING);
        connection.setTimestamp(LocalDateTime.now());

        em.persist(connection);
        return connection;
    }

    // Get a connection by ID
    public Connection getConnectionById(Long id) {
        return em.find(Connection.class, id);
    }

    // Get all connections for a specific user (either requester or receiver)
    public List<Connection> getConnectionsForUser(Long userId) {
        return em.createQuery(
                        "SELECT c FROM Connection c WHERE c.requester.id = :userId OR EXISTS (" +
                                "SELECT r FROM c.receiver r WHERE r.id = :userId)", Connection.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // Update the status of a connection (ACCEPTED, REJECTED)
    public boolean updateConnectionStatus(Long connectionId, Connection.ConnectionStatus newStatus) {
        Connection connection = em.find(Connection.class, connectionId);
        if (connection == null) return false;

        connection.setStatus(newStatus);
        return true;
    }

    // Delete a connection
    public boolean deleteConnection(Long id) {
        Connection connection = em.find(Connection.class, id);
        if (connection == null) return false;

        em.remove(connection);
        return true;
    }

    // Get all pending connections where the user is a receiver
    public List<Connection> getPendingRequestsForUser(Long userId) {
        return em.createQuery(
                        "SELECT c FROM Connection c WHERE c.status = :status AND EXISTS (" +
                                "SELECT r FROM c.receiver r WHERE r.id = :userId)", Connection.class)
                .setParameter("status", Connection.ConnectionStatus.PENDING)
                .setParameter("userId", userId)
                .getResultList();
    }
}
