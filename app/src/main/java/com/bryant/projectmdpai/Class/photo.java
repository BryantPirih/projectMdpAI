package com.bryant.projectmdpai.Class;

public class photo {
    private String id;
    private byte[] image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public photo(String id, byte[] image) {
        this.id = id;
        this.image = image;
    }
}
