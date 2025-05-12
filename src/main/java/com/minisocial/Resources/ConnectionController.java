package com.minisocial.Resources;

import com.minisocial.DTO.ConnectionRequestDTO;
import com.minisocial.DTO.ConnectionResponseDTO;
import com.minisocial.Service.ConnectionService;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/connections")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({ "ADMIN", "USER" })
public class ConnectionController {

    @EJB
    private ConnectionService connectionService;

    // Send a connection request
    @POST
    @Path("/request")
    @RolesAllowed({ "USER", "ADMIN" })
    public Response sendConnectionRequest(ConnectionRequestDTO requestDTO) {
        ConnectionResponseDTO connection = connectionService.sendRequest(requestDTO);
        return Response.status(Response.Status.CREATED).entity(connection).build();
    }

    // Accept a connection request
    @PUT
    @Path("/accept/{requestId}")
    @RolesAllowed({ "USER", "ADMIN" })
    public Response acceptConnectionRequest(@PathParam("requestId") Long requestId) {
        boolean success = connectionService.acceptRequest(requestId);
        if (!success) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    // Reject a connection request
    @PUT
    @Path("/reject/{requestId}")
    @RolesAllowed({ "USER", "ADMIN" })
    public Response rejectConnectionRequest(@PathParam("requestId") Long requestId) {
        boolean success = connectionService.rejectRequest(requestId);
        if (!success) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    // Get all connections for a user
    @GET
    @Path("/user/{userId}")
    @RolesAllowed({ "USER", "ADMIN" })
    public Response getUserConnections(@PathParam("userId") Long userId) {
        List<ConnectionResponseDTO> connections = connectionService.getConnectionsForUser(userId);
        return Response.ok(connections).build();
    }

    // Delete a connection
    @DELETE
    @Path("/{connectionId}")
    @RolesAllowed({ "USER", "ADMIN" })
    public Response deleteConnection(@PathParam("connectionId") Long connectionId) {
        boolean deleted = connectionService.deleteConnection(connectionId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
