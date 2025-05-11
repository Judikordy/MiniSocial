package com.minisocial.Service;

import com.minisocial.DTO.UserRegistrationDTO;
import com.minisocial.DTO.UserResponseDTO;
import com.minisocial.Model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "MiniSocialPU")
    private EntityManager em;

    public UserResponseDTO registerUser(UserRegistrationDTO userDTO) {
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); 
        user.setRole(userDTO.getRole());

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
                "SELECT u FROM User WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
