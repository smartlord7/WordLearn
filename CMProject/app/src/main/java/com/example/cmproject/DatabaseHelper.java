package com.example.cmproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "language_game.db";
    private static final int DATABASE_VERSION = 1;

    // Define your user table schema
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";

    // Define words tables for each tier
    private static final String TABLE_BRONZE = "bronze_words";
    private static final String TABLE_SILVER = "silver_words";
    private static final String TABLE_GOLD = "gold_words";
    private static final String TABLE_MASTER = "master_words";
    private static final String COLUMN_PORTUGUESE = "portuguese";
    private static final String COLUMN_ENGLISH = "english";

    // SQL statement to create the users table
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL);";

    // SQL statements to create the words tables
    private static final String CREATE_BRONZE_TABLE =
            "CREATE TABLE " + TABLE_BRONZE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PORTUGUESE + " TEXT NOT NULL, " +
                    COLUMN_ENGLISH + " TEXT NOT NULL);";

    private static final String CREATE_SILVER_TABLE =
            "CREATE TABLE " + TABLE_SILVER + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PORTUGUESE + " TEXT NOT NULL, " +
                    COLUMN_ENGLISH + " TEXT NOT NULL);";

    private static final String CREATE_GOLD_TABLE =
            "CREATE TABLE " + TABLE_GOLD + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PORTUGUESE + " TEXT NOT NULL, " +
                    COLUMN_ENGLISH + " TEXT NOT NULL);";

    private static final String CREATE_MASTER_TABLE =
            "CREATE TABLE " + TABLE_MASTER + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PORTUGUESE + " TEXT NOT NULL, " +
                    COLUMN_ENGLISH + " TEXT NOT NULL);";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase(); // Open the database when the helper is created
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table when the database is first created
        db.execSQL(CREATE_USERS_TABLE);

        // Create the words tables
        db.execSQL(CREATE_BRONZE_TABLE);
        db.execSQL(CREATE_SILVER_TABLE);
        db.execSQL(CREATE_GOLD_TABLE);
        db.execSQL(CREATE_MASTER_TABLE);

        Log.d("DatabaseHelper", "Tier tables created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
        // This method is called when the database version is increased
        // You can modify the database schema here
    }

    // Additional methods for common database operations

    // Example: Insert a user into the database
    public void insertUser(String username, String password, String email) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        db.insert(TABLE_USERS, null, values);
    }

    // Example: Check if a user with the given username exists in the database
    public boolean isUsernameExists(String username) {
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean isEmailExists(String email) {
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Add more methods as needed for your specific use case

    // ... (e.g., query, update, delete operations)

    // Method to insert words into a specific tier table
    public void insertWord(String tier, String portuguese, String english) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PORTUGUESE, portuguese);
        values.put(COLUMN_ENGLISH, english);

        long rowId;

        switch (tier.toLowerCase()) {
            case "bronze":
                rowId = db.insert(TABLE_BRONZE, null, values);
                break;
            case "silver":
                rowId = db.insert(TABLE_SILVER, null, values);
                break;
            case "gold":
                rowId = db.insert(TABLE_GOLD, null, values);
                break;
            case "master":
                rowId = db.insert(TABLE_MASTER, null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid tier: " + tier);
        }

        if (rowId == -1) {
            // Handle insertion failure
        }
    }

    public boolean checkUserCredentials(String username, String password) {
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        boolean isValid = cursor.getCount() > 0;

        // Close the cursor
        cursor.close();

        return isValid;
    }

    public void uploadWordsFromFile(String tier, InputStream fileInputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into Portuguese and English words
                String[] words = line.split(";");
                if (words.length == 2) {
                    String portugueseWord = words[0].trim();
                    String englishWord = words[1].trim();

                    // Insert the words into the corresponding table
                    insertWord(tier, portugueseWord, englishWord);
                }
            }

            // Close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }


    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
