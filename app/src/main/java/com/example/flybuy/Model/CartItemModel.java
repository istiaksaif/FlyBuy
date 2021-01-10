package com.example.flybuy.Model;

public class CartItemModel {
    private String id,productId,name,price,itemtotalprice,quantity,image;

    public CartItemModel() {
    }

    public CartItemModel(String id, String productId, String name, String price, String itemtotalprice, String quantity,String image) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.itemtotalprice = itemtotalprice;
        this.quantity = quantity;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemtotalprice() {
        return itemtotalprice;
    }

    public void setItemtotalprice(String itemtotalprice) {
        this.itemtotalprice = itemtotalprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
