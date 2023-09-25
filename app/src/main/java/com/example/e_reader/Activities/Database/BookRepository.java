package com.example.e_reader.Activities.Database;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application) {
        BookDatabase db = BookDatabase.getInstance(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    public void insert(Book book) {
        new Thread(() -> bookDao.insert(book)).start();
    }

    public void update(Book book) {
        new Thread(() -> bookDao.update(book)).start();
    }

    public void delete(Book book) {
        new Thread(() -> bookDao.delete(book)).start();
    }

    public void deleteAllBooks() {
        new Thread(() -> bookDao.deleteAllBooks()).start();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }
}