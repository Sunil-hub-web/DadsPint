package co.in.dadspint;

public class SingleProductModel {

    String product_id,product_name,primary_image,vendor_id,trendingg,today_dealing_date_time,product_type,regular_price,
            sales_price,stock,city_name,city_id,school_id,school_name,class_id,class_name,brands_id,brands_name,description;

    public SingleProductModel(String product_id, String product_name, String primary_image, String vendor_id,
                              String trendingg, String today_dealing_date_time, String product_type, String regular_price,
                              String sales_price, String stock, String city_name, String city_id, String school_id,
                              String school_name, String class_id, String class_name, String brands_id, String brands_name,
                              String description) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.primary_image = primary_image;
        this.vendor_id = vendor_id;
        this.trendingg = trendingg;
        this.today_dealing_date_time = today_dealing_date_time;
        this.product_type = product_type;
        this.regular_price = regular_price;
        this.sales_price = sales_price;
        this.stock = stock;
        this.city_name = city_name;
        this.city_id = city_id;
        this.school_id = school_id;
        this.school_name = school_name;
        this.class_id = class_id;
        this.class_name = class_name;
        this.brands_id = brands_id;
        this.brands_name = brands_name;
        this.description = description;
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

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getTrendingg() {
        return trendingg;
    }

    public void setTrendingg(String trendingg) {
        this.trendingg = trendingg;
    }

    public String getToday_dealing_date_time() {
        return today_dealing_date_time;
    }

    public void setToday_dealing_date_time(String today_dealing_date_time) {
        this.today_dealing_date_time = today_dealing_date_time;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getBrands_id() {
        return brands_id;
    }

    public void setBrands_id(String brands_id) {
        this.brands_id = brands_id;
    }

    public String getBrands_name() {
        return brands_name;
    }

    public void setBrands_name(String brands_name) {
        this.brands_name = brands_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
