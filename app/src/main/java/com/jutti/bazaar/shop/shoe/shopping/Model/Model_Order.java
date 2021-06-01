package com.jutti.bazaar.shop.shoe.shopping.Model;

public class Model_Order {

    String ORDER_ID;
    String Display_id;
    String ORDER_DATE;
    String STATUS;
    String ORDER_AMOUNT;
    String USER_ID;
    String SELLER_NAME;

    public String getSELLER_NAME() {
        return SELLER_NAME;
    }

    public void setSELLER_NAME(String SELLER_NAME) {
        this.SELLER_NAME = SELLER_NAME;
    }



    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }
  public String getDisplay_id() {
        return Display_id;
    }

    public void setDisplay_id(String Display_id) {
        this.Display_id = Display_id;
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

    public void setORDER_AMOUNT(String ORDER_AMOUNT) {
        this.ORDER_AMOUNT = ORDER_AMOUNT;
    }  public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
}
