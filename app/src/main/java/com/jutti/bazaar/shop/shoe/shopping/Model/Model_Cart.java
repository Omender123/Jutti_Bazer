package com.jutti.bazaar.shop.shoe.shopping.Model;

public class Model_Cart {
    String cart_id;
    String productid;
    String TITLE;
    String THUMBNAIL;
    String type;
    String PRICE;
    String qty;
    String TOTAL_PRICE;
    String variant_size;
    String variant_id;
    String cart_quantity;
    String product_total_stock;
    String product_min_order_qty;


    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getVariant_id() {
        return variant_id;
    }
    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }
    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getvariant_size() {
        return variant_size;
    }

    public void setvariant_size(String variant_size) {
        this.variant_size = variant_size;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }


    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getTHUMBNAIL() {
        return THUMBNAIL;
    }

    public void setTHUMBNAIL(String THUMBNAIL) {
        this.THUMBNAIL = THUMBNAIL;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getqty() {
        return qty;
    }

    public void setqty(String qty) {
        this.qty = qty;
    }

    public String getTOTAL_PRICE() {
        return TOTAL_PRICE;
    }

    public void setTOTAL_PRICE(String TOTAL_PRICE) {
        this.TOTAL_PRICE = TOTAL_PRICE;
    }


    public String getProduct_total_stock() {
        return product_total_stock;
    }

    public void setProduct_total_stock(String product_total_stock) {
        this.product_total_stock = product_total_stock;
    }

    public String getProduct_min_order_qty() {
        return product_min_order_qty;
    }

    public void setProduct_min_order_qty(String product_min_order_qty) {
        this.product_min_order_qty = product_min_order_qty;
    }
}
