package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import Databases.NoteListDB;

public class NoteDetails_Activity extends AppCompatActivity {
    NoteListDB noteListDB;
    EditText TitleET,NoteET;
    TextView DateTv;
    Button saveBTN;
    ImageButton DeleteBTN;
    AlertDialog.Builder delete_alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details_);
        TitleET = (EditText) findViewById(R.id.Title_ETID);
        NoteET = (EditText) findViewById(R.id.Note_ETID);
        saveBTN = (Button) findViewById(R.id.save_btnID);
        DeleteBTN = (ImageButton) findViewById(R.id.delete_btnID);
        DateTv = (TextView) findViewById(R.id.DateTVID);

        noteListDB = new NoteListDB(this);
        SQLiteDatabase sqLiteDatabase = noteListDB.getWritableDatabase();


        final String get_id = getIntent().getStringExtra("Id");
        getDetails(get_id);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Titles = TitleET.getText().toString();
                String Notes = NoteET.getText().toString();

                Boolean result = noteListDB.updateMethod(get_id,Titles,Notes);
                if (result==true){
                    Toast.makeText(NoteDetails_Activity.this, "Note updated", Toast.LENGTH_SHORT).show();
                    MainActivity.mainActivity.finish();
                    Intent intent = new Intent(NoteDetails_Activity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(NoteDetails_Activity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_alertdialog = new AlertDialog.Builder(NoteDetails_Activity.this);
                delete_alertdialog.setTitle("Delete");
                delete_alertdialog.setMessage("Delete this note?");
                delete_alertdialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int del_item = noteListDB.deleteMethod(get_id);
                        if (del_item>0){
                            Toast.makeText(NoteDetails_Activity.this,"deleted", Toast.LENGTH_SHORT).show();
                            MainActivity.mainActivity.finish();
                            Intent intent = new Intent(NoteDetails_Activity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(NoteDetails_Activity.this, "delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                delete_alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = delete_alertdialog.create();
                alertDialog.show();
            }
        });
    }

    private void getDetails(String get_id) {
        int id = Integer.parseInt(get_id);
        Cursor cursor = noteListDB.fetchDetails(id);
        cursor.moveToNext();
        TitleET.setText(cursor.getString(1));
        NoteET.setText(cursor.getString(2));
        DateTv.setText(cursor.getString(3));
    }

}