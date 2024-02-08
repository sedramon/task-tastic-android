package rs.ac.tasktastic.dto;

import java.util.Date;

public class Task {
    private String id, title, description;
    private User user;
    private Date createdDate, endDate;
    private boolean isFinished = false;

    public Task() {
    }

    public Task(String id, User user, String title, String description, Date createdDate, Date endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
        this.createdDate = createdDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
