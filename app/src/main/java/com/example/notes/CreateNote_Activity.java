package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Databases.NoteListDB;

public class CreateNote_Activity extends AppCompatActivity {
    EditText Title_ET,Note_ET;
    Button saveBTN;
    NoteListDB noteListDB;
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note_);
        Title_ET = (EditText) findViewById(R.id.TitleETID);
        Note_ET = (EditText) findViewById(R.id.NoteETID);
        saveBTN = (Button) findViewById(R.id.savebtnID);
        textInputLayout = (TextInputLayout) findViewById(R.id.TitleErrorTIID);

        noteListDB = new NoteListDB(this);
        SQLiteDatabase sqLiteDatabase = noteListDB.getWritableDatabase();

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = Title_ET.getText().toString();
                String Note = Note_ET.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-YYYY\nhh:mm:ss");
                String Date = simpleDateFormat.format(calendar.getTime());

                Boolean get_title = noteListDB.CheckTitleMethod(Title);
                if (get_title == true || Title.isEmpty()){
                    textInputLayout.setError(getResources().getString(R.string.title_error));
                }else {
                    long rowID = noteListDB.InsertNote(Title,Note,Date);
                    if (rowID == -1){
                        Toast.makeText(CreateNote_Activity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CreateNote_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                        MainActivity.mainActivity.finish();
                        Intent intent = new Intent(CreateNote_Activity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });


    }
}