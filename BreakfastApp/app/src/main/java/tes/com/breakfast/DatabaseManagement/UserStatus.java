package tes.com.breakfast.DatabaseManagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.Util.MessageDisplay;

/**
 * Created by Aashijit on 17/10/2018.
 */

public class UserStatus extends SQLiteOpenHelper {
    private static final String DB_NAME = "Breakfast";
    private static final int DB_VERSION = 1;
    private  Context mContext;

    public UserStatus(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext=context;
    }

    public UserStatus(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE STATUS(current_status TEXT PRIMARY KEY);";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS STATUS");
        onCreate(sqLiteDatabase);
    }

    /**
     * Check if the User is already logged in.
     *
     * @return
     * @throws BreakfastException
     */
    public boolean hasLogin() throws BreakfastException {
        boolean flag = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from STATUS;", null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    String status = cursor.getString(0);
                    if (status.equalsIgnoreCase("LoggedIn"))
                        flag = true;
                    else if (status.equalsIgnoreCase("LoggedOut"))
                        flag = false;
                    else
                        throw new BreakfastException("Unknown Status Message, FATAL EXCEPTION");
                } while (cursor.moveToNext());
            }
        }
        else
            return false;

        if (cursor != null)
            cursor.close();

        return flag;
    }

    /**
     * Replace the Login Status
     *
     * @param status
     * @throws BreakfastException
     */
    public void setLogin(String status) throws BreakfastException {
        try {
            if (status.equalsIgnoreCase("LoggedIn") || status.equalsIgnoreCase("LoggedOut")) {
                SQLiteDatabase db = this.getWritableDatabase();
                //Delete all records from the Table
                String sqlQuery = "delete from STATUS";
                db.execSQL(sqlQuery);
                ContentValues contentValues = new ContentValues();
                contentValues.put("current_status", status);
                db.insert("STATUS", null, contentValues);
                Log.v("Check Records Status", "Successful");
            } else
                throw new BreakfastException("Unknown Status !!! Fatal Exception !!");
        } catch (BreakfastException ex) {
            MessageDisplay.errorMessage(ex.getMessage(),mContext);
        } catch (SQLException ex) {
            throw new BreakfastException(ex.getMessage());
        }
    }
}
