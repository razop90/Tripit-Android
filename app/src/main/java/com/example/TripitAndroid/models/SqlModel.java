package com.example.TripitAndroid.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.TripitApplication;
import com.example.TripitAndroid.Consts;

import java.util.ArrayList;

public class SqlModel {

    MyHelper mDbHelper;

    public SqlModel() {
        mDbHelper = new MyHelper(TripitApplication.getContext());
    }

    //region Post
    public ArrayList<Post> getAllPosts(String userId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<Post> data = new ArrayList<>();

        String query = null;
        String[] args = null;
        if(userId != null) {
            query = "USER_ID=?";
            args = new String[] { userId };
        }

        Cursor cursor = db.query(Consts.SQL.PostsTableName, null, query, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("ID");
            int userIdIndex = cursor.getColumnIndex("USER_ID");
            int locationIndex = cursor.getColumnIndex("LOCATION");
            int descriptionIndex = cursor.getColumnIndex("DESCRIPTION");
            int imageUrlIndex = cursor.getColumnIndex("IMAGE_URL");
            int creationDateIndex = cursor.getColumnIndex("CREATION_DATE");
            int lastUpdateIndex = cursor.getColumnIndex("LAST_UPDATE");
            int isDeletedIndex = cursor.getColumnIndex("IS_DELETED");

            do {
                String id = cursor.getString(idIndex);
                String curUserId = cursor.getString(userIdIndex);
                String location = cursor.getString(locationIndex);
                String description = cursor.getString(descriptionIndex);
                String imageUrl = cursor.getString(imageUrlIndex);
                long creationDate = cursor.getLong(creationDateIndex);
                long lastUpdate = cursor.getLong(lastUpdateIndex);
                int isDeleted = cursor.getInt(isDeletedIndex);

                Post post = new Post(curUserId, id, location, description, creationDate, imageUrl, lastUpdate);
                post.isDeleted = isDeleted;

                data.add(post);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public void addPost(Post post) {
        ContentValues values = new ContentValues();
        values.put("USER_ID", post.userID);
        values.put("LOCATION", post.location);
        values.put("DESCRIPTION", post.description);
        values.put("IMAGE_URL", post.imageUrl);
        values.put("CREATION_DATE", post.creationDateLongFormat);
        values.put("LAST_UPDATE", post.lastUpdateLongFormat);
        values.put("IS_DELETED", post.isDeleted);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rows = db.update(Consts.SQL.PostsTableName, values, "ID=?", new String[]{post.id});
        if (rows == 0) {
            values.put("ID", post.id);
            db.insert(Consts.SQL.PostsTableName, "ID", values);
        }
    }

    public boolean deletePost(String postId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(Consts.SQL.PostsTableName,"ID=?", new String[]{postId}) > 0;
    }
    //endregion

    //region Last Update
    public long getLastUpdate(String table) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long date = 0;

        Cursor cursor = db.query(Consts.SQL.LastUpdateTableName, null, "TABLE_NAME=?", new String[] { table }, null, null, null);

        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex("DATE");

            do {
                long currDate = cursor.getLong(dateIndex);

               date = currDate;
            } while (cursor.moveToNext());
        }

        return date;
    }

    public void updateLastUpdate(String table, long date) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        values.put("DATE", date);

        int rows = db.update(Consts.SQL.LastUpdateTableName, values, "TABLE_NAME=?", new String[]{table});
        if (rows == 0) {
            values.put("TABLE_NAME", table);
            db.insert(Consts.SQL.LastUpdateTableName, "TABLE_NAME", values);
        }
    }
    //endregion

    class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context) {
            super(context, "database.db", null, 5);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+ Consts.SQL.PostsTableName +" (ID TEXT PRIMARY KEY, USER_ID TEXT, " +
                    "LOCATION TEXT, DESCRIPTION TEXT, IMAGE_URL TEXT, CREATION_DATE DOUBLE, " +
                    "LAST_UPDATE DOUBLE, IS_DELETED INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + Consts.SQL.LastUpdateTableName + " (TABLE_NAME TEXT PRIMARY KEY, DATE DOUBLE)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + Consts.SQL.PostsTableName);
            db.execSQL("DROP TABLE " + Consts.SQL.LastUpdateTableName);
            onCreate(db);
        }
    }
}
