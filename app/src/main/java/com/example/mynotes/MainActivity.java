package com.example.mynotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mynotes.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NoteDatabase noteDatabase;
    NotesAdapter notesAdapter;
    ArrayList<Note> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteDatabase = NoteDatabase.getInstance(this);
        notes = new ArrayList<>();
        notesAdapter = new NotesAdapter(this,notes);
        binding.notesRV.setAdapter(notesAdapter);
        new RetrieveTask(this).execute();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreateNoteActivity.class);
                startActivityForResult(intent,456);
            }
        });

        binding.searchBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.deleteAll){
                    noteDatabase.getNotesDao().deleteAll();
                    notes.clear();
                    notesAdapter.notifyItemRangeRemoved(0,notes.size());
                }
                return false;
            }
        });


    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>> {

        private final WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().noteDatabase.getNotesDao().getNotes();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes!=null && notes.size()>0 ){
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                activityReference.get().notesAdapter.notifyDataSetChanged();
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            String extra = data.getStringExtra("note");
            Gson gson = new Gson();
            Note note = gson.fromJson(extra, Note.class);
            notes.add(note);
            notesAdapter.notifyItemInserted(notes.size());
        }
    }
}