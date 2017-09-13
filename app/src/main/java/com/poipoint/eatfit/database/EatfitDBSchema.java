package com.poipoint.eatfit.database;

/**
 * Created by Tanmay on 2/9/2016.
 */
public class EatfitDBSchema {
    public static final class HealthCategory
    {
        public static final String NAME="health_category";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String ID_ORDER="id_order";
            public static final String NAME="nazwa";
            public static final String ICON="icon";
        }
    }

    public static final class HealthSubcategory
    {
        public static final String NAME="health_cat2";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String CATEGORY_ID="cat1";
            public static final String NAME="name";
            public static final String ORDER_ID="id_order";
        }
    }

    public static final class HealthProduct
    {
        public static final String NAME="health_produkt";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String CATEGORY_ID="main_cat";
            public static final String SUBCATEGORY_ID="second_cat";
            public static final String NAME="nazwa";
            public static final String COMPANY="firma";
            public static final String INGREDIENTS="sklad";
            public static final String BARCODE="stan";

        }
    }

    public static final class HealthFirmy
    {
        public static final String NAME="health_firmy";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String COMPANY="firma";
        }
    }

    public static final class HealthIngredients
    {
        public static final String NAME="health_sklad";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String NAME1="nazwa";
            public static final String NAME2="nazwa2";
            public static final String NAME3="nazwa3";
            public static final String SYMBOL="symbol";
            public static final String DESCRIPTION="opis";
            public static final String DANGER_LEVEL="danger";
        }
    }

    public static final class HealthSearch
    {
        public static final String NAME="health_search";
        public static final class Cols
        {
        public static final String NAME="name";
        public static final String TYPE="type";
        public static final String TABLEID="table_id";
        }
    }
}
