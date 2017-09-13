package com.poipoint.eatfit.models;

/**
 * Created by Tanmay on 2/20/2016.
 */
public class SearchObject implements Comparable<SearchObject>{

    private String name;
    private int type;
    private int tableId;

    public SearchObject( String name, int type, int tableId) {
        this.name = name;
        this.type = type;
        this.tableId = tableId;
    }



    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getTableId() {
        return tableId;
    }

    @Override
    public int compareTo(SearchObject rhs) {
        int lhstype=this.getType();
        int rhstype=rhs.getType();
        if(lhstype<rhstype)
            return rhstype-lhstype;
        if(lhstype>rhstype)
            return rhstype-lhstype;
        return 0;
    }
}
