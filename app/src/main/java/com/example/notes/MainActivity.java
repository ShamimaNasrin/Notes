package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;

import Databases.NoteListDB;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    NoteListDB noteListDB;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> Titles;
    SearchView searchView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    GridView gridView;
    boolean time=true;
    FloatingActionButton floatBTN;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        listView =(ListView) findViewById(R.id.ListviewID);
        gridView =(GridView) findViewById(R.id.GridviewID);
        searchView =(SearchView) findViewById(R.id.searchviewID);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayoutID);
        navigationView = (NavigationView) findViewById(R.id.NavigationviewID);
        toolbar = (Toolbar) findViewById(R.id.ToolbarID);
        setSupportActionBar(toolbar);
        floatBTN = (FloatingActionButton) findViewById(R.id.FloatButton);

        floatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreateNote_Activity.class);
                startActivity(intent);

            }
        });

        noteListDB = new NoteListDB(this);
        SQLiteDatabase sqLiteDatabase = noteListDB.getWritableDatabase();
        LoadNotes();
        SharedPreferences sharedPreferences = getSharedPreferences("viewdetails",MODE_PRIVATE);
        if (sharedPreferences.contains("KEY")){
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            GridviewMethod();
        }

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.createnoteMenuID) {
                    Intent intent = new Intent(MainActivity.this, CreateNote_Activity.class);
                    startActivity(intent);
                    //finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.Alpha_sortID){
                    AscendingMethod(Titles,arrayAdapter);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.LV_viewID){
                    //String lock = "nasrin";
                    SharedPreferences sharedPreferences = getSharedPreferences("viewdetails",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //editor.putString("LOCK",lock);
                    editor.clear();
                    editor.apply();
                    listView.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    LoadNotes();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.GV_viewID){
                    String key = "shamima";
                    SharedPreferences sharedPreferences = getSharedPreferences("viewdetails",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("KEY",key);
                    editor.commit();
                    listView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    GridviewMethod();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.Time_sortID){
                    time = false;
                    LoadNotes();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }

    private void ListviewMethod(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String values = adapterView.getItemAtPosition(i).toString();
                Cursor cursor = noteListDB.fetchData(values);
                cursor.moveToNext();
                String id = cursor.getString(0);
                Intent intent = new Intent(MainActivity.this,NoteDetails_Activity.class);
                intent.putExtra("Id",id);
                startActivity(intent);

            }
        });

    }
    private void GridviewMethod(){
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               String values = adapterView.getItemAtPosition(i).toString();
               Cursor cursor = noteListDB.fetchData(values);
               cursor.moveToNext();
               String id = cursor.getString(0);
               Intent intent = new Intent(MainActivity.this,NoteDetails_Activity.class);
               intent.putExtra("Id",id);
               startActivity(intent);

           }
       });

    }

    private void LoadNotes() {
        Titles = new ArrayList<>();

        final Cursor cursor = noteListDB.showNotes();
        if(cursor.getCount()==0){
            Toast.makeText(this, "null database", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                Titles.add(cursor.getString(1));
            }
        }
        arrayAdapter = new ArrayAdapter<>(this,R.layout.listview_sampledesign,R.id.TitleTVID,Titles);
        listView.setAdapter(arrayAdapter);
        gridView.setAdapter(arrayAdapter);
        if (time == false){
            Collections.reverse(Titles);
        }
        ListviewMethod();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void AscendingMethod(ArrayList<String> Titles,ArrayAdapter<String> arrayAdapter) {
        Collections.sort(Titles);
        arrayAdapter.notifyDataSetChanged();
        //AscendingMethod(Titles,arrayAdapter);
    }

}