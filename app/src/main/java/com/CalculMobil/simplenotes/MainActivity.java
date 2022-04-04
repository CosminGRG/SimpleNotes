package com.CalculMobil.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.CalculMobil.simplenotes.model.Adapter;
import com.CalculMobil.simplenotes.R;
import com.CalculMobil.simplenotes.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;
    RecyclerView noteLists;
    Adapter adapter;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Hello World");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        Query query = fStore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("title", Query.Direction.DESCENDING);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("notif", "notif", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, @SuppressLint("RecyclerView") int position, @NonNull Note note) {
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContent.setText(note.getContent());

                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    //passed the data to note details
                    public void onClick(View view) {
                        Intent i  = new Intent(view.getContext(), NoteDetails.class);
                        i.putExtra("title", note.getTitle());
                        i.putExtra("content", note.getContent());
                        i.putExtra("noteId", docId);
                        view.getContext().startActivity(i);
                    }
                });

                //find ImageView for menu
                ImageView menu_icon = noteViewHolder.view.findViewById(R.id.menuIcon);

                menu_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        //get note id
                        final String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                        PopupMenu menu = new PopupMenu(view.getContext(),view);
                        menu.setGravity(Gravity.END);

                        //elements off popup menu
                        menu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent editNoteIntent = new Intent(view.getContext(), EditNote.class);
                                editNoteIntent.putExtra("title", note.getTitle());
                                editNoteIntent.putExtra("content", note.getContent());
                                editNoteIntent.putExtra("noteId",docId);
                                startActivity(editNoteIntent);
                                return false;
                            }
                        });
                        menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference docRef = fStore.collection("notes").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(MainActivity.this,"Error in deleting note",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return false;
                            }
                        });
                        menu.getMenu().add("Notify").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                NotificationTask runner = new NotificationTask();
                                String sleepingTime = "5";
                                runner.execute(sleepingTime);
                                return false;
                            }
                        });
                        menu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        noteLists = findViewById(R.id.notelist);

        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);

        //open add note button
        FloatingActionButton fab = findViewById(R.id.addNoteFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddNote.class);
                startActivity(intent);
            }
        });
    }

    private class NotificationTask extends AsyncTask<String, String, String> {

        private String resp;
        private int NOTIFICATION_ID = 1;

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        private void createNotification(String contentTitle, String contentText) {
            Log.d("createNotification", "title is [" + contentTitle +"]");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "notif");
            builder.setSmallIcon(R.drawable.ic_baseline_add_box_24)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

            notificationManager.notify(NOTIFICATION_ID, builder.build());

        }

        @Override
        protected void onPostExecute(String result) {
            createNotification("New notification", "Hello World!");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notes:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                break;
            case R.id.addNote:
                Intent addNoteIntent = new Intent(this,AddNote.class);
                startActivity(addNoteIntent);
                break;
            case R.id.sync:
                if(user.isAnonymous())
                {
                    Intent loginIntent = new Intent(this,Login.class);
                    startActivity(loginIntent);
                }
                else    
                {
                    Toast.makeText(this, "You Are connected.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.logout:
                checkUser();
                break;

            default:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void checkUser() {

        //if user is real or not
        if(user.isAnonymous())
        {
            displayAlert();
        }
        else
        {
            FirebaseAuth.getInstance().signOut();
            Intent splashIntent = new Intent(getApplicationContext(),Splash.class);
            splashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(splashIntent);
            finish();
        }

    }

    private void displayAlert() {
            //alert dialog with 2 buttons
        AlertDialog.Builder warning = new AlertDialog.Builder(this).setTitle("Are you sure?")
                .setMessage("You are logged in with TemporaryAccount.Logging out will delete all notes. ")
                .setPositiveButton("Sync Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent registerIntent = new Intent(getApplicationContext(),Register.class);
                        registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(registerIntent);
                        finish();
                    }
                }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete all the notes created by the Anon user


                        //delete the anon user

                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent splashIntent = new Intent(getApplicationContext(),Splash.class);
                                splashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(splashIntent);
                                finish();

                            }
                        });


                    }
                });

        warning.show();


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
            Intent settingsIntent = new Intent(this,Settings.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAvatarImageSelected(View view)
    {
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            view = itemView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }*/
}
