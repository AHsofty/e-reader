package com.example.e_reader.Database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(BookTable bookTable);

    @Update
    void update(BookTable bookTable);

    @Delete
    void delete(BookTable bookTable);

    @Query("DELETE FROM book_table")
    void deleteAllBooks();

    @Query("SELECT * FROM book_table")
    LiveData<List<BookTable>> getAllBooks();
}
