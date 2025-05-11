package com.minisocial.Controller;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class User extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("MiniSocialPU");
    }


    //Register
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            if ("register".equals(action)) {
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String role = "user";

                com.minisocial.Model.User user = new com.minisocial.Model.User(username, email, password, role);

                tx.begin();
                em.persist(user);
                tx.commit();

                writeResponse(response, "User registered successfully! ID: " + user.getId());

            } else if ("login".equals(action)) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                TypedQuery<com.minisocial.Model.User> query = em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email AND u.password = :password",
                        com.minisocial.Model.User.class);
                query.setParameter("email", email);
                query.setParameter("password", password);

                com.minisocial.Model.User user = query.getResultStream().findFirst().orElse(null);

                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", user.getId());
                    writeResponse(response, "Login successful! Welcome, " + user.getUsername());
                } else {
                    writeResponse(response, "Invalid email or password.");
                }
            }

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error in doPost", e);
        } finally {
            em.close();
        }
    }

    //Get
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            writeResponse(response, "User ID is required.");
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            Long id = Long.parseLong(idParam);
            com.minisocial.Model.User user = em.find(com.minisocial.Model.User.class, id);

            if (user != null) {
                writeResponse(response, "User profile:\nUsername: " + user.getUsername()
                        + "\nEmail: " + user.getEmail()
                        + "\nRole: " + user.getRole());
            } else {
                writeResponse(response, "User not found.");
            }
        } finally {
            em.close();
        }
    }

    //Update
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            com.minisocial.Model.User user = em.find(com.minisocial.Model.User.class, id);

            if (user != null) {
                tx.begin();
                if (request.getParameter("username") != null)
                    user.setUsername(request.getParameter("username"));
                if (request.getParameter("email") != null)
                    user.setEmail(request.getParameter("email"));
                if (request.getParameter("password") != null)
                    user.setPassword(request.getParameter("password"));
                if (request.getParameter("role") != null)
                    user.setRole(request.getParameter("role"));
                tx.commit();
                writeResponse(response, "User updated successfully.");
            } else {
                writeResponse(response, "User not found.");
            }

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error in doPut", e);
        } finally {
            em.close();
        }
    }

    //delete
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            com.minisocial.Model.User user = em.find(com.minisocial.Model.User.class, id);

            if (user != null) {
                tx.begin();
                em.remove(user);
                tx.commit();
                writeResponse(response, "User deleted successfully.");
            } else {
                writeResponse(response, "User not found.");
            }

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error in doDelete", e);
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