package com.example;

public class GiftModel {
    private String id;
    private String name;
    private String price;
    private String reviews;
    private String imageUrl;

    private boolean isTrending;
    private boolean isPopular;

    public GiftModel(String id, String name, String price, String reviews, String imageUrl, boolean isTrending, boolean isPopular) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.reviews = reviews;
        this.imageUrl = imageUrl;
        this.isTrending = isTrending;
        this.isPopular = isPopular;
    }

    public GiftModel(String name, String price, String reviews, String imageUrl) {
        this(null, name, price, reviews, imageUrl, false, false);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getReviews() { return reviews; }
    public void setReviews(String reviews) { this.reviews = reviews; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isTrending() { return isTrending; }
    public void setTrending(boolean trending) { this.isTrending = trending; }
    public boolean isPopular() { return isPopular; }
    public void setPopular(boolean popular) { this.isPopular = popular; }
}
