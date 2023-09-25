package com.example.e_reader.Activities.Database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("DELETE FROM book_table")
    void deleteAllBooks();

    @Query("SELECT * FROM book_table")
    LiveData<List<Book>> getAllBooks();
}
