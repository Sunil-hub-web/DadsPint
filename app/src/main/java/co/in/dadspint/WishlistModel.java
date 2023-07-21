package co.in.dadspint;

public class WishlistModel {

    String wishlist_id,user_id,product_id,product_name,primary_image,regular_price,sales_price,product_type;

    public WishlistModel(String wishlist_id, String user_id, String product_id, String product_name,
                         String primary_image, String regular_price, String sales_price,String product_type) {
        this.wishlist_id = wishlist_id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.primary_image = primary_image;
        this.regular_price = regular_price;
        this.sales_price = sales_price;
        this.product_type = product_type;
    }

    public String getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(String wishlist_id) {
        this.wishlist_id = wishlist_id;
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

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
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

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
}
