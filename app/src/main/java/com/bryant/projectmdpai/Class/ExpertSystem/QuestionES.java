package com.bryant.projectmdpai.Class.ExpertSystem;

public class QuestionES {
    String foreginvariable;
    String question;

    public QuestionES(String foreginvariable, String question) {
        this.foreginvariable = foreginvariable;
        this.question = question;
    }

    public String getForeginvariable() {
        return foreginvariable;
    }

    public void setForeginvariable(String foreginvariable) {
        this.foreginvariable = foreginvariable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
