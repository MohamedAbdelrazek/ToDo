package com.tripleservice.mvvm_architecture;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {


    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("Delete From notes_table")
    void delete();

    @Query("Select * From notes_table order by priority_column")
    List<Note> getAllNotes();
}

