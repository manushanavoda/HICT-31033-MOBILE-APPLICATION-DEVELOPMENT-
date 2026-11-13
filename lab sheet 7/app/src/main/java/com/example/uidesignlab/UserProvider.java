
package com.example.uidesignlab;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserProvider extends ContentProvider {

    public static final String AUTHORITY =
            "com.example.uidesignlab.provider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/users");

    private static final int USERS = 1;
    private static final int USER_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "users", USERS);
        uriMatcher.addURI(AUTHORITY, "users/#", USER_ID);
    }

    private AppDatabase database;

    @Override
    public boolean onCreate() {

        if (getContext() == null) {
            return false;
        }

        database = AppDatabase.getInstance(
                getContext().getApplicationContext()
        );

        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {

        int match = uriMatcher.match(uri);

        if (match != USERS && match != USER_ID) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM UserRecord"
        );

        if (match == USER_ID) {
            selection = "id = ?";
            selectionArgs = new String[]{
                    uri.getLastPathSegment()
            };
        }

        if (selection != null && !selection.trim().isEmpty()) {
            sql.append(" WHERE ").append(selection);
        }

        if (sortOrder != null && !sortOrder.trim().isEmpty()) {
            sql.append(" ORDER BY ").append(sortOrder);
        }

        String[] args = selectionArgs != null
                ? selectionArgs
                : new String[0];

        return database.getOpenHelper()
                .getReadableDatabase()
                .query(sql.toString(), args);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = uriMatcher.match(uri);

        if (match == USERS) {
            return "vnd.android.cursor.dir/vnd."
                    + AUTHORITY + ".users";
        }

        if (match == USER_ID) {
            return "vnd.android.cursor.item/vnd."
                    + AUTHORITY + ".users";
        }

        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    @Nullable
    @Override
    public Uri insert(
            @NonNull Uri uri,
            @Nullable ContentValues values) {

        if (uriMatcher.match(uri) != USERS) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        if (values == null) {
            throw new IllegalArgumentException("Values cannot be null");
        }

        long id = database.getOpenHelper()
                .getWritableDatabase()
                .insert(
                        "UserRecord",
                        SQLiteDatabase.CONFLICT_ABORT,
                        values
                );

        if (id == -1) {
            return null;
        }

        return Uri.withAppendedPath(
                CONTENT_URI,
                String.valueOf(id)
        );
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs) {

        int match = uriMatcher.match(uri);

        if (match != USERS && match != USER_ID) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (values == null) {
            return 0;
        }

        if (match == USER_ID) {
            selection = "id = ?";
            selectionArgs = new String[]{
                    uri.getLastPathSegment()
            };
        }

        return database.getOpenHelper()
                .getWritableDatabase()
                .update(
                        "UserRecord",
                        SQLiteDatabase.CONFLICT_ABORT,
                        values,
                        selection,
                        selectionArgs
                );
    }

    @Override
    public int delete(
            @NonNull Uri uri,
            @Nullable String selection,
            @Nullable String[] selectionArgs) {

        int match = uriMatcher.match(uri);

        if (match != USERS && match != USER_ID) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (match == USER_ID) {
            selection = "id = ?";
            selectionArgs = new String[]{
                    uri.getLastPathSegment()
            };
        }

        return database.getOpenHelper()
                .getWritableDatabase()
                .delete(
                        "UserRecord",
                        selection,
                        selectionArgs
                );
    }
}
