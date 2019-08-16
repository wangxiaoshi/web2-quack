package de.quackr.api;

import de.quackr.api.IOController;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.quackr.persistence.entities.Quack;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;

import de.quackr.persistence.entities.User;
import de.quackr.persistence.entities.User_;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.quackr.auth.DefaultRealm.ROLE_ADMIN;

/**
 * @author wangxiaoshi
 *
 */
@Path("/users")
public class UserController {

    static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
    static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
    static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @EJB
    private IOController ioController;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readUser(@PathParam("id") long id) {
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = ioController.getUser(id);

        if (!user.getUsername().equals(subject.getPrincipal()) && !user.isModerator() && !user.isAdmin()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(user).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAuthenticatedUser() {
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = ioController.getUser((String) subject.getPrincipal());
        return Response.ok(user).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAllUsersAsJSON() {
        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated() && subject.hasRole(ROLE_ADMIN)) {
            logger.debug("User \"{}\" is authenticated and admin. Returning all users.", subject.getPrincipal());
            List<User> users = ioController.getAllUsers();
            return Response.ok(users).build();
        } else {
            logger.debug("User is not authenticated or does not have admin role.");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User param) {
        try {
            if(!validateInput(param)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (PatternSyntaxException ex) {
            return Response.serverError().build();
        }

        final User user = new User();
        user.setSignUpTimestamp(new Date(System.currentTimeMillis()));
        user.setLastActiveTimestamp(param.getLastActiveTimestamp());
        user.setUsername(param.getUsername());
        user.setRealName(param.getRealName());
        user.setBirthday(param.getBirthday());
        user.setEmail(param.getEmail());
        user.setPasswordHash(param.getPasswordHash());
        user.setAdmin(param.isAdmin());
        user.setModerator(param.isModerator());

        ioController.createUser(user);

        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") long id) {
        if (!userLoggedInOrAdmin(id)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        logger.info("Deleting user with ID #{}.", id);
        ioController.deleteUser(id);

        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") long id, User param) {
        logger.debug("Trying to update user with ID #{}.", id);

        if (!userLoggedInOrAdmin(id)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            if(param.getUsername() != null && !validateUsername(param.getUsername())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if(param.getPasswordHash() != null && !validatePassword(param.getPasswordHash())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if(param.getEmail() != null && !validateEmail(param.getEmail())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (PatternSyntaxException ex) {
            return Response.serverError().build();
        }

        ioController.updateUser(id, param);
        return Response.ok(param).build();
    }

    public static boolean validateInput(User param) {
        return validateUsername(param.getUsername())
            && validateEmail(param.getEmail())
            && validatePassword(param.getPasswordHash());
    }

    public static boolean validateUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    public static boolean validatePassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean validateEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    private boolean userLoggedInOrAdmin(long id) {
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            return false;
        }

        User user = ioController.getUser(id);

        return user.getId() == id || user.isAdmin();
    }

}
