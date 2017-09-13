package com.poipoint.eatfit.models;

/**
 * Created by Tanmay on 2/12/2016.
 */
public class Company {
    private int id;
    private String name;

    public Company(int id,String name)
    {
        this.id=id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
