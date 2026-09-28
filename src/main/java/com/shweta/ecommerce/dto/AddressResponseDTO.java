package com.shweta.ecommerce.dto;

public class AddressResponseDTO {

    private Long id;
    private String name;
    private String phone;
    private String house;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private Boolean defaultAddress;

    public AddressResponseDTO() {
    }

    public AddressResponseDTO(
            Long id,
            String name,
            String phone,
            String house,
            String street,
            String city,
            String state,
            String pincode,
            Boolean defaultAddress) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.house = house;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.defaultAddress = defaultAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
