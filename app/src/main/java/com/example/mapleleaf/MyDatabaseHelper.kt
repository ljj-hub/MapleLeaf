package com.example.mapleleaf

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int):
    SQLiteOpenHelper(context, name, null, version){

    private val createNote ="create table Note("+
            "id integer primary key autoincrement,"+
            "title text,"+
            "context text,"+
            "time text,"+
            "class text,"+
            "bit_image BLOB)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createNote)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

}