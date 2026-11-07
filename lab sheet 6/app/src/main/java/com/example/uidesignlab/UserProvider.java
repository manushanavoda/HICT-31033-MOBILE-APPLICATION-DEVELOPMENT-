
package com.example.uidesignlab;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

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

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {

        if (uriMatcher.match(uri) != USERS &&
                uriMatcher.match(uri) != USER_ID) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        String table = "UserRecord";

        if (uriMatcher.match(uri) == USER_ID) {
            selection = "id = ?";
            selectionArgs = new String[]{
                    uri.getLastPathSegment()
            };
        }


        StringBuilder sql = new StringBuilder(
                "SELECT * FROM UserRecord"
        );

        if (selection != null && !selection.trim().isEmpty()) {
            sql.append(" WHERE ").append(selection);
        }

        if (sortOrder != null && !sortOrder.trim().isEmpty()) {
            sql.append(" ORDER BY ").append(sortOrder);
        }

        String[] args = selectionArgs;

        if (args == null) {
            args = new String[0];
        }

        return database.getOpenHelper()
                .getReadableDatabase()
                .query(
                        sql.toString(),
                        args
                );

    }

    @Override
    public String getType(Uri uri) {

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

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Insert not supported");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete not supported");
    }

    @Override
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs
    ) {
        throw new UnsupportedOperationException("Update not supported");
    }
}
