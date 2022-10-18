package com.example.phone_shop;

import android.os.Parcel;
import android.os.Parcelable;

public class Mask implements Parcelable{

    private int id_phone;
    private String manufacturer;
    private String model;
    private String colour;
    private float price;
    private String image;

    public Mask(int id_phone, String manufacturer, String model, String colour, Float price, String image) {
        this.id_phone = id_phone;
        this.manufacturer = manufacturer;
        this.model = model;
        this.colour = colour;
        this.price = price;
        this.image = image;
    }

    protected Mask(Parcel in) {
        id_phone = in.readInt();
        manufacturer = in.readString();
        model = in.readString();
        colour = in.readString();
        price = in.readFloat();
        image = in.readString();
    }

    public static final Creator<Mask> CREATOR = new Creator<Mask>() {
        @Override
        public Mask createFromParcel(Parcel in) {
            return new Mask(in);
        }

        @Override
        public Mask[] newArray(int size) {
            return new Mask[size];
        }
    };

    public void setId_phone(int id_phone) {
        this.id_phone = id_phone;
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

    public int getId_phone() {
        return id_phone;
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_phone);
        dest.writeString(manufacturer);
        dest.writeString(model);
        dest.writeString(colour);
        dest.writeFloat(price);
        dest.writeString(image);
    }
}
