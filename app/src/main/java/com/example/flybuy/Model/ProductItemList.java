package com.example.flybuy.Model;

public class ProductItemList {
//    private String productImage,productName,productPrice,productId,productOverview;
    private String image,name,price,productId;

    public ProductItemList() {
    }

    public ProductItemList(String productId,String image, String name, String price) {
        this.productId = productId;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return image;
    }

    public void setProductImage(String productImage) {
        this.image = productImage;
    }

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public String getProductPrice() {
        return price;
    }

    public void setProductPrice(String productPrice) {
        this.price = productPrice;
    }

}
