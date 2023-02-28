package com.example.assignment5project;

 class DatabaseTables {

        static class Wonders {

            static final String TABLE_NAME = "mountain";
            static final String COLUMN_NAME_ID = "id";
            static final String COLUMN_NAME_NAME = "name";
            static final String COLUMN_NAME_COMPANY = "company";
            static final String COLUMN_NAME_LOCATION = "location";
            static final String COLUMN_NAME_CATEGORY = "category";
            static final String COLUMN_NAME_AUXDATA = "auxdata";
        }

        static final String SQL_CREATE_TABLE_MOUNTAIN =
                // "CREATE TABLE mountain (id INTEGER PRIMARY KEY, name TEXT, height INT)"
                "CREATE TABLE " + Wonders.TABLE_NAME + " (" +
                        Wonders.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        Wonders.COLUMN_NAME_NAME + " TEXT," +
                        Wonders.COLUMN_NAME_COMPANY + " TEXT, " +
                        Wonders.COLUMN_NAME_LOCATION + " TEXT, " +
                        Wonders.COLUMN_NAME_CATEGORY + " TEXT, " +
                        Wonders.COLUMN_NAME_AUXDATA + " TEXT)";

        static final String SQL_DELETE_TABLE_MOUNTAIN =
                // "DROP TABLE IF EXISTS mountain"
                "DROP TABLE IF EXISTS " + Wonders.TABLE_NAME;

    }




