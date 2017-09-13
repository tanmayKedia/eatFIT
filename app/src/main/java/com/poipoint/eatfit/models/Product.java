package com.poipoint.eatfit.models;

/**
 * Created by Tanmay on 2/12/2016.
 */
public class Product implements Comparable<Product> {
    private int id;
    private int category_id;
    private int subcategoryId;
    private String name;
    private String company_id;
    private String ingredients;

    public Product(int id,int category_id,int subcategoryId,String name,String company_name,String ingredients)
    {
        this.id=id;
        this.category_id=category_id;
        this.subcategoryId=subcategoryId;
        this.name=name;
        this.company_id =company_name;
        this.ingredients=ingredients;
    }

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public String getName() {
        return name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getIngredients() {
        return ingredients;
    }

    @Override
    public int compareTo(Product otherObject) {
        return this.name.compareTo(otherObject.name);
    }
}
