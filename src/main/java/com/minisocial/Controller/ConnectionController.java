package com.minisocial.Controller;

import com.minisocial.Model.Connection;
import com.minisocial.Model.User;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/connection")
public class ConnectionController extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("MiniSocialPU");
    }

    // Handle connection creation (send request)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            if ("send".equalsIgnoreCase(action)) {
                Long requesterId = Long.parseLong(request.getParameter("requesterId"));
                Long receiverId = Long.parseLong(request.getParameter("receiverId"));

                User requester = em.find(User.class, requesterId);
                User receiver = em.find(User.class, receiverId);

                if (requester == null || receiver == null) {
                    writeResponse(response, "Invalid user ID(s).");
                    return;
                }

                Connection connection = new Connection();
                connection.setRequester(requester);
                connection.setReceiver(Collections.singletonList(receiver));
                connection.setStatus(Connection.ConnectionStatus.PENDING);
                connection.setTimestamp(LocalDateTime.now());

                tx.begin();
                em.persist(connection);
                tx.commit();

                writeResponse(response, "Connection request sent.");

            } else if ("respond".equalsIgnoreCase(action)) {
                Long connectionId = Long.parseLong(request.getParameter("connectionId"));
                String responseAction = request.getParameter("response"); // "accept" or "reject"

                Connection connection = em.find(Connection.class, connectionId);
                if (connection == null) {
                    writeResponse(response, "Connection not found.");
                    return;
                }

                Connection.ConnectionStatus newStatus = "accept".equalsIgnoreCase(responseAction)
                        ? Connection.ConnectionStatus.ACCEPTED
                        : Connection.ConnectionStatus.REJECTED;

                tx.begin();
                connection.setStatus(newStatus);
                tx.commit();

                writeResponse(response, "Connection " + responseAction + "ed.");

            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error handling connection", e);
        } finally {
            em.close();
        }
    }

    // Get connection(s) by requester or receiver
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");
        if (userIdParam == null) {
            writeResponse(response, "Missing userId.");
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            Long userId = Long.parseLong(userIdParam);
            User user = em.find(User.class, userId);
            if (user == null) {
                writeResponse(response, "User not found.");
                return;
            }

            TypedQuery<Connection> query = em.createQuery(
                    "SELECT c FROM Connection c WHERE c.requester.id = :userId OR :user MEMBER OF c.receiver",
                    Connection.class);
            query.setParameter("userId", userId);
            query.setParameter("user", user);

            List<Connection> connections = query.getResultList();

            StringBuilder sb = new StringBuilder("Connections:\n");
            for (Connection c : connections) {
                sb.append("ID: ").append(c.getId())
                        .append(", Requester: ").append(c.getRequester().getUsername())
                        .append(", Status: ").append(c.getStatus())
                        .append(", Timestamp: ").append(c.getTimestamp()).append("\n");
            }

            writeResponse(response, sb.toString());

        } finally {
            em.close();
        }
    }

    private void writeResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(message);
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

