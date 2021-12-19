package com.example.landmark_remark

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "landmark_remark_database.dp"
        const val DATABASE_VERSION = 1
        const val TABLE_NOTES = "notes_table"
        const val KEY_ID = "id"
        const val KEY_NAME = "username"
        const val KEY_MESSAGE = "message"
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createNotesTable = ("CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_MESSAGE + " TEXT, "
                + KEY_LATITUDE + " DOUBLE, "
                + KEY_LONGITUDE + " DOUBLE" + ")")
        db.execSQL(createNotesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun addNote(note: Note): Long {
        // Add note to the database.
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_NAME, note.username.trim())
        contentValues.put(KEY_MESSAGE, note.message)
        contentValues.put(KEY_LATITUDE, note.coordinates.latitude)
        contentValues.put(KEY_LONGITUDE, note.coordinates.longitude)

        val success = db.insert(TABLE_NOTES, null, contentValues)
        db.close()

        return success
    }

    fun getAllNotes(): Cursor {
        // Get all notes.
        val db = this.writableDatabase
        return db.rawQuery("select * from $TABLE_NOTES", null)
    }

    // Get notes filtered by the search query.
    // This search both by username and by note message.
    // For username, the query must match.
    // For note message, the query can be contained in the message.
    fun getFilteredNotes(searchQuery: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("select * from $TABLE_NOTES where username = '$searchQuery' OR message like '%$searchQuery%'", null)
    }
}