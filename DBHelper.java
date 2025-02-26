package com.example.sahayakapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SahayakDB";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_POSTS = "posts";

    // User Table Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "fullName";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_USER_TYPE = "userType";
    private static final String COLUMN_SKILLS = "skills";

    // Post Table Columns
    private static final String COLUMN_POST_ID = "postId";
    private static final String COLUMN_POST_TEXT = "postText";
    private static final String COLUMN_POST_IMAGE = "postImage";
    private static final String COLUMN_USER_ID = "userId";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_CONTACT + " TEXT, "
                + COLUMN_LOCATION + " TEXT, "
                + COLUMN_USER_TYPE + " TEXT, "
                + COLUMN_SKILLS + " TEXT)";

        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
                + COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_POST_TEXT + " TEXT, "
                + COLUMN_POST_IMAGE + " TEXT, "
                + COLUMN_USER_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    public boolean registerUser(String fullName, String email, String password, String contact, String location, String userType, String skills) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_USER_TYPE, userType);
        values.put(COLUMN_SKILLS, skills);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean checkUserExists(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        String name = cursor.moveToFirst() ? cursor.getString(0) : "N/A";
        cursor.close();
        return name;
    }

    public String getUserContact(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CONTACT + " FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        String contact = cursor.moveToFirst() ? cursor.getString(0) : "N/A";
        cursor.close();
        return contact;
    }

    public String getUserLocation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_LOCATION + " FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        String location = cursor.moveToFirst() ? cursor.getString(0) : "N/A";
        cursor.close();
        return location;
    }

    public boolean updateUserProfile(String email, String fullName, String contact, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, fullName);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_LOCATION, location);

        int rows = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();

        return rows > 0;
    }

    public boolean updateUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PASSWORD, newPassword);

        int rows = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();

        return rows > 0;
    }

    public boolean addPost(String text, String imageUrl, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_POST_TEXT, text);
        values.put(COLUMN_POST_IMAGE, imageUrl);
        values.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_POSTS, null, values);
        db.close();

        return result != -1;
    }

    public Cursor getAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT p.*, u." + COLUMN_NAME + " FROM " + TABLE_POSTS + " p JOIN " + TABLE_USERS + " u ON p." + COLUMN_USER_ID + "=u." + COLUMN_ID, null);
    }
}
