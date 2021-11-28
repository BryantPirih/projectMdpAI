package com.bryant.projectmdpai.Class.ExpertSystem;

public class QuestionES {
    String foreignvariable;
    String question;

    public QuestionES(String foreignvariable, String question) {
        this.foreignvariable = foreignvariable;
        this.question = question;
    }

    public String getForeignvariable() {
        return foreignvariable;
    }

    public void setForeignvariable(String foreignvariable) {
        this.foreignvariable = foreignvariable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
