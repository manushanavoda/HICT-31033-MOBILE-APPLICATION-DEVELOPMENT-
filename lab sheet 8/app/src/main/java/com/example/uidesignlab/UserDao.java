
package com.example.uidesignlab;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(UserRecord user);

    @Update
    int update(UserRecord user);

    @Delete
    int delete(UserRecord user);

    @Query("SELECT * FROM UserRecord ORDER BY id DESC")
    List<UserRecord> getAll();

    @Query("DELETE FROM UserRecord")
    int deleteAll();

    // Get all users
    @Query("SELECT * FROM UserRecord ORDER BY id DESC")
    List<UserRecord> getAllCursor();

    // Get user by ID
    @Query("SELECT * FROM UserRecord WHERE id = :id")
    UserRecord getByIdCursor(long id);

    // Update user name
    @Query("UPDATE UserRecord SET name = :name WHERE id = :id")
    int updateName(long id, String name);

    // Delete user by ID
    @Query("DELETE FROM UserRecord WHERE id = :id")
    int deleteById(long id);

    // Count users with the same name
    @Query("SELECT COUNT(*) FROM UserRecord WHERE name = :name")
    int countByName(String name);

}
