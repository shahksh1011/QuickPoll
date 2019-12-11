package com.example.kshitij.quickpoll.data.model;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.util.Date;
import java.util.List;

public class Survey implements Serializable {
    public String surveyId, createdBy, surveyDescription, surveyName, timeToComplete;
    public Date surveyCreated, surveryExpiry;
    public List<?> questions;
    public Location location;

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(String timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public Survey(String surveyId, String createdBy, String surveyDescription, Date surveyCreated, Date surveryExpiry, List<?> questions, String surveyName, String timeToComplete, Object location) {
        this.surveyId = surveyId;
        this.createdBy = createdBy;
        this.surveyDescription = surveyDescription;
        this.surveyCreated = surveyCreated;
        this.surveryExpiry = surveryExpiry;
        this.questions = questions;
        this.surveyName = surveyName;
        this.timeToComplete = timeToComplete;
        this.location = (Location) location;
    }

    public List<?> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public Date getSurveyCreated() {
        return surveyCreated;
    }

    public void setSurveyCreated(Date surveyCreated) {
        this.surveyCreated = surveyCreated;
    }

    public Date getSurveryExpiry() {
        return surveryExpiry;
    }

    public void setSurveryExpiry(Date surveryExpiry) {
        this.surveryExpiry = surveryExpiry;
    }
}
