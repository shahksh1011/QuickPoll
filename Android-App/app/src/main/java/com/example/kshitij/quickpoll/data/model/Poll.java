package com.example.kshitij.quickpoll.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Poll implements Serializable {

    private String id;
    private String name;
    private String createdBy;
    private Date createdDate;
    private String description;
    private boolean lockQuestion;
    private long displayQuestion;
    public List<Question> questionList;

    public Poll(String id, String name, String createdBy, Date createdDate, String description, boolean lockQuestion, long displayQuestion, List<Question> questionList) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.description = description;
        this.lockQuestion = lockQuestion;
        this.displayQuestion = displayQuestion;
        this.questionList = questionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLockQuestion() {
        return lockQuestion;
    }

    public void setLockQuestion(boolean lockQuestion) {
        this.lockQuestion = lockQuestion;
    }

    public long getDisplayQuestion() {
        return displayQuestion;
    }

    public void setDisplayQuestion(long displayQuestion) {
        this.displayQuestion = displayQuestion;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
