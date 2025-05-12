package com.minisocial.Resources;

import com.minisocial.Model.Post;
import com.minisocial.Model.User;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet("/post")
public class PostController extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("MiniSocialPU");
    }

    // Create a new post
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Long authorId = Long.parseLong(request.getParameter("authorId"));
            String content = request.getParameter("content");

            User author = em.find(User.class, authorId);
            if (author == null) {
                writeResponse(response, "Author not found.");
                return;
            }

            Post post = new Post(content, LocalDateTime.now());
            post.setAuthor(author);

            tx.begin();
            em.persist(post);
            tx.commit();

            writeResponse(response, "Post created successfully. ID: " + post.getId());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error in doPost", e);
        } finally {
            em.close();
        }
    }

    // Get a post by ID
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Post post = em.find(Post.class, id);

            if (post != null) {
                writeResponse(response, "Post ID: " + post.getId() +
                        "\nAuthor: " + post.getAuthor().getUsername() +
                        "\nContent: " + post.getContent() +
                        "\nTimestamp: " + post.getTimestamp());
            } else {
                writeResponse(response, "Post not found.");
            }

        } finally {
            em.close();
        }
    }

    // Update a post
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Post post = em.find(Post.class, id);

            if (post != null) {
                String content = request.getParameter("content");

                tx.begin();
                if (content != null) {
                    post.setContent(content);
                    post.setTimestamp(LocalDateTime.now());
                }
                tx.commit();

                writeResponse(response, "Post updated successfully.");
            } else {
                writeResponse(response, "Post not found.");
            }

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error in doPut", e);
        } finally {
            em.close();
        }
    }

    // Delete a post
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Post post = em.find(Post.class, id);

            if (post != null) {
                tx.begin();
                em.remove(post);
                tx.commit();

                writeResponse(response, "Post deleted successfully.");
            } else {
                writeResponse(response, "Post not found.");
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
