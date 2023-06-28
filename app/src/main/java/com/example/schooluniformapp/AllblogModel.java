package com.example.schooluniformapp;

public class AllblogModel {

    String blog_id,name,title,message,category,image,date;

    public AllblogModel(String blog_id, String name, String title, String message, String category,
                        String image, String date) {

        this.blog_id = blog_id;
        this.name = name;
        this.title = title;
        this.message = message;
        this.category = category;
        this.image = image;
        this.date = date;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
