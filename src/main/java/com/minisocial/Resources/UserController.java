package com.minisocial.Resources;

import com.minisocial.DTO.UserRegistrationDTO;
import com.minisocial.DTO.UserResponseDTO;
import com.minisocial.Service.UserService;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @EJB
    private UserService userService;

    @POST
    @Path("/register")
    public Response registerUser(UserRegistrationDTO userDTO) {
        UserResponseDTO createdUser = userService.registerUser(userDTO);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @GET
    @Path("/{email}")
    public Response getUserByEmail(@PathParam("email") String email) {
        var user = userService.findByEmail(email);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var response = new UserResponseDTO(
                user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return Response.ok(response).build();
    }

    @GET
    @Path("/id/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        var user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var response = new UserResponseDTO(
                user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return Response.ok(response).build();
    }

    @GET
    @Path("/all")
    public Response getAllUsers() {
        var users = userService.getAllUsers();
        var response = users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole()))
                .toList();
        return Response.ok(response).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UserRegistrationDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedUser).build();
    }

    @PUT
    @Path("/{id}/password")
    public Response updatePassword(@PathParam("id") Long id, String newPassword) {
        boolean isUpdated = userService.updatePassword(id, newPassword);
        if (!isUpdated) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/search/{keyword}")
    public Response searchUsersByName(@PathParam("keyword") String keyword) {
        var users = userService.searchUsersByName(keyword);
        var response = users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole()))
                .toList();
        return Response.ok(response).build();
    }
}