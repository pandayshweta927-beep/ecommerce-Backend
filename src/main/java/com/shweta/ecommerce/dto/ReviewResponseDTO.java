package com.shweta.ecommerce.dto;

public class ReviewResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String userName;
    private Integer rating;
    private String comment;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(
            Long id,
            Long productId,
            String productName,
            String userName,
            Integer rating,
            String comment) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}