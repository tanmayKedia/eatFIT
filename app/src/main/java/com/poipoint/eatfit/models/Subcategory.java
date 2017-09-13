package com.poipoint.eatfit.models;

import com.poipoint.eatfit.database.EatfitDBSchema;

/**
 * Created by Tanmay on 2/11/2016.
 */
public class Subcategory implements Comparable<Subcategory>{

    private int id;
    private int categoryId;
    private String name;
    private int orderId;

    public Subcategory(int id,int categoryId,String name,int orderId)
    {
        this.id=id;
        this.categoryId=categoryId;
        this.name=name;
        this.orderId=orderId;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public int compareTo(Subcategory rhs) {
        int lhsOrderId=this.getOrderId();
        int rhsOrderId=rhs.getOrderId();
        if(lhsOrderId<rhsOrderId)
            return lhsOrderId-rhsOrderId;
        if(lhsOrderId>rhsOrderId)
            return lhsOrderId-rhsOrderId;
        return 0;
    }
}
