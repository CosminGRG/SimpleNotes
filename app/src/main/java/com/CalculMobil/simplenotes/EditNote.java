package com.CalculMobil.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {

    Intent data;
    EditText editNoteTitle, editNoteContent;
    FirebaseFirestore fStore;
    ProgressBar editNoteProgressBar;
    FirebaseUser user;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = fStore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        editNoteProgressBar = findViewById(R.id.editNoteProgressBar);

        data = getIntent();

        editNoteTitle = findViewById(R.id.editNoteTitle);
        editNoteContent = findViewById(R.id.editNoteContent);

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        editNoteTitle.setText(noteTitle);
        editNoteContent.setText(noteContent);

        FloatingActionButton saveEditedNoteFab = findViewById(R.id.saveEditedNote);
        saveEditedNoteFab.setOnClickListener((view) -> {
            String nTitle = editNoteTitle.getText().toString();
            String nContent = editNoteContent.getText().toString();

            if(nTitle.isEmpty() || nContent.isEmpty())
            {
                Toast.makeText(EditNote.this, "Can not save note with empty field", Toast.LENGTH_SHORT).show();
                return;
            }

            editNoteProgressBar.setVisibility(View.VISIBLE);

            /* Update note */
            DocumentReference docref = fStore.collection("notes").document(user.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));

            Map<String,Object> note = new HashMap<>();
            note.put("title",nTitle);
            note.put("content",nContent);

            docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditNote.this, "Note updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditNote.this, "Try again", Toast.LENGTH_SHORT).show();
                    editNoteProgressBar.setVisibility(View.VISIBLE);

                }
            });
        });
    }
}
