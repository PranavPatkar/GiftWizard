package com.example;

public class Gift {
    private String id;
    private final String name;
    private final String price;
    private final String reviews;
    private final String imageUrl;

    private boolean trending;
    private boolean popular;

    public Gift(String id, String name, String price, String reviews, String imageUrl, boolean trending, boolean popular) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.reviews = reviews;
        this.imageUrl = imageUrl;
        this.trending = trending;
        this.popular = popular;
    }

    public Gift(String name, String price, String reviews, String imageUrl) {
        this(null, name, price, reviews, imageUrl, false, false);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getReviews() { return reviews; }
    public String getImageUrl() { return imageUrl; }
    public boolean isTrending() { return trending; }
    public void setTrending(boolean trending) { this.trending = trending; }
    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
}
