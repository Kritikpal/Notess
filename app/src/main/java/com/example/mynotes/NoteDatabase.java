package com.example.mynotes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {


    private static NoteDatabase noteDatabase;

    public static NoteDatabase getInstance(Context context) {
        if (noteDatabase==null){
            noteDatabase = Room.databaseBuilder(context,NoteDatabase.class,"Notes.db").allowMainThreadQueries()
                    .build();
        }
        return noteDatabase;
    }

    public abstract NotesDao getNotesDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
