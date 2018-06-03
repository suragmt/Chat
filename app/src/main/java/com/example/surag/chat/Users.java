package com.example.surag.chat;

public class Users {

    String name;
    String status;
    String image;
    String thumb;
    public Users(){}

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Users(String name, String status, String image, String thumb) {

        this.name = name;
        this.status = status;
        this.image = image;
        this.thumb=thumb;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }
}
