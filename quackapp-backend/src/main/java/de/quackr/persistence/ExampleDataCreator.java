package de.quackr.persistence;

import de.quackr.persistence.entities.Quack;
//import de.quackr.persistence.entities.Quack_;
import de.quackr.persistence.entities.User;
//import de.quackr.persistence.entities.User_;
import de.quackr.persistence.entities.Comment;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
//import javax.persistence.criteria.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Singleton
@Startup
public class ExampleDataCreator {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void startup() {
        createExampleData();
    }

    private void createExampleData() {
        Date now = new Date(System.currentTimeMillis());

        final User tom = new User();
        tom.setUsername("EinfachTom");
        tom.setEmail("tom@quackrmail.de");
        tom.setPasswordHash("tomtom");
        tom.setSignUpTimestamp(now);
        this.entityManager.persist(tom);

        final User max = new User();
        max.setUsername("M4xTheModerat0r");
        max.setEmail("maxthemod@nomail.xy");
        max.setPasswordHash("maxmax");
        max.setModerator(true);
        max.setBirthday(new GregorianCalendar(1990, Calendar.FEBRUARY, 1).getTime());
        max.setSignUpTimestamp(now);
        this.entityManager.persist(max);

        final User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash("admin");
        admin.setModerator(true);
        admin.setAdmin(true);
        admin.setSignUpTimestamp(new Date(0));
        this.entityManager.persist(admin);

        final Quack q1 = new Quack();
        q1.setTitle("First Quack");
        q1.setText("Quack, yeah.");
        q1.setAuthor(tom);
        q1.setDate(now);
        q1.setBackgroundColor("smoky-black");
        this.entityManager.persist(q1);

        final Quack q2 = new Quack();
        q2.setTitle("Second Quack");
        q2.setText("Quack, yeah yeah.");
        q2.setAuthor(max);
        q2.setDate(now);
        q2.setBackgroundColor("dark-lavender");
        this.entityManager.persist(q2);

        final Quack q3 = new Quack();
        q3.setTitle("Lorem Ipsum");
        q3.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt " +
            "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
            "et ea rebum.");
        q3.setAuthor(max);
        q3.setDate(now);
        q3.setBackgroundColor("mountbatten-pink");
        this.entityManager.persist(q3);

        final Quack q4 = new Quack();
        q4.setTitle("Geheimquack");
        q4.setText("Das hier ist ein privater Quack. Nur eingeloggte Benutzer dürfen mich quacken hören!");
        q4.setAuthor(admin);
        q4.setDate(now);
        q4.setBackgroundColor("bournished-brown");
        q4.setPubliclyVisible(false);
        this.entityManager.persist(q4);

        final Comment c1 = new Comment();
        c1.setText("Quack, yeah. Nice one");
        c1.setQid(q1.getId());
        c1.setAuthor(tom);
        c1.setDate(now);
        this.entityManager.persist(c1);

        final Comment c2 = new Comment();
        c2.setText("Dummy Comment. sorry!");
        c2.setQid(q1.getId());
        c2.setAuthor(tom);
        c2.setDate(now);
        this.entityManager.persist(c2);

        final Comment c3 = new Comment();
        c3.setText("Another one.. keep it up!");
        c3.setQid(q1.getId());
        c3.setAuthor(tom);
        c3.setDate(now);
        this.entityManager.persist(c3);

        final Comment c4 = new Comment();
        c4.setText("Ok now I'm done");
        c4.setQid(q3.getId());
        c4.setAuthor(tom);
        c4.setDate(now);
        this.entityManager.persist(c4);

        final Comment c5 = new Comment();
        c5.setText("Dummy Comment. sorry!");
        c5.setQid(q2.getId());
        c5.setAuthor(tom);
        c5.setDate(now);
        this.entityManager.persist(c5);

        final Comment c6 = new Comment();
        c6.setText("Another one.. keep it up!");
        c6.setQid(q3.getId());
        c6.setAuthor(tom);
        c6.setDate(now);
        this.entityManager.persist(c6);

        final Comment c7 = new Comment();
        c7.setText("Ok now I'm done");
        c7.setQid(q3.getId());
        c7.setAuthor(tom);
        c7.setDate(now);
        this.entityManager.persist(c7);

    }
}
