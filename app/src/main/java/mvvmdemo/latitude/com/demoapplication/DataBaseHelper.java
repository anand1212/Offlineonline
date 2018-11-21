package mvvmdemo.latitude.com.demoapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "storeContacts";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PH_NO = "phone_number";
    String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FNAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PH_NO + " TEXT" + ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addContacts(String name, String email, String phone_no) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PH_NO, phone_no);
        db.insert(TABLE_CONTACTS, null, values);
        db.close();

    }

    public ArrayList<Employee> getAllStudentsList() {
        ArrayList<Employee> studentsArrayList = new ArrayList<Employee>();
        String name = "";
        String email = "";
        String phone = "";
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Employee employee = new Employee();
                name = c.getString(c.getColumnIndex(KEY_FNAME));
                email = c.getString(c.getColumnIndex(KEY_EMAIL));
                phone = c.getString(c.getColumnIndex(KEY_PH_NO));
                employee.setEmail(email);
                employee.setName(name);
                employee.setPhone(phone);


                // adding to Students list
                studentsArrayList.add(employee);
            } while (c.moveToNext());
            Log.d("array", studentsArrayList.toString());
        }
        return studentsArrayList;
    }
}
