package de.quackr.persistence.entities;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Quack {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    private String text;

    @ManyToOne
    private User author;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = JsonbDateFormat.TIME_IN_MILLIS)
    private Date date;

    private boolean publiclyVisible = true;

    private String backgroundColor;

    @Override
    public String toString() {
        return text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
