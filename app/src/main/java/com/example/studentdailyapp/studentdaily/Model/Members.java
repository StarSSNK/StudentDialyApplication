package com.example.studentdailyapp.studentdaily.Model;

public class Members {

    public String email, username,image_uri,id_user;
    public Boolean activated;

    public Members(){}


    public Members(String email, String username, Boolean activated,String id_user) {
        this.email = email;
        this.username = username;
        this.activated = activated;
        this.id_user = id_user;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
