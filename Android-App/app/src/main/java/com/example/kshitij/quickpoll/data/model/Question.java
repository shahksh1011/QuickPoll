package com.example.kshitij.quickpoll.data.model;

import java.util.List;
import java.util.Map;

public class Question {
//    public String questionText, questionType;
    public Map<String, Object> hashMap;
//    List<String> optionsl;


    public Question(Map<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

//    public Question(String questionText, String questionType, List<String> optionsl) {
//        this.questionText = questionText;
//        this.questionType = questionType;
//        this.optionsl = optionsl;
//    }

    public Map<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(Map<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

//    public String getQuestionText() {
//        return questionText;
//    }
//
//    public void setQuestionText(String questionText) {
//        this.questionText = questionText;
//    }
//
//    public String getQuestionType() {
//        return questionType;
//    }
//
//    public void setQuestionType(String questionType) {
//        this.questionType = questionType;
//    }
//
//    public List<String> getOptionsl() {
//        return optionsl;
//    }
//
//    public void setOptionsl(List<String> optionsl) {
//        this.optionsl = optionsl;
//    }
}
