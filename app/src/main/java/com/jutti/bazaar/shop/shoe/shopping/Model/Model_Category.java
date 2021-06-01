package com.jutti.bazaar.shop.shoe.shopping.Model;

public class Model_Category {

    String category_id, category_name, category_image,  parent_name;


    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getcategory_id() {
        return category_id;
    }

    public void setcategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getcategory_name() {
        return category_name;
    }

    public void setcategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getcategory_image() {
        return category_image;
    }

    public void setcategory_image(String category_image) {
        this.category_image = category_image;
    }

}
