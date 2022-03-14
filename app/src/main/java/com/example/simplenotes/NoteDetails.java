package com.example.simplenotes;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

<<<<<<< HEAD
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.simplenotes.databinding.ActivityNoteDetailsBinding;
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a

public class NoteDetails extends AppCompatActivity {
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

<<<<<<< HEAD
        //receive data from adapter
=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
        data = getIntent();


        TextView content = findViewById(R.id.noteDetailsContent);
<<<<<<< HEAD
        TextView title = findViewById(R.id.noteDetailsTitle);
=======
        TextView title = findViewById(R.id.titles);
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
        content.setMovementMethod(new ScrollingMovementMethod());

        content.setText(data.getStringExtra("content"));
        title.setText(data.getStringExtra("title"));
<<<<<<< HEAD

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->  {
            Snackbar.make(view,"Replace with your action",Snackbar.LENGTH_LONG).setAction("Action",null).show();
=======
        content.setBackgroundColor(getResources().getColor(data.getIntExtra("code",0),null));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(view.getContext(),EditNote.class);
//                i.putExtra("title",data.getStringExtra("title"));
//                i.putExtra("content",data.getStringExtra("content"));
//                i.putExtra("noteId",data.getStringExtra("noteId"));
//                startActivity(i);
            }
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
