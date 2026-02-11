package com.example.noteakbar

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAll(): List<Note>
}
