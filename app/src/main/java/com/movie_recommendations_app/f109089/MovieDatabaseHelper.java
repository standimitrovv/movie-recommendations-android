package com.movie_recommendations_app.f109089;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "favorite_movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_OVERVIEW = "overview";
    private static final String COLUMN_POSTER_URL = "poster_url";
    private static final String COLUMN_FAVORITE = "favorite";

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_OVERVIEW + " TEXT, " +
                COLUMN_POSTER_URL + " TEXT, " +
                COLUMN_FAVORITE + " INTEGER DEFAULT 0);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addFavoriteMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, movie.getId());
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_OVERVIEW, movie.getDescription());
        values.put(COLUMN_POSTER_URL, movie.getPosterUrl());
        values.put(COLUMN_FAVORITE, movie.getIsFavorite());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public boolean removeFavoriteMovie(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int res = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{movieId});
        db.close();

        return  res > 0;
    }

    public boolean isFavorite(String movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID},
                COLUMN_ID + "=?", new String[]{movieId},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Movie> getAllFavoriteMovies() {
        List<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_OVERVIEW,
                COLUMN_POSTER_URL,
                COLUMN_FAVORITE
        };

        String selection = COLUMN_FAVORITE + " = ?";
        String[] selectionArgs = { "1" };

        Cursor cursor = db.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW));
            String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_URL));
            boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)) == 1;

            Movie movie = new Movie(id, title, description, posterUrl, isFavorite);
            movies.add(movie);
        }
        cursor.close();
        db.close();

        return movies;

    }
}
