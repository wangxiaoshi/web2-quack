package de.quackr.api;

import de.quackr.persistence.entities.Comment;
import de.quackr.persistence.entities.Quack;
import de.quackr.persistence.entities.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.quackr.auth.DefaultRealm.ROLE_ADMIN;
import static de.quackr.auth.DefaultRealm.ROLE_MODERATOR;


/**
 * @author wangxiaoshi
 *
 */

@Path("/quacks")
public class QuackController {

//    static final String TITLE_LENGTH = "^\\W*(?:\\w+\\b\\W*){1,10}$";
//    static final String TEXT_LENGTH = "^\\\\W*(?:\\\\w+\\\\b\\\\W*){0,200}$";

    static final String[] BACKGROUND_COLORS = {
        "smoky-black",
        "dark-lavender",
        "mountbatten-pink",
        "bournished-brown",
        "rosy-brown"
    };

    private Logger logger = LoggerFactory.getLogger(QuackController.class);

    @EJB
    private IOController ioController;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readQuack(@PathParam("id") long id) {
        return Response.ok(ioController.getQuack(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quack> readAllQuacksAsJSON() {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            logger.debug("User \"{}\" is authenticated. Returning ALL quacks.", subject.getPrincipal());
            return ioController.getAllQuacks();
        } else {
            logger.debug("Returning public quacks.");
            return ioController.getAllPublicQuacks();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuack(Quack param) {
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            logger.debug("Unauthenticated user tried to post a quack.");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = ioController.getUser((String) subject.getPrincipal());

        try {
            if(!validateTitle(param.getTitle()) || !validateText(param.getText())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (PatternSyntaxException ex) {
            return Response.serverError().build();
        }

        logger.debug("User \"{}\" (#{}) is creating a quack.", user.getUsername(), user.getId());

        final Quack quack = new Quack();
        quack.setDate(new Date(System.currentTimeMillis()));
        quack.setAuthor(user);
        quack.setPubliclyVisible(param.isPubliclyVisible());
        quack.setTitle(param.getTitle());
        quack.setText(param.getText());

        quack.setBackgroundColor(BACKGROUND_COLORS[(int)(Math.random()*BACKGROUND_COLORS.length)]);

        ioController.createQuack(quack);

        return Response.ok(quack).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteQuack(@PathParam("id") long id) {
        if (!authorLoggedInOrModerator(id)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        ioController.deleteQuack(id);

        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuack(@PathParam("id") long id, Quack param) {
        if (!authorLoggedInOrModerator(id)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            if(!validateInput(param)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (PatternSyntaxException ex) {
            return Response.serverError().build();
        }

        param.setDate(new Date(System.currentTimeMillis()));
        ioController.updateQuack(id, param);
        return Response.ok(param).build();
    }

//-----------------Methoden fuer Kommentare----------------------------
    @GET
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> readAllCommentsFor(@PathParam("id") long id) {
        return ioController.getAllCommentsFor(id);
    }

    @POST
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("id") long id, Comment param) {
        //TODO: HOW TO get the current logging user
    	Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            logger.debug("Unauthenticated user tried to comment quack with id ({})", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = ioController.getUser((String) subject.getPrincipal());

        try {
            if(!validateText(param.getText())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (PatternSyntaxException ex) {
            return Response.serverError().build();
        }

        logger.debug("User \"{}\" (#{}) is creating a comment.", user.getUsername(), user.getId());

        final Comment comment = new Comment();
        comment.setDate(new Date(System.currentTimeMillis()));
        comment.setAuthor(user);
        comment.setQid(id);
        comment.setText(param.getText());

        ioController.createComment(comment);
        return Response.ok(comment).build();
    }

    @DELETE
    @Path("/{qid}/comments/{cid}")
    public void deleteComment(@PathParam("cid") long id) {
    	ioController.deleteComment(id);
    }



//---------------------------------------------------------------------
    public static boolean validateInput(Quack param) {
        return validateTitle(param.getTitle())
            && validateText(param.getText());
    }

    public static boolean validateTitle(String title) {
        return title != null && title.length() >= 3 && title.length() <= 20;
    }

    public static boolean validateText(String text) {
        return text != null && text.length() >= 6 && text.length() <= 300;
    }


    private boolean authorLoggedInOrModerator(long quackId) {
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            return false;
        }

        Quack quack = ioController.getQuack(quackId);

        return quack.getAuthor().getUsername().equals(subject.getPrincipal()) || subject.hasRole(ROLE_MODERATOR);
    }

}
