package com.minisocial.service;

import com.minisocial.DTO.UserRegistrationDTO;
import com.minisocial.DTO.UserResponseDTO;
import com.minisocial.model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "MiniSocialPU")
    private EntityManager em;

    public UserResponseDTO registerUser(UserRegistrationDTO dto) {
        User user = new User();
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

    public User findByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public UserResponseDTO updateUser(Long id, UserRegistrationDTO dto) {
        User user = em.find(User.class, id);
        if (user == null) return null;

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public boolean updatePassword(Long userId, String newPassword) {
        User user = em.find(User.class, userId);
        if (user == null) return false;

        user.setPassword(newPassword);
        return true;
    }

    public boolean deleteUser(Long id) {
        User user = em.find(User.class, id);
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

    public List<User> searchUsersByName(String keyword) {
        return em.createQuery("SELECT u FROM User u WHERE u.username LIKE :kw", User.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }
}
