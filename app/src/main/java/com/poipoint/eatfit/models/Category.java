package com.poipoint.eatfit.models;

import java.util.Comparator;

/**
 * Created by Tanmay on 2/10/2016.
 */
public class Category implements Comparable<Category> {
   private int id;
    private int order_id;
    private String name;
    private String icon_url;

    public Category(int id,int order_id,String name,String icon_url)
    {
        this.id=id;
        this.order_id=order_id;
        this.name=name;
        this.icon_url=icon_url;
    }
    public Category()
    {}

    public int getId() {
        return id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getName() {
        return name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    @Override
    public int compareTo(Category rhs) {
        int lhsOrderId=this.getOrder_id();
        int rhsOrderId=rhs.getOrder_id();
        if(lhsOrderId<rhsOrderId)
            return lhsOrderId-rhsOrderId;
        if(lhsOrderId>rhsOrderId)
            return lhsOrderId-rhsOrderId;
        return 0;
    }
}
