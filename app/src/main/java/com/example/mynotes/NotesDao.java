package com.example.mynotes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("select * from Note")
    List<Note> getNotes();

    @Query("SELECT * FROM note WHERE noteId IN (:userIds)")
    List<Note> loadAllByIds(int[] userIds);

    @Insert
    void insertNote(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note")
    void deleteAll();
}
