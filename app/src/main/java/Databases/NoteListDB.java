package Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.CaseMap;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class NoteListDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Note_details";
    private static final String TABLE_NAME = "Notes";
    private static final String ID = "Id";
    private static final String TITLE = "Title";
    private static final String NOTE = "Note";
    private static final String DATE = "Date";

    private static final int DATABASE_VERSION_NO = 2;
    private Context context;

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            ""+TITLE+" VARCHAR(30)," +
            ""+NOTE+" VARCHAR(1500)," +
            ""+DATE+" VARCHAR(30));";
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public NoteListDB(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION_NO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context, "onCreate successful", Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }catch (Exception e){
            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            Toast.makeText(context, "onUpgrade is called", Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(DELETE_TABLE);
            onCreate(sqLiteDatabase);
        }catch (Exception e){
            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    public Long InsertNote(String title,String note,String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,title);
        contentValues.put(NOTE,note);
        contentValues.put(DATE,date);

        long rowID = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowID;
    }
    public Cursor showNotes(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }
    public Cursor fetchData(String title){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+TITLE+"= ?",new String[]{title});
        return cursor;
    }

    public Boolean updateMethod(String id,String titles,String notes){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(TITLE,titles);
        contentValues.put(NOTE,notes);

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID+"= ?",new String[]{id});
        return true;
    }
    public Cursor fetchDetails(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = "+id,null);
        return cursor;
    }
    public Integer deleteMethod(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,ID+" = ?",new String[]{id});
    }
    public Boolean CheckTitleMethod(String title){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        Boolean cursorresult = false;
        if (cursor.getCount()==0){
            Toast.makeText(context, "Null database", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                String n_title = cursor.getString(1);

                if (n_title.equals(title)){
                    cursorresult = true;
                    break;
                }
            }
        }
        return cursorresult;
    }

}
