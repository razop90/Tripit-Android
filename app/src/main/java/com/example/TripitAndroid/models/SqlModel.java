package com.example.TripitAndroid.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.TripitApplication;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.Consts;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

        try {
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

                    Post post = new Post(curUserId, id, location, description, creationDate, imageUrl, lastUpdate, isDeleted);
                    //curUserId, location, description, imageUrl

                    data.add(post);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
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

        try {
            int rows = db.update(Consts.SQL.PostsTableName, values, "ID=?", new String[]{post.id});
            if (rows == 0) {
                values.put("ID", post.id);
                db.insert(Consts.SQL.PostsTableName, "ID", values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }
    }

    public boolean deletePost(String postId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            return db.delete(Consts.SQL.PostsTableName,"ID=?", new String[]{postId}) > 0;
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }

        return false;
    }
    //endregion

    //region Last Update
    public long getLastUpdate(String table) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long date = 0;

        try {
            Cursor cursor = db.query(Consts.SQL.LastUpdateTableName, null, "TABLE_NAME=?", new String[] { table }, null, null, null);

            if (cursor.moveToFirst()) {
                int dateIndex = cursor.getColumnIndex("DATE");

                do {
                    long currDate = cursor.getLong(dateIndex);

                    date = currDate;
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }

        return date;
    }

    public void updateLastUpdate(String table, long date) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        values.put("DATE", date);
        try {
            int rows = db.update(Consts.SQL.LastUpdateTableName, values, "TABLE_NAME=?", new String[]{table});
            if (rows == 0) {
                values.put("TABLE_NAME", table);
                db.insert(Consts.SQL.LastUpdateTableName, "TABLE_NAME", values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }
    }
    //endregion

    //region User Info
    public UserInfo getUserInfo(String userId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.query(Consts.SQL.UserInfoTableName, null, "UID=?", new String[] { userId }, null, null, null);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("UID");
                int nameIndex = cursor.getColumnIndex("DISPLAY_NAME");
                int emailIndex = cursor.getColumnIndex("EMAIL");
                int imageUrlIndex = cursor.getColumnIndex("IMAGEURL");
                int timestampIndex = cursor.getColumnIndex("TIMESTAMP");

                do {
                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String imageUrl = cursor.getString(imageUrlIndex);
                    long timestamp = cursor.getLong(timestampIndex);

                    return new UserInfo(id, name, email, imageUrl, timestamp);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }

        return null;
    }

    public void addUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put("DISPLAY_NAME", user.displayName);
        values.put("EMAIL", user.email);
        values.put("IMAGEURL", user.profileImageUrl);
        values.put("TIMESTAMP", user.getLastUpdate());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            int rows = db.update(Consts.SQL.UserInfoTableName, values, "UID=?", new String[]{ user.uid });
            if (rows == 0) {
                values.put("UID", user.uid);
                db.insert(Consts.SQL.UserInfoTableName, "UID", values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }
    }
    //endregion

    //region Likes
    //endregion

    class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context) {
            super(context, "database.db", null, 13);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+ Consts.SQL.PostsTableName +" (ID TEXT PRIMARY KEY, USER_ID TEXT, " +
                    "LOCATION TEXT, DESCRIPTION TEXT, IMAGE_URL TEXT, CREATION_DATE DOUBLE, " +
                    "LAST_UPDATE DOUBLE, IS_DELETED INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + Consts.SQL.LastUpdateTableName + " (TABLE_NAME TEXT PRIMARY KEY, DATE DOUBLE)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + Consts.SQL.UserInfoTableName + " (UID TEXT PRIMARY KEY, DISPLAY_NAME TEXT, EMAIL TEXT, IMAGEURL TEXT, TIMESTAMP DOUBLE)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + Consts.SQL.LikesTableName + " (UID TEXT, POST_ID TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Consts.SQL.PostsTableName);
            db.execSQL("DROP TABLE IF EXISTS " + Consts.SQL.UserInfoTableName);
            db.execSQL("DROP TABLE IF EXISTS " + Consts.SQL.LikesTableName);
            db.execSQL("DROP TABLE IF EXISTS " + Consts.SQL.LastUpdateTableName);

            onCreate(db);
        }
    }
}
