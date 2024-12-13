
package com.mycompany.controller;

import com.mycompany.dto.*;
import com.mycompany.util.TokenResponse;
import com.mycompany.model.User;
import com.mycompany.util.JwtUtil;
import com.mycompany.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.quarkus.elytron.security.common.BcryptUtil;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;


    // --- Registration Methods ---

    @POST
    @Path("/register/buyer")
    @PermitAll
    @Transactional
    public Response registerBuyer(@Valid UserDTO userDTO) {
        Response response = registerWithRole(userDTO, "BUYER");

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            userService.sendEmailVerificationToken(userDTO.email);
        }

        return response;
    }

    @POST
    @Path("/register/artist")
    @PermitAll
    @Transactional
    public Response registerArtist(@Valid UserDTO userDTO) {
        return registerWithRole(userDTO, "ARTIST");
    }

    private Response registerWithRole(UserDTO userDTO, String role) {
        if (User.find("email", userDTO.email).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT).entity("Email already registered").build();
        }

        User.add(userDTO.email, userDTO.password, role, userDTO.name);
        return Response.ok("User successfully registered with role: " + role).build();
    }

    // --- Login and Authentication Methods ---

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginDTO loginDTO) {
        User user = User.find("email", loginDTO.email).firstResult();

        if (user == null || !user.activated || !BcryptUtil.matches(loginDTO.password, user.password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials or account not activated").build();
        }

        String token = JwtUtil.generateToken(user.getEmail(), user.getRole());
        return Response.ok(new TokenResponse(token)).build();
    }

    @POST
    @Path("/request-password-reset")
    @PermitAll
    public Response requestPasswordReset(@Valid PasswordResetRequestDTO request) {
        boolean isEmailSent = userService.sendPasswordResetToken(request.getEmail());

        if (isEmailSent) {
            return Response.ok().entity("{\"message\":\"Password reset token generated and logged.\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Email not found.\"}").build();
        }
    }

    @PUT
    @Path("/change-password")
    @Transactional
    public Response changePassword(@HeaderParam("Authorization") String token, @Valid PasswordChangeDTO passwordChangeDTO) {
        String email = JwtUtil.extractEmailFromToken(token);

        boolean success = userService.changePassword(email, passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());

        if (success) {
            return Response.ok("Password changed successfully").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Current password is incorrect").build();
        }
    }

    @PUT
    @Path("/reset-password")
    @Transactional
    public Response resetPassword(@Valid PasswordResetDTO passwordResetDTO) {
        boolean isReset = userService.resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getNewPassword());

        if (isReset) {
            return Response.ok("Password reset successfully.").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid or expired token.").build();
        }
    }

    @DELETE
    @Path("/delete-account")
    @Transactional
    public Response deleteAccount(@HeaderParam("Authorization") String token) {
        String email = JwtUtil.extractEmailFromToken(token);

        boolean success = userService.deleteAccount(email);

        if (success) {
            return Response.ok("Account successfully deleted").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete account").build();
        }
    }

    // --- Email Verification Methods ---

    @GET
    @Path("/verify-email")
    @Transactional
    public Response verifyEmail(@QueryParam("token") String token) {
        boolean isVerified = userService.verifyEmail(token);

        if (isVerified) {
            return Response.ok("Email successfully verified.").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid or expired token.").build();
        }
    }
}
