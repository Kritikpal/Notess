package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.mynotes.databinding.ActivityCreateNoteBinding;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class CreateNoteActivity extends AppCompatActivity {

    ActivityCreateNoteBinding binding;
    NoteDatabase noteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteDatabase = NoteDatabase.getInstance(this);
        binding.materialToolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.materialToolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.saveNote){
                    String title = binding.noteTitle.getText().toString().trim();
                    String subTitle = binding.noteSubTitle.getText().toString().trim();
                    String description = binding.notesDesc.getText().toString().trim();
                    saveNote(title,subTitle,description);
                }
                return false;
            }
        });

    }

    private void saveNote(String title, String subTitle, String description) {
        Note note = new Note();
        note.setTitle(title);
        note.setSubTitle(subTitle);
        note.setDescription(description);
        note.setTimeStamp(Calendar.getInstance().getTimeInMillis());
        new InsertTask(this,note).execute();
    }
    private void setResult(Note note){
        Gson gson = new Gson();
        Intent intent = new Intent();
        intent.putExtra("note",gson.toJson(note));
        setResult(RESULT_OK,intent);
        finish();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private final WeakReference<CreateNoteActivity> activityReference;
        private final Note note;


        public InsertTask(CreateNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().noteDatabase.getNotesDao().insertNote(note);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool){
                activityReference.get().setResult(note);
            }
        }

    }

}