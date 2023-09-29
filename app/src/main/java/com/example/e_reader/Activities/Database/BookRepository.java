package com.example.e_reader.Activities.Database;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<BookTable>> allBooks;

    public BookRepository(Application application) {
        BookDatabase db = BookDatabase.getInstance(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    public void insert(BookTable bookTable) {
        new Thread(() -> bookDao.insert(bookTable)).start();
    }

    public void update(BookTable bookTable) {
        new Thread(() -> bookDao.update(bookTable)).start();
    }

    public void delete(BookTable bookTable) {
        new Thread(() -> bookDao.delete(bookTable)).start();
    }

    public void deleteAllBooks() {
        new Thread(() -> bookDao.deleteAllBooks()).start();
    }

    public LiveData<List<BookTable>> getAllBooks() {
        return allBooks;
    }
}