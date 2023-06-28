package com.example.schooluniformapp;

public class OrderListModel {

    String order_id,order_date,status,total_qty,total_price;

    public OrderListModel(String order_id, String order_date, String status, String total_qty, String total_price) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.status = status;
        this.total_qty = total_qty;
        this.total_price = total_price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
