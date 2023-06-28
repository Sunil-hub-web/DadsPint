package com.example.dadspint;

public class OrderDetails_Model {

    String orders_id,productname,variation_id,qty,img,price,shipping_charge,order_id,payment_mode,status,
            created_date,user_id;

    public OrderDetails_Model(String orders_id, String productname, String variation_id, String qty, String img,
                              String price, String shipping_charge, String order_id, String payment_mode,
                              String status,String created_date,String user_id) {
        this.orders_id = orders_id;
        this.productname = productname;
        this.variation_id = variation_id;
        this.qty = qty;
        this.img = img;
        this.price = price;
        this.shipping_charge = shipping_charge;
        this.order_id = order_id;
        this.payment_mode = payment_mode;
        this.status = status;
        this.created_date = created_date;
        this.user_id = user_id;
    }

    public String getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(String orders_id) {
        this.orders_id = orders_id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(String variation_id) {
        this.variation_id = variation_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
