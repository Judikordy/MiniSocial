package com.minisocial.Service;

import com.minisocial.Model.Post;
import com.minisocial.Model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostService {

    @PersistenceContext(unitName = "MiniSocialPU")
    private EntityManager em;

    // Create a new post
    public Post createPost(User author, String content) {
        Post post = new Post();
        post.setAuthor(author);
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());

        em.persist(post);
        return post;
    }

    // Get a post by its ID
    public Post getPostById(Long id) {
        return em.find(Post.class, id);
    }

    // Get all posts by a specific user
    public List<Post> getPostsByAuthor(Long authorId) {
        return em.createQuery(
                        "SELECT p FROM Post p WHERE p.author.id = :authorId ORDER BY p.timestamp DESC", Post.class)
                .setParameter("authorId", authorId)
                .getResultList();
    }

    // Get all posts (e.g., for a feed)
    public List<Post> getAllPosts() {
        return em.createQuery("SELECT p FROM Post p ORDER BY p.timestamp DESC", Post.class)
                .getResultList();
    }

    // Update the content of a post
    public boolean updatePost(Long postId, String newContent) {
        Post post = em.find(Post.class, postId);
        if (post == null) return false;

        post.setContent(newContent);
        post.setTimestamp(LocalDateTime.now()); // Optional: update timestamp on edit
        return true;
    }

    // Delete a post
    public boolean deletePost(Long postId) {
        Post post = em.find(Post.class, postId);
        if (post == null) return false;

        em.remove(post);
        return true;
    }
}
