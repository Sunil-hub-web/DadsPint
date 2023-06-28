package com.example.schooluniformapp;

public class ViewCartModel {

    String cart_id,user_id,product_id,product_name,quantity,Product_price,variation_id,product_type,
            primary_image,variation_names;

    public ViewCartModel(String cart_id, String user_id, String product_id, String product_name, String quantity,
                         String product_price, String variation_id, String product_type, String primary_image,
                         String variation_names) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        Product_price = product_price;
        this.variation_id = variation_id;
        this.product_type = product_type;
        this.primary_image = primary_image;
        this.variation_names = variation_names;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(String product_price) {
        Product_price = product_price;
    }

    public String getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(String variation_id) {
        this.variation_id = variation_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public String getVariation_names() {
        return variation_names;
    }

    public void setVariation_names(String variation_names) {
        this.variation_names = variation_names;
    }
}