package de.quackr.persistence.entities;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String username;

    private String email;

    private String passwordHash;

    private String realName;

    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = JsonbDateFormat.TIME_IN_MILLIS)
    private Date birthday;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = JsonbDateFormat.TIME_IN_MILLIS)
    private Date signUpTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = JsonbDateFormat.TIME_IN_MILLIS)
    private Date lastActiveTimestamp;

    private boolean admin;

    private boolean moderator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getSignUpTimestamp() {
        return signUpTimestamp;
    }

    public void setSignUpTimestamp(Date signUpDate) {
        this.signUpTimestamp = signUpDate;
    }

    public Date getLastActiveTimestamp() {
        return lastActiveTimestamp;
    }

    public void setLastActiveTimestamp(Date lastActiveDate) {
        this.lastActiveTimestamp = lastActiveDate;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isModerator() {
        return moderator;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }
}
