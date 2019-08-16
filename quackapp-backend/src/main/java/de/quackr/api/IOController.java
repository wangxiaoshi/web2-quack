package de.quackr.api;

import de.quackr.persistence.entities.Comment;
import de.quackr.persistence.entities.Comment_;
import de.quackr.persistence.entities.Quack;
import de.quackr.persistence.entities.Quack_;
import de.quackr.persistence.entities.User;
import de.quackr.persistence.entities.User_;

import javax.ejb.Stateless;
import javax.persistence.criteria.*;

import java.time.format.DateTimeParseException;
import java.util.List;

import javax.persistence.*;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class IOController {

    @PersistenceContext
    private EntityManager entityManager;

    public void createUser(User newUser) throws DateTimeParseException {
        this.entityManager.persist(newUser);
    }

    public void createQuack(Quack newQuack) {
        this.entityManager.persist(newQuack);
    }

    public void updateUser(long id, User user) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

        // create update
        CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);

        // set root class
        Root<User> e = update.from(User.class);

        // set update and where clause
        if (user.getUsername() != null)             update.set(User_.username, user.getUsername());
        if (user.getEmail() != null)                update.set(User_.email, user.getEmail());
        if (user.getPasswordHash() != null)         update.set(User_.passwordHash, user.getPasswordHash());
        if (user.getRealName() != null)             update.set(User_.realName, user.getRealName());
        if (user.getBirthday() != null)             update.set(User_.birthday, user.getBirthday());
        if (user.getLastActiveTimestamp() != null)  update.set(User_.lastActiveTimestamp, user.getLastActiveTimestamp());

        update.set(User_.moderator, user.isModerator());

        update.where(cb.equal(e.get(User_.id), id));

        // perform update
        this.entityManager.createQuery(update).executeUpdate();

    }

    public void deleteUser(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

        // create delete
        CriteriaDelete<User> delete = cb.createCriteriaDelete(User.class);

        // set root class
        Root<User> e = delete.from(User.class);

        // set where clause
        delete.where(cb.equal(e.get(User_.id), id));

        // perform delete
        this.entityManager.createQuery(delete).executeUpdate();
    }

    public User getUser(long id) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root<User> root = query.from(User.class);

        final Predicate predicate = builder.equal(root.get(User_.id), id);

        query.select(root).where(predicate);

        // getSingleResult() does not work, because it throws an Exception if there is no result.
        List<User> resultList = this.entityManager.createQuery(query).setMaxResults(1).getResultList();

        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    public User getUser(String username) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root<User> root = query.from(User.class);

        final Predicate predicate = builder.equal(root.get(User_.username), username);

        query.select(root).where(predicate);

        List<User> resultList = this.entityManager.createQuery(query).setMaxResults(1).getResultList();

        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    public void updateQuack(long id, Quack quack) {

        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

        // create update
        CriteriaUpdate<Quack> update = cb.createCriteriaUpdate(Quack.class);

        // set root class
        Root<Quack> root = update.from(Quack.class);

        // set update and where clause
        update.set(Quack_.title, quack.getTitle());
        update.set(Quack_.text, quack.getText());
        update.set(Quack_.date, quack.getDate());
        update.set(Quack_.publiclyVisible, quack.isPubliclyVisible());
        update.where(cb.equal(root.get(Quack_.id), id));

        // perform update
        this.entityManager.createQuery(update).executeUpdate();
    }

    public void deleteQuack(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

        // create delete
        CriteriaDelete<Quack> delete = cb.createCriteriaDelete(Quack.class);

        // set root class
        Root<Quack> e = delete.from(Quack.class);

        // set where clause
        delete.where(cb.equal(e.get(Quack_.id), id));

        // perform delete
        this.entityManager.createQuery(delete).executeUpdate();
    }

    public Quack getQuack(long id) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Quack> query = builder.createQuery(Quack.class);
        final Root<Quack> root = query.from(Quack.class);

        final Predicate predicate = builder.equal(root.get(Quack_.id), id);

        query.select(root).where(predicate);

        // getSingleResult() does not work, because it throws an Exception if there is no result.
        List<Quack> resultList = this.entityManager.createQuery(query).setMaxResults(1).getResultList();

        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    public List<User> getAllUsers() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        cq.select(user);
        TypedQuery<User> q = entityManager.createQuery(cq);
        List<User> allUsers = q.getResultList();
        return allUsers;

    }

    public List<Quack> getAllQuacks() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Quack> cq = cb.createQuery(Quack.class);
        Root<Quack> quack = cq.from(Quack.class);
        cq.select(quack);
        TypedQuery<Quack> q = entityManager.createQuery(cq);
        List<Quack> allQuacks = q.getResultList();
        return allQuacks;
    }

    public List<Quack> getAllPublicQuacks() {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Quack> cq = cb.createQuery(Quack.class);
        final Root<Quack> quack = cq.from(Quack.class);

        final Predicate predicate = cb.isTrue(quack.get(Quack_.publiclyVisible));

        cq.select(quack).where(predicate);

        return entityManager.createQuery(cq).getResultList();
    }

// Hier fängt mit allen Comment Methoden an!!!


    public void createComment(Comment newComment) {
        this.entityManager.persist(newComment);
    }

    public List<Comment> getAllCommentsFor(long id) {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
        Root<Comment> comment = cq.from(Comment.class);

        final Predicate predicate = cb.equal(comment.get(Comment_.qid), id);

        cq.select(comment).where(predicate);
        TypedQuery<Comment> c = entityManager.createQuery(cq);
        List<Comment> allComments = c.getResultList();
        return allComments;
	}

    public void deleteComment(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

        // create delete
        CriteriaDelete<Comment> delete = cb.createCriteriaDelete(Comment.class);

        // set root class
        Root<Comment> e = delete.from(Comment.class);

        // set where clause
        delete.where(cb.equal(e.get(Comment_.id), id));

        // perform delete
        this.entityManager.createQuery(delete).executeUpdate();
    }

}
