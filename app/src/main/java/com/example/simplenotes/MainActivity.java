package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.simplenotes.model.Adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
=======
import android.widget.Toast;

import com.example.simplenotes.model.Adapter;
import com.google.android.material.navigation.NavigationView;
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;
    RecyclerView noteLists;
    Adapter adapter;
<<<<<<< HEAD
    FirebaseFirestore fStore;
=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Hello World");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD
        fStore = FirebaseFirestore.getInstance();

        Query query = fStore.collection("notes").orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecycleOptions<>

=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
        noteLists = findViewById(R.id.notelist);

        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        List<String> titles = new ArrayList<>();
        List<String> content = new ArrayList<>();
        titles.add("First note title");
        content.add("First note content");

        titles.add("Second note title");
        content.add("Second note content");

        titles.add("Third note title");
        content.add("Third note content");

        adapter = new Adapter(titles,content);
        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(adapter);
<<<<<<< HEAD

        //open add note button

        FloatingActionButton fab = findViewById(R.id.addNoteFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddNote.class);
                startActivity(intent);
            }
        });
=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
<<<<<<< HEAD
            case R.id.addNote:
                Intent intent = new Intent(this,AddNote.class);
                startActivity(intent);
                break;
            case R.id.sync:
                Intent intent1 = new Intent(this,Login.class);
                startActivity(intent1);
                break;
=======
>>>>>>> b913969d42213a4aada607d53164c0f6addb0f4a
            default:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settings)
        {
            Toast.makeText(this, "Settings menu is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}