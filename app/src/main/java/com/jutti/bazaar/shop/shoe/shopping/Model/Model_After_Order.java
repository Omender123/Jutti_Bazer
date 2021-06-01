package com.jutti.bazaar.shop.shoe.shopping.Model;

public class Model_After_Order {
    String ORDER_ID;
    String ORDER_DATE;
    String STATUS;
    String ORDER_AMOUNT;
    String USER_ID;
    String title;
    String qty;
    String priceqty;
    String subtotal;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPriceqty() {
        return priceqty;
    }

    public void setPriceqty(String priceqty) {
        this.priceqty = priceqty;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    String productimage;

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getORDER_DATE() {
        return ORDER_DATE;
    }

    public void setORDER_DATE(String ORDER_DATE) {
        this.ORDER_DATE = ORDER_DATE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getORDER_AMOUNT() {
        return ORDER_AMOUNT;
    }

    public void setORDER_AMOUNT(String ORDER_AMOUNT) {this.ORDER_AMOUNT = ORDER_AMOUNT;
    }  public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
}
