package com.data.sqlexample2023

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context) :SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "notes.db"
        val DATABASE_VERSION = 1

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE note(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                content TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }
    //CRUD
    //CREATE
    fun insertNote(note:Note){
        val db = writableDatabase

        val sql = "INSERT INTO note (title, content) VALUES (?,?)"
        val args = arrayOf(note.title, note.content)
        db.execSQL(sql,args)
    }
    //READ
    fun getAllNotes():MutableList<Note>{
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM note", null)
        val noteList = mutableListOf<Note>()

        while(cursor.moveToNext()){
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)

            var newNote = Note(id, title, content)
            noteList.add(newNote)
        }
        cursor.close()
        return noteList
    }
    //UPDATE
    fun updateData(note: Note){
        val db = writableDatabase
        val updateQuery = "UPDATE note SET title ='${note.title}', content ='${note.content}' WHERE id = ${note.id};"
        db.execSQL(updateQuery)
    }

    //DELETE
    fun deleteData(id:Int){
        val db = writableDatabase
        val deleteQuery = "DELETE FROM note WHERE ID = $id"
        db.execSQL(deleteQuery)
    }
}