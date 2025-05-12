package com.minisocial.Service;

import com.minisocial.DTO.UserRegistrationDTO;
import com.minisocial.DTO.UserResponseDTO;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class User {

    @PersistenceContext(unitName = "MiniSocialPU")
    private EntityManager em;

    public UserResponseDTO registerUser(UserRegistrationDTO dto) {
        com.minisocial.Model.User user = new com.minisocial.Model.User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        em.persist(user);

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public com.minisocial.Model.User findByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", com.minisocial.Model.User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public com.minisocial.Model.User findById(Long id) {
        return em.find(com.minisocial.Model.User.class, id);
    }

    public List<com.minisocial.Model.User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", com.minisocial.Model.User.class).getResultList();
    }

    public UserResponseDTO updateUser(Long id, UserRegistrationDTO dto) {
        com.minisocial.Model.User user = em.find(com.minisocial.Model.User.class, id);
        if (user == null) return null;

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public boolean updatePassword(Long userId, String newPassword) {
        com.minisocial.Model.User user = em.find(com.minisocial.Model.User.class, userId);
        if (user == null) return false;

        user.setPassword(newPassword);
        return true;
    }

    public boolean deleteUser(Long id) {
        com.minisocial.Model.User user = em.find(com.minisocial.Model.User.class, id);
        if (user == null) return false;

        em.remove(user);
        return true;
    }

    public boolean emailExists(String email) {
        return em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0;
    }

    public boolean usernameExists(String username) {
        return em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult() > 0;
    }

    public List<com.minisocial.Model.User> searchUsersByName(String keyword) {
        return em.createQuery("SELECT u FROM User u WHERE u.username LIKE :kw", com.minisocial.Model.User.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }
}