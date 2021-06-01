package com.jutti.bazaar.shop.shoe.shopping.Product_list;

public class Model_product_list {


        String products_id;
    String products_name;
    String products_image;
    String popularity;
    String variant_mrp_price;
    String variant_regular_price;
    String product_min_order_qty;
    String category_name;

    String seller_name;
    String variant_selling_price;

    public String getVariant_selling_price() {
        return variant_selling_price;
    }

    public void setVariant_selling_price(String variant_selling_price) {
        this.variant_selling_price = variant_selling_price;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }




    public String getProduct_min_order_qty() {
        return product_min_order_qty;
    }

    public void setProduct_min_order_qty(String product_min_order_qty) {
        this.product_min_order_qty = product_min_order_qty;
    }





    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVariant_mrp_price() {
        return variant_mrp_price;
    }

    public void setVariant_mrp_price(String variant_mrp_price) {
        this.variant_mrp_price = variant_mrp_price;
    }

    public String getVariant_regular_price() {
        return variant_regular_price;
    }

    public void setVariant_regular_price(String variant_regular_price) {
        this.variant_regular_price = variant_regular_price;
    }



        public String getproducts_id() {
            return products_id;
        }

        public void setproducts_id(String products_id) {
            this.products_id = products_id;
        }

        public String getproducts_name() {
            return products_name;
        }

        public void setproducts_name(String products_name) {
            this.products_name = products_name;
        }

        public String getproducts_image() {
            return products_image;
        }

        public void setproducts_image(String products_image) {
            this.products_image = products_image;
        }


}
