package com.example.phone_shop;

public class DataModal {
    private String manufacturer;
    private String model;
    private String colour;
    private float price;
    private String image;

    public DataModal(String manufacturer, String model, String colour, Float price, String image) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.colour = colour;
        this.price = price;
        this.image = image;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getColour() {
        return colour;
    }

    public Float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
