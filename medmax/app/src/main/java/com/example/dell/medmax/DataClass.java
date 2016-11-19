package com.example.dell.medmax;

/**
 * Created by Aggarwal on 28-06-2016.
 */
public class DataClass {
    int id;
    String name, number;

    DataClass( String name, String number) {

        this.name = name;
        this.number = number;
    }


    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
