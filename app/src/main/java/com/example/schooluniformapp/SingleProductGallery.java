package com.example.schooluniformapp;

public class SingleProductGallery {

    String gallery_id,product_id,image;

    public SingleProductGallery(String gallery_id, String product_id, String image) {
        this.gallery_id = gallery_id;
        this.product_id = product_id;
        this.image = image;
    }

    public String getGallery_id() {
        return gallery_id;
    }

    public void setGallery_id(String gallery_id) {
        this.gallery_id = gallery_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
