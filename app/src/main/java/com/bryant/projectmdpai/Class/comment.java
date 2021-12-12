package com.bryant.projectmdpai.Class;

public class comment {
    private String id_article;
    private String id_comment;
    private String username_user;
    private String comment;

    public comment(String id_comment,String id_article, String username_user, String comment) {
        this.id_comment = id_comment;
        this.id_article = id_article;
        this.username_user = username_user;
        this.comment = comment;
    }

    public String getId_comment() {
        return id_comment;
    }

    public void setId_comment(String id_comment) {
        this.id_comment = id_comment;
    }

    public String getId_article() {
        return id_article;
    }

    public void setId_article(String id_article) {
        this.id_article = id_article;
    }

    public String getUsername_user() {
        return username_user;
    }

    public void setUsername_user(String username_user) {
        this.username_user = username_user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
