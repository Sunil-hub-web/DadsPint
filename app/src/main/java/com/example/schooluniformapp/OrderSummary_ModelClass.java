package com.example.schooluniformapp;

public class OrderSummary_ModelClass {

    String product_id,product_img,product_name,product_weight,sale_price,quantity;

    public OrderSummary_ModelClass(String product_id, String product_img, String product_name,
                                   String product_weight, String sale_price, String quantity) {

        this.product_id = product_id;
        this.product_img = product_img;
        this.product_name = product_name;
        this.product_weight = product_weight;
        this.sale_price = sale_price;
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
