package com.example.finalexam.utils;

/**
 * Created by mshehab on 5/6/18.
 */

public class Gift {
    public String name;
    public int price;
    public String id;

    public Gift() {
    }

    public Gift(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Gift(String name, int price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }
}
