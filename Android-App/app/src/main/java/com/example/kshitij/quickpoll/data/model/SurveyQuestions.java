package com.example.kshitij.quickpoll.data.model;

import java.util.List;

public class SurveyQuestions {
    String questionText, questionType;
    List<String> options;

    public SurveyQuestions(String questionText, String questionType, List<String> options) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
    }

    public SurveyQuestions(String questionText, String questionType) {
        this.questionText = questionText;
        this.questionType = questionType;
    }
}
